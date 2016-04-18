package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import lando.systems.ld35.utils.*;

import static lando.systems.ld35.gameobjects.LevelObject.*;
//import static lando.systems.ld35.utils.Assets.batch;

public class LevelInfo {

    public static final float MAP_UNIT_SCALE    = 1f;
    public static final int   SCREEN_TILES_WIDE = 20;
    public static final int   SCREEN_TILES_HIGH = 15;
    public static final int   PIXELS_PER_TILE   = Config.gameWidth / SCREEN_TILES_WIDE;

    public Pool<Rectangle>                  rectanglePool;
    public Array<LevelBoundry>              tiles;
    public Level                            details;
    public TiledMap                         map;
    public Array<ObjectBase>                mapObjects;
    public OrthogonalTiledMapRenderer       mapRenderer;
    public TiledMapTileLayer                foregroundLayer;
    public TiledMapTileLayer                backgroundLayer;
    public int                              levelIndex;
    public ObjectMap<String, Array<Rope>>   ropeGroups;
    public ObjectMap<String, Array<TriggerableEntity>>   triggeredByRopeGroup;

    public LevelInfo(int level, Pool<Rectangle> rectanglePool) {
        createLevel(level, rectanglePool);
    }

    public void createLevel(int level, Pool<Rectangle> rectanglePool) {
        this.levelIndex = level;
        this.details = Level.values()[level];
        this.rectanglePool = rectanglePool;
        this.tiles = new Array<LevelBoundry>();
        this.ropeGroups = new ObjectMap<String, Array<Rope>>();
        this.triggeredByRopeGroup = new ObjectMap<String, Array<TriggerableEntity>>();
        loadMap(details.mapName);
    }

    public void setView(OrthographicCamera camera) {
        mapRenderer.setView(camera);
    }

    public void renderBackground() {
        mapRenderer.renderTileLayer(backgroundLayer);
    }

    public void renderForeground(SpriteBatch batch) {
        mapRenderer.renderTileLayer(foregroundLayer);

        for(int i = 0; i < mapObjects.size; i++) {
            mapObjects.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for(int i = 0; i < mapObjects.size; i++) {
            mapObjects.get(i).update(dt);
        }
    }

    public void loadMap(String mapName){
        final TmxMapLoader mapLoader = new TmxMapLoader();

        map = mapLoader.load(mapName);

        mapRenderer = new OrthogonalTiledMapRenderer(map, MAP_UNIT_SCALE, Assets.batch);

        foregroundLayer = (TiledMapTileLayer) map.getLayers().get("foreground");
        backgroundLayer = (TiledMapTileLayer) map.getLayers().get("background");

        loadMapObjects();
    }

    public void nextLevel() {
        levelIndex++;
        if (levelIndex < Level.values().length) {
            // TODO: do fancy shit here before loading the next level, elapsed time, # times switched, shape histogram, ...
            createLevel(levelIndex, rectanglePool);
        } else {
            // TODO: tell the player, no more levels
        }
    }

    public WindField getWindBounds(Vector2 direction, Rectangle bounds){
        int x1, y1, x2, y2;
        if (direction.y == 0) { // horizontal
            x1 = x2 = (int)bounds.x / 32;
            y1 = (int)bounds.y / 32;
            y2 = y1+1;
        } else {
            y1 = y2 = (int)bounds.y/32;
            x1 = (int)bounds.x/32;
            x2 = x1+1;
        }
        int tempX1 = x1 + (int)direction.x;
        int tempX2 = x2 + (int)direction.x;
        int tempY1 = y1 + (int)direction.y;
        int tempY2 = y2 + (int)direction.y;
        while(tempX1 >= 0 && tempX1 < foregroundLayer.getWidth() && tempX2 >= 0 && tempX2 < foregroundLayer.getWidth() &&
                tempY1 >= 0 && tempY1 < foregroundLayer.getHeight() && tempY2 >= 0 && tempY2 < foregroundLayer.getHeight()){
            if (foregroundLayer.getCell(tempX1, tempY1) != null || foregroundLayer.getCell(tempX2, tempY2) != null){
                break;
            }
            // TODO also test for doors etc
            tempX1 += direction.x;
            tempX2 += direction.x;
            tempY1 += direction.y;
            tempY2 += direction.y;
        }
        tempX1 -= direction.x;
        tempX2 -= direction.x;
        tempY1 -= direction.y;
        tempY2 -= direction.y;
        Vector2 center = new Vector2();
        bounds.getCenter(center);
        Rectangle windBounds = new Rectangle(Math.min(x1, tempX1) * 32, Math.min(y1, tempY1) * 32, (Math.abs(tempX2 - x1) + 1)*32, (Math.abs(tempY2 - y1) + 1)*32);
        return new WindField(center, windBounds, direction);
    }

    public Array<LevelBoundry> getTiles (int startX, int startY, int endX, int endY) {
        if (startX > endX){
            int t = startX;
            startX = endX;
            endX = t;
        }
        if (startY > endY){
            int t = startY;
            startY = endY;
            endY = t;
        }
        for(int i = 0; i < tiles.size; i++) {
            rectanglePool.free(tiles.get(i).rect);
        }
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                TiledMapTileLayer.Cell cell = foregroundLayer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectanglePool.obtain();
                    rect.set(x *32, y *32, 32, 32);
                    tiles.add(new LevelBoundry(cell, rect));
                }
            }
        }
        return tiles;
    }

    public TiledMapTileLayer.Cell getCell(int x, int y){
        return foregroundLayer.getCell(x, y);
    }

    private void loadMapObjects() {
        if (map == null) return;

        mapObjects = new Array<ObjectBase>();

        MapProperties props;
        MapLayer objectLayer = map.getLayers().get("objects");
        for (MapObject object : objectLayer.getObjects()) {
            TiledMapTileMapObject tileObject = (TiledMapTileMapObject) object;
            props = object.getProperties();
            float w = (Float) props.get("width");
            float h = (Float) props.get("height");
            float x = (Float) props.get("x");
            float y = (Float) props.get("y") + h; // NOTE: god dammit... off by 1
            float rotation = tileObject.getRotation() * -1;
            boolean flipX = tileObject.isFlipHorizontally();

            LevelObject type = valueOf((String) props.get("type"));

            switch (type) {
                case spawn:
                    details.startX = x;
                    details.startY = y;
                    break;
                case door:
                    String ropeGroupName = (String) props.get("triggeredByRopeGroup");
                    Float openedRotation = (Float) props.get("openedRotation");
                    // If not set just use starting rotation.  Otherwise reverse it from Tiled.
                    openedRotation = openedRotation == null ? rotation : openedRotation * -1;
                    Door door = new Door(
                            new Rectangle(x, y, w, h),
                            rotation,
                            openedRotation,
                            ((TiledMapTileMapObject) object).getTextureRegion()
                    );

                    if (ropeGroupName != null) {
                        Array<TriggerableEntity> group = triggeredByRopeGroup.get(ropeGroupName);
                        if (group == null) {
                            group = new Array<TriggerableEntity>();
                            triggeredByRopeGroup.put(ropeGroupName, group);
                        }
                        group.add(door);
                    }
                    mapObjects.add(door);
                    break;
                case exit:
                    mapObjects.add(new Exit(new Rectangle(x, y, w, h), rotation, flipX));
                    break;
                case fan:
                    mapObjects.add(new Fan(new Rectangle(x, y, w, h), rotation, flipX, this));
                    break;
                case spikes:
                    mapObjects.add(new Spikes(new Rectangle(x, y, w, h), rotation, flipX, tileObject.getTextureRegion()));
                    break;
                case rope:
                    String groupName = (String) props.get("group");
                    Rope levelRope = new Rope(
                            new Rectangle(x, y, w, h),
                            rotation,
                            flipX,
                            tileObject.getTextureRegion(),
                            groupName);
                    // If it's part of a group...
                    if (groupName != null) {
                        Array<Rope> group = ropeGroups.get(groupName);
                        if (group == null ) {
                            group = new Array<Rope>();
                            ropeGroups.put(groupName, group);
                        }
                        group.add(levelRope);
                    }
                    mapObjects.add(levelRope);
            }
        }
    }

}

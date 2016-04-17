package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import lando.systems.ld35.utils.Config;
import lando.systems.ld35.utils.Level;
import lando.systems.ld35.utils.LevelBoundry;

import static lando.systems.ld35.utils.Assets.batch;

public class LevelInfo {
    public static final float MAP_UNIT_SCALE    = 1f;
    public static final int   SCREEN_TILES_WIDE = 20;
    public static final int   SCREEN_TILES_HIGH = 15;
    public static final int   PIXELS_PER_TILE   = Config.gameWidth / SCREEN_TILES_WIDE;

    public Pool<Rectangle> rectanglePool;
    public Array<LevelBoundry> tiles;
    public Level details;
    public TiledMap map;
    public OrthogonalTiledMapRenderer mapRenderer;
    public TiledMapTileLayer          foregroundLayer;
    public TiledMapTileLayer          backgroundLayer;

    public LevelInfo(int level, Pool<Rectangle> rectanglePool) {
        details = Level.values()[level];
        this.rectanglePool = rectanglePool;
        this.tiles = new Array<LevelBoundry>();
        loadMap(details.mapName);
    }

    public void setView(OrthographicCamera camera) {
        mapRenderer.setView(camera);
    }

    public void renderBackground() {
        mapRenderer.renderTileLayer(backgroundLayer);
    }

    public void renderForeground() {
        mapRenderer.renderTileLayer(foregroundLayer);
    }

    public void loadMap(String mapName){
        final TmxMapLoader mapLoader = new TmxMapLoader();

        map = mapLoader.load(mapName);
        // TODO: Game objects and things
        //loadMapObjects();

        mapRenderer = new OrthogonalTiledMapRenderer(map, MAP_UNIT_SCALE, batch);

        foregroundLayer = (TiledMapTileLayer) map.getLayers().get("foreground");
        backgroundLayer = (TiledMapTileLayer) map.getLayers().get("background");
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
}

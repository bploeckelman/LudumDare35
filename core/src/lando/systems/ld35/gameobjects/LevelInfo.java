package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import lando.systems.ld35.utils.Config;
import lando.systems.ld35.utils.Level;

import static lando.systems.ld35.utils.Assets.batch;

public class LevelInfo {
    public static final float MAP_UNIT_SCALE    = 1f;
    public static final int   SCREEN_TILES_WIDE = 20;
    public static final int   SCREEN_TILES_HIGH = 15;
    public static final int   PIXELS_PER_TILE   = Config.gameWidth / SCREEN_TILES_WIDE;

    public Level details;
    public TiledMap map;
    public OrthogonalTiledMapRenderer mapRenderer;
    public TiledMapTileLayer          foregroundLayer;
    public TiledMapTileLayer          backgroundLayer;

    public LevelInfo(int level) {
        details = Level.values()[level];
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
}

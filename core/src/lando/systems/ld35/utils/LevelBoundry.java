package lando.systems.ld35.utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

public class LevelBoundry {
    public TiledMapTileLayer.Cell tile;
    public Rectangle rect;

    public LevelBoundry(TiledMapTileLayer.Cell tile, Rectangle rect) {
        this.tile = tile;
        this.rect = rect;
    }
}

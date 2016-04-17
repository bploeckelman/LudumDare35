package lando.systems.ld35.utils;

import com.badlogic.gdx.math.Vector2;

public enum Level {
    ONE ("maps/level1.tmx", 32, 64),
    TWO ("maps/level2.tmx", 32, 64);

    public String mapName;
    public float startX;
    public float startY;

    Level(String mapName, float startX, float startY) {
        this.mapName = mapName;
        this.startX = startX;
        this.startY = startY;
    }

    public Vector2 getStart() {
        return new Vector2(startX, startY);
    }
}

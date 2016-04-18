package lando.systems.ld35.utils;

import com.badlogic.gdx.math.Vector2;

public enum Level {

    ZERO  ("maps/level0.tmx",       32, 64, new boolean[] {true, false, false, false, false, false}),
    ONE   ("maps/level1.tmx",       32, 64, new boolean[] {true,  true,  true, false, false, false}),
    L4x1  ("maps/level-4-1.tmx",    32, 64, new boolean[] {true,  true,  true,  true, false, false}),
    L5x1  ("maps/level-5-1.tmx",    32, 64, new boolean[] {true,  true,  true,  true,  true, false}),
    FOUR  ("maps/level_door_tutorial.tmx",       32, 64, new boolean[] {true,  true,  true,  true,  true,  true}),
    FIVE  ("maps/level4.tmx",       32, 64, new boolean[] {true,  true,  true,  true,  true,  true});

    public String mapName;
    public float startX;
    public float startY;
    public boolean[] uiButtonStates;

    Level(String mapName, float startX, float startY, boolean[] uiButtonStates) {
        this.mapName = mapName;
        this.startX = startX;
        this.startY = startY;
        this.uiButtonStates = uiButtonStates;
    }

    public Vector2 getStart() {
        return new Vector2(startX, startY);
    }

}

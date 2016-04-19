package lando.systems.ld35.utils;

import com.badlogic.gdx.math.Vector2;

public enum Level {

    INTRODUCTION     ("maps/level-intro.tmx", 32, 64,               new boolean[] {true, false, false, false, false, false}),
    ROCKETANVIL_EASY ("maps/level-rocket-anvil-easy.tmx", 32, 64,   new boolean[] {true,  true,  true, false, false, false}),
    ROCKETANVIL_MED  ("maps/level-rocket-anvil-medium.tmx", 32, 64, new boolean[] {true,  true,  true, false, false, false}),
    TORUS_TUTORIAL   ("maps/level-torus-tutorial.tmx", 32, 64,      new boolean[] {true,  true,  true,  true, false, false}),
    TORUS_MED        ("maps/level-torus-medium.tmx", 32, 64,        new boolean[] {true,  true,  true,  true, false, false}),
    MAGNET_EASY      ("maps/level-magnet-easy.tmx", 32, 64,         new boolean[] {true,  true,  true,  true,  true, false}),
    ROPE_TUTORIAL    ("maps/level-rope-tutorial.tmx", 32, 64,       new boolean[] {true,  true,  true,  true,  true,  true}),
    DOOR_TUTORIAL    ("maps/level_door_tutorial.tmx", 32, 64,       new boolean[] {true,  true,  true,  true,  true,  true}),
    DOOR_MED         ("maps/level-branching-doors.tmx", 32, 64,     new boolean[] {true,  true,  true,  true,  true,  true}),
    MAGNET_TORUS_HARD("maps/level-magnet-torus-hard.tmx", 32, 64,   new boolean[] {true,  true,  true,  true,  true,  true}),
    CHALLENGE_1      ("maps/level-challenge-1.tmx", 32, 64,         new boolean[] {true,  true,  true,  true,  true,  true}),
    SPIKES_HARD      ("maps/level-spikes-hard.tmx", 32, 64,         new boolean[] {true,  true,  true,  true,  true,  true});

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

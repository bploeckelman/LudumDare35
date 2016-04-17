package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.math.Vector2;

public enum ForceEntityDirection {
    n (0, 1),
    e (1, 0),
    s (0, -1),
    w (-1, 0);

    private final float x;
    private final float y;

    ForceEntityDirection(float x, float y) {
        this.x = x;
        this.y = y;
    }

    Vector2 getDirection() {
        return new Vector2(this.x, this.y);
    }
}

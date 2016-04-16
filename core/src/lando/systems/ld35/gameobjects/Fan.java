package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.math.Vector2;

public class Fan extends ForceEntity {

    Vector2 direction;

    public Fan(Vector2 position, Vector2 direction) {
        super(position, true);
        this.direction = direction;
    }

    public Vector2 getDirection() {
        return direction;
    }

}

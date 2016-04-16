package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.math.Vector2;

public abstract class ForceEntity {

    public Vector2 position;
    public boolean isMagnetic;

    public ForceEntity(Vector2 position, boolean isMagnetic) {
        this.position = position;
        this.isMagnetic = isMagnetic;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean getIsMagnetic() {
        return isMagnetic;
    }

}

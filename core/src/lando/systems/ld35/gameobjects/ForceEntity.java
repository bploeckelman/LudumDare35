package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class ForceEntity extends ObjectBase {

    public boolean isMagnetic;
    public Vector2 direction;

    private ForceEntity(Rectangle bounds, float rotation, boolean flipX) {
        super(bounds, rotation, flipX);
    }

    public ForceEntity(Rectangle bounds, float rotation, boolean flipX, boolean isMagnetic) {
        this(bounds, rotation, flipX);
        this.isMagnetic = isMagnetic;
        this.direction = ForceEntityDirection.fromRotation(rotation, flipX).getDirection();
    }

    public boolean getIsMagnetic() {
        return isMagnetic;
    }
}

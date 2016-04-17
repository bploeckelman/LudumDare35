package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.math.Rectangle;

public abstract class ForceEntity extends ObjectBase {

    public boolean isMagnetic;

    private ForceEntity(Rectangle bounds) {
        super(bounds);
    }

    public ForceEntity(Rectangle bounds, boolean isMagnetic) {
        this(bounds);
        this.isMagnetic = isMagnetic;
    }

    public boolean getIsMagnetic() {
        return isMagnetic;
    }
}

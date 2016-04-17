package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class ForceEntity extends ObjectBase {

    public boolean isMagnetic;
    public Vector2 direction;
    public Vector2 center;

    private ForceEntity(Rectangle bounds, float rotation, boolean flipX) {
        super(bounds, rotation, flipX);
    }

    public ForceEntity(Rectangle bounds, float rotation, boolean flipX, boolean isMagnetic) {
        this(bounds, rotation, flipX);
        this.isMagnetic = isMagnetic;
        this.direction = ForceEntityDirection.fromRotation(rotation, flipX).getDirection();

        realWorldBounds = new Rectangle(bounds);
        if (direction.x != 0){
//            realWorldBounds.x += 32 * direction.x;
        }
        if (direction.y != 0){
            realWorldBounds.width = 64;
            realWorldBounds.height = 32;
//            realWorldBounds.y += 32 * direction.y;
            if (direction.y > 0){
                realWorldBounds.x -= 64;
            } else {
                realWorldBounds.y -= 32;
            }
        }
        center = new Vector2();
        realWorldBounds.getCenter(center);
    }

    public boolean getIsMagnetic() {
        return isMagnetic;
    }

    public Vector2 getMagneticForce(Vector2 pos){
        Vector2 force = new Vector2();
        if (isMagnetic){
            force = center.cpy().sub(pos);
            force.nor();
            force.scl(1f/pos.dst2(center) * 100000);
        }
        return force;
    }
}

package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class ForceEntity extends ObjectBase {

    public boolean isMagnetic;
    public Vector2 direction;
    public Vector2 center;
    public Rectangle realWorldWindBounds;

    private ForceEntity(Rectangle bounds, float rotation, boolean flipX) {
        super(bounds, rotation, flipX);
    }

    public ForceEntity(Rectangle bounds, float rotation, boolean flipX, boolean isMagnetic) {
        this(bounds, rotation, flipX);
        this.isMagnetic = isMagnetic;
        this.direction = ForceEntityDirection.fromRotation(rotation, flipX).getDirection();

        realWorldWindBounds = new Rectangle(bounds);
        if (direction.x != 0){
//            realWorldWindBounds.x += 32 * direction.x;
        }
        if (direction.y != 0){
            realWorldWindBounds.width = 64;
            realWorldWindBounds.height = 32;
//            realWorldWindBounds.y += 32 * direction.y;
            if (direction.y > 0){
                realWorldWindBounds.x -= 64;
            } else {
                realWorldWindBounds.y -= 32;
            }
        }
        center = new Vector2();
        realWorldWindBounds.getCenter(center);
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

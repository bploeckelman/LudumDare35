package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.utils.Assets;

public class Fan extends ForceEntity {

    Animation animation;
    float timer = 0;

    public Fan(Rectangle bounds, float rotation, boolean flipX) {
        super(bounds, rotation, flipX, false);
        this.animation = Assets.fanAnimation;
    }

    public Vector2 getDirection() {
        return direction;
    }

    @Override
    public void update(float delta) {
        timer += delta;
        keyframe = animation.getKeyFrame(timer);
    }
}

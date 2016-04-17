package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.utils.Assets;

public class Fan extends ForceEntity {

    Vector2 direction;
    Animation animation;
    float timer = 0;
    float scaleX = 1;
    float rotation = 0;
    float originX = 0;
    float originY = 0;

    public Fan(Rectangle bounds, Vector2 direction) {
        super(bounds, false);
        this.direction = direction;
        this.animation = Assets.fanAnimation;

        if(this.direction.x < 0) {
            scaleX = -1;
            originX = bounds.width / 2;
        } else if(this.direction.y > 0) {
            rotation = 90;
        } else if(this.direction.y < 0) {
            rotation = -90;
        }
    }

    public Vector2 getDirection() {
        return direction;
    }

    @Override
    public void update(float delta) {
        timer += delta;
        keyframe = animation.getKeyFrame(timer);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(keyframe, bounds.x, bounds.y, originX, originY, bounds.width, bounds.height, scaleX, 1f, rotation);
    }
}

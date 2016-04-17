package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.utils.Assets;
import lando.systems.ld35.utils.WindField;

public class Fan extends ForceEntity {

    Animation animation;
    float timer = 0;
    WindField windField;

    public Fan(Rectangle bounds, float rotation, boolean flipX, LevelInfo level) {
        super(bounds, rotation, flipX, true);
        this.animation = Assets.fanAnimation;
        calcWindField(level);
    }

    public void calcWindField(LevelInfo level){
        windField = level.getWindBounds(direction, realWorldBounds);

    }

    public Vector2 getWindForce(Vector2 pos){
        Vector2 force = new Vector2();
        if (windField.bounds.contains(pos)){
            force.add(direction).scl(60);
        }

        return force;
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
    public void render(SpriteBatch batch){
        super.render(batch);
//        batch.draw(Assets.testTexture, realWorldBounds.x, realWorldBounds.y, realWorldBounds.width, realWorldBounds.height);
    }
}

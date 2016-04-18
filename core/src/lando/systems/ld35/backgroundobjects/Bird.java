package lando.systems.ld35.backgroundobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.gameobjects.LevelInfo;
import lando.systems.ld35.utils.Assets;

/**
 * Created by Doug on 4/17/2016.
 */
public class Bird {
    public Vector2 position;
    public float direction;
    public float accumulator;
    public boolean alive;
    public int type;
    public float speed;
    public float verticalDrift;


    public Bird(LevelInfo level){
        accumulator = MathUtils.random(2f);
        type = MathUtils.random(Assets.birdAnimations.length -1);
        if (MathUtils.randomBoolean()){
            direction = 1;
            position = new Vector2(-Assets.birdAnimations[type].getKeyFrame(accumulator).getRegionWidth(), MathUtils.random(level.foregroundLayer.getHeight() * 32));
        } else {
            direction = -1;
            position = new Vector2(level.foregroundLayer.getWidth() * 32, MathUtils.random(level.foregroundLayer.getHeight() * 32));
        }
        speed = MathUtils.random(40f, 70f);
        verticalDrift = MathUtils.random(15f, 30f);
        alive = true;
    }

    public void update(float dt, LevelInfo level) {
        accumulator += dt;
        position.x += speed  * dt * direction;

        if (position.x <  -Assets.birdAnimations[type].getKeyFrame(accumulator).getRegionWidth()
            || position.x > level.foregroundLayer.getWidth() * 32 + Assets.birdAnimations[type].getKeyFrame(accumulator).getRegionWidth()){
            alive = false;
        }
    }

    public void render(SpriteBatch batch){
        float yFloat = MathUtils.sin(accumulator * .5f) * verticalDrift;

        batch.draw(Assets.birdAnimations[type].getKeyFrame(accumulator), position.x, position.y + yFloat, 16 * -direction, 16);
    }
}

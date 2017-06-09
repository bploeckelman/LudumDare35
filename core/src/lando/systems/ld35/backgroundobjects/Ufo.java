package lando.systems.ld35.backgroundobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.gameobjects.LevelInfo;
import lando.systems.ld35.utils.Assets;

/**
 * Created by Brian on 6/8/2017.
 */
public class Ufo {
    public Vector2 position;
    public float direction;
    public float accumulator;
    public boolean alive;
    public float speed;
    public float verticalDrift;


    public Ufo(LevelInfo level){
        accumulator = MathUtils.random(2f);

        direction = -1;
        position = new Vector2(level.foregroundLayer.getWidth() * 32, MathUtils.random(level.foregroundLayer.getHeight() * 32));

        speed = MathUtils.random(100f, 200f);
        verticalDrift = MathUtils.random(25f, 125f);
        alive = true;
    }

    public void update(float dt, LevelInfo level) {
        accumulator += dt;
        position.x += speed  * dt * direction;

        if (position.x <  -Assets.ufoTexture.getRegionWidth()
         || position.x > level.foregroundLayer.getWidth() * 32 + Assets.ufoTexture.getRegionWidth()){
            alive = false;
        }
    }

    public void render(SpriteBatch batch){
        float yFloat = MathUtils.sin(accumulator * .2f) * verticalDrift;

        batch.draw(Assets.ufoTexture, position.x, position.y + yFloat, 16 * -direction, 16);
    }
}

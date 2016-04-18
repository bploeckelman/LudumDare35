package lando.systems.ld35.backgroundobjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.gameobjects.LevelInfo;
import lando.systems.ld35.utils.Assets;

/**
 * Created by Doug on 4/17/2016.
 */
public class Cloud {
    public Vector2 position;
    public float distance;
    public int type;
    public static float SPEED = 50;
    public boolean alive;

    public Cloud(Vector2 position){
        this.position = position;
        alive = true;
        type = MathUtils.random(Assets.cloudTextures.length -1);
        distance = MathUtils.random(1f, 6f);
    }

    public void update(float dt, LevelInfo level){
        position.x -= SPEED * (1/distance) * dt;
        if (position.x <  -Assets.cloudTextures[type].getRegionWidth() * 2) alive = false;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera){
        TextureRegion tex = Assets.cloudTextures[type];
        batch.draw(tex, position.x, position.y + (.04f * distance * camera.position.y), 0, 0, tex.getRegionWidth(), tex.getRegionHeight(), 2/distance, 2/distance, 0);
    }
}

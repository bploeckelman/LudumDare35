package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.utils.Assets;
import lando.systems.ld35.utils.SoundManager;

/**
 * Created by Doug on 4/16/2016.
 */
public class Balloon {
    public enum State {NORMAL, LIFT, HEAVY, SPINNER, MAGNET}
    public static float MAX_SPEED = 100f;

    public Vector2 position;
    public Vector2 velocity;
    public State currentState;

    public TextureRegion currentTexture;

    public Balloon(Vector2 position){
        currentState = State.NORMAL;
        this.position = position;
        velocity = new Vector2(10, 100);
        currentTexture = Assets.balloonTexture;
    }

    public void changeState(State state){
        State previousState = currentState;
        currentState = state;
        //TODO: trigger an animation
        switch(currentState){
            case NORMAL: currentTexture = Assets.balloonTexture; break;
            case LIFT:   currentTexture = Assets.rocketTexture; break;
            case HEAVY:  currentTexture = Assets.weightTexture; break;
            default:     currentTexture = Assets.testTexture; break;
        }

        SoundManager.playBalloonSound(currentState);
    }

    public void update(float dt, LevelInfo level){
        switch (currentState){
            case LIFT:
                velocity.y += 100 * dt;
                break;
            case HEAVY:
                velocity.y -= 100 * dt;
                break;
        }

        velocity.y = MathUtils.clamp(velocity.y, -MAX_SPEED, MAX_SPEED);

        // TODO magnets

        // TODO wind
        if (position.y > 200 && position.y < 300){
            velocity.x += 40 * dt;
        }

        velocity.x = MathUtils.clamp(velocity.x, -MAX_SPEED, MAX_SPEED);


        position.add(velocity.cpy().scl(dt));
        velocity.scl(.99f);

        // TODO: Do collision detection against level.getTiles
    }

    public void render(SpriteBatch batch){
        batch.draw(currentTexture, position.x, position.y, 32, 32);
    }

}

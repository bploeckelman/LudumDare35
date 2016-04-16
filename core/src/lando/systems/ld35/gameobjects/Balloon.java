package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.utils.Assets;

/**
 * Created by Doug on 4/16/2016.
 */
public class Balloon {
    public enum State {NORMAL, LIFT, HEAVY, SPINNER, MAGNET}
    public static float MAX_SPEED = 100f;

    public Vector2 position;
    public Vector2 velocity;
    public State currentState;

    public Balloon(Vector2 position){
        currentState = State.NORMAL;
        this.position = position;
        velocity = new Vector2(10, 100);
    }

    public void changeState(State state){
        currentState = state;
    }

    /**
     * @deprecated
     * Remove this when we have a real UI
     */
    public void cycleState(){
        switch(currentState){
            case NORMAL:
                currentState = State.LIFT;
                break;
            case LIFT:
                currentState = State.HEAVY;
                break;
            case HEAVY:
                currentState = State.NORMAL;
                break;
        }
    }

    public void update(float dt){
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

        position.add(velocity.cpy().scl(dt));
        velocity.scl(.99f);
    }

    public void render(SpriteBatch batch){
        batch.draw(Assets.testTexture, position.x, position.y, 32, 32);
    }

}

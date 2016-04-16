package lando.systems.ld35.gameobjects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.Animation;
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

    public static final float ANIM_DURATION = 0.5f;
    public static float MAX_SPEED = 100f;

    public Vector2       position;
    public Vector2       velocity;
    public State         currentState;
    public TextureRegion currentTexture;
    public boolean       animating;
    public MutableFloat  animationTimer;
    public Animation     currentAnimation;

    public Balloon(Vector2 position) {
        currentState = State.NORMAL;
        this.position = position;
        velocity = new Vector2(10, 100);
        currentTexture = Assets.balloonTexture;
        animating = false;
        animationTimer = new MutableFloat(0);
        currentAnimation = null;
    }

    public void changeState(State state) {
        SoundManager.playBalloonSound(currentState);
        State previousState = currentState;
        currentState = state;

        // TODO: previous state -> balloon then balloon -> current state then animating = false && currentTexture = currentState
        animating = true;
        animationTimer.setValue(0f);
        currentAnimation = Assets.balloonToRocketAnimation;
        Tween.to(animationTimer, -1, ANIM_DURATION / 2f)
                .target(currentAnimation.getAnimationDuration())
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        Tween.to(animationTimer, -1, ANIM_DURATION / 2f)
                                .target(0f)
                                .setCallback(new TweenCallback() {
                                    @Override
                                    public void onEvent(int type, BaseTween<?> source) {
                                        animating = false;
                                        currentAnimation = null;
                                        switch(currentState){
                                            case NORMAL: currentTexture = Assets.balloonTexture; break;
                                            case LIFT:   currentTexture = Assets.rocketTexture; break;
                                            case HEAVY:  currentTexture = Assets.weightTexture; break;
                                            default:     currentTexture = Assets.testTexture; break;
                                        }
                                    }
                                })
                                .start(Assets.tween);
                    }
                })
                .start(Assets.tween);
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
        if (animating && currentAnimation != null) {
            batch.draw(currentAnimation.getKeyFrame(animationTimer.floatValue()), position.x, position.y, 32, 32);
        } else {
            batch.draw(currentTexture, position.x, position.y, 32, 32);
        }
    }

}

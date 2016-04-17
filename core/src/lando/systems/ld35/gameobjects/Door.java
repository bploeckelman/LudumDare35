package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.utils.Assets;

public class Door extends TriggerableEntity {

    private static final float DEGREES_PER_SECOND = 90;

    private String tag = this.toString();

    public enum State {OPEN, CLOSED, OPENING, CLOSING }

    private Vector2 dimensions; // x=width, y=height
    private Vector2 position; // Origin point and point of rotation.  The center of the left side.
    private float closedRotation;
    private float openedRotation;

    private int openDirection;
    private State state;
    private float rotation;
    private static TextureRegion textureRegion;

    // -----------------------------------------------------------------------------------------------------------------

    public Door(Vector2 position, Vector2 dimensions, float closedRotation, float openedRotation) {
        this.dimensions = dimensions;
        this.position = position;
        this.closedRotation = closedRotation;
        this.openedRotation = openedRotation;
        this.rotation = this.closedRotation;
        this.state = State.CLOSED;
        Gdx.app.log(tag, "x=" + String.valueOf(this.position.x));
        openDirection = (this.openedRotation > this.closedRotation) ? 1 : -1;
        textureRegion = new TextureRegion(Assets.whitePixelTexture);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void onTrigger() {
        switch(state) {
            case OPEN:
            case OPENING:
                setState(State.CLOSING);
                break;
            case CLOSED:
            case CLOSING:
                setState(State.OPENING);
                break;
            default:
                Gdx.app.error(tag, "unrecognized state");
        }
    }

    public void open() {
        setState(State.OPENING);
    }

    public void close() {
        setState(State.CLOSING);
    }

    private void setState(State state) {
        if (this.state != state) {
            this.state = state;
//            Gdx.app.log(tag, "state='" + state.toString() + "'");
        }
    }

    // -----------------------------------------------------------------------------------------------------------------


    private static final float ACCELERATION = 220f;
    private static final float MAX_ROTATION_SPEED = 144f;
    private float rotationSpeed = 0f;

    public void update(float dt){

        switch (state) {
            case OPEN:
                rotation = openedRotation;
                break;
            case CLOSED:
                rotation = closedRotation;
                break;
            case OPENING:
                // Speed up?
                rotationSpeed = Math.min(rotationSpeed + (ACCELERATION * dt * openDirection), MAX_ROTATION_SPEED);
                // Rotate
                rotation += rotationSpeed * dt;
                // Test for open
                if ((openedRotation - rotation) * openDirection <= 0) {
                    // Destination reached.
                    setState(State.OPEN);
                    rotationSpeed = 0f;
                    rotation = openedRotation;
                }
                break;
            case CLOSING:
                // Speed up?
                rotationSpeed = Math.min(rotationSpeed + (ACCELERATION * dt * openDirection * -1), MAX_ROTATION_SPEED);
                // Rotate
                rotation += rotationSpeed * dt;
                // Test for closed
                if ((rotation - closedRotation) * openDirection <= 0) {
                    // Destination reached.
                    setState(State.CLOSED);
                    rotationSpeed = 0f;
                    rotation = closedRotation;
                }
                break;
        }


//        // Change in state?
//        if (stateHasChanged) {
//            // Are we now closing or opening?
//            if (state == State.CLOSING || state == State.OPENING) {
//                stateHasChanged = false;
//                //
//                // todo: cancel running tween if there is one
//                if (tween != null) {
//                    Gdx.app.log(tag, "killing existing tween");
////                    tween.free();
//                    tween.setCallback(null);
//                    tween.kill();
//                    this.rotation = new MutableFloat(rotation.floatValue());
//                }
//                // What's the destination?
//                float destination = (state == State.OPENING) ? openedRotation : closedRotation;
//                float duration = getDuration(rotation.floatValue() - destination);
//                Gdx.app.log(tag, "dest:" + String.valueOf(destination) + ", dur:" + String.valueOf(duration));
//                tween = Tween.to(rotation, -1, duration)
//                        .target(destination)
//                        .setCallback(new TweenCallback() {
//                            @Override
//                            public void onEvent(int i, BaseTween<?> baseTween) {
//                                // Complete the state
//                                switch(state) {
//                                    case OPENING:
//                                        setState(State.OPEN);
//                                        break;
//                                    case CLOSING:
//                                        setState(State.CLOSED);
//                                        break;
//                                    default:
//                                        Gdx.app.log(tag, "Completed tween and found unexpected state '" + state.toString() + "'");
//                                        break;
//                                }
//                            }
//                        })
//                        .start(Assets.tween);
//            } else {
//                Gdx.app.log(tag, "Change to open or closed detected.");
//            }
//            // Flip the flag as we've now observed the state change.
//            stateHasChanged = false;
//        }
    }

//    private float getDuration(float degrees) {
//        return (Math.abs(degrees) / DEGREES_PER_SECOND);
//    }


    public void render(SpriteBatch batch){
        Color c = batch.getColor();
        batch.setColor(0,0,1,1);
        batch.draw(
                textureRegion,
                position.x, position.y - (dimensions.y / 2),
                0, (dimensions.y / 2),
                dimensions.x, dimensions.y,
                1, 1, rotation
        );
        batch.setColor(c);
    }

}

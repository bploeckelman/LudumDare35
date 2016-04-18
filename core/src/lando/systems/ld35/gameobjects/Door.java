package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Door extends TriggerableEntity {

    private static final float ACCELERATION = 220f;
    private static final float MAX_ROTATION_SPEED = 144f;
    private float rotationSpeed = 0f;

    private String tag = this.toString();

    public enum State {OPEN, CLOSED, OPENING, CLOSING }

    private Vector2 dimensions; // x=width, y=height
    private Vector2 position; // Origin point and point of rotation.  The center of the left side.
    private Vector2 rotationOrigin;

    private float closedRotation;
    private float openedRotation;

    private int openDirection;
    private State state;
    private float rotation;
    private TextureRegion textureRegion;

    // -----------------------------------------------------------------------------------------------------------------

    public Door(Rectangle bounds, float closedRotation, float openedRotation, TextureRegion textureRegion) {
        super(bounds, closedRotation, false);
        this.dimensions = new Vector2(bounds.width, bounds.height);
        this.position = new Vector2(bounds.x, bounds.y);
        this.closedRotation = closedRotation;
        this.openedRotation = openedRotation;
        this.textureRegion = textureRegion;
        //

//        this.rotationOrigin = new Vector2(bounds)
        //
        this.rotation = this.closedRotation;
        this.state = State.CLOSED;
        Gdx.app.log(tag, "x=" + String.valueOf(this.position.x));
        openDirection = (this.openedRotation > this.closedRotation) ? 1 : -1;

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

    public void update(float dt) {

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
    }

    public void render(SpriteBatch batch){
//        Color c = batch.getColor();
//        batch.setColor(0,0,1,1);
        batch.draw(
                textureRegion,
                position.x, position.y - (dimensions.y / 2),
                0, (dimensions.y / 2),
                dimensions.x, dimensions.y,
                1, 1, rotation
        );
//        batch.setColor(c);
    }

}

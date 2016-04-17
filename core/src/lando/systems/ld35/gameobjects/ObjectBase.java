package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld35.utils.Assets;

public abstract class ObjectBase {
    static float scaleY = 1f;

    TextureRegion keyframe;
    Rectangle bounds;
    float scaleX = 1;
    float rotation = 0;
    float originX = 0;
    float originY = 0;
    public Rectangle realWorldBounds;


    public ObjectBase(Rectangle bounds, float rotation, boolean flipX) {
        this.keyframe = new TextureRegion(Assets.testTexture);
        this.bounds = bounds;
        this.rotation = rotation;
        realWorldBounds = new Rectangle(bounds);
        if(flipX) {
            scaleX = -1;
            originX = bounds.width / 2;
        }
    }

    public abstract void update(float delta);

    public void touch() {}

    public void render(SpriteBatch batch) {
        batch.draw(keyframe, bounds.x, bounds.y, originX, originY, bounds.width, bounds.height, scaleX, scaleY, rotation);
    }

    public Rectangle getBounds() { return bounds; }
    public TextureRegion getKeyframe() { return keyframe; }
}

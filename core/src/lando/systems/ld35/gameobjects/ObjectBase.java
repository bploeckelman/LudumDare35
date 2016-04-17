package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld35.utils.Assets;

public abstract class ObjectBase {
    TextureRegion keyframe;
    Rectangle bounds;

    public ObjectBase(Rectangle bounds) {
        this.keyframe = new TextureRegion(Assets.testTexture);
        this.bounds = bounds;
    }

    public abstract void update(float delta);

    public void touch() {}

    public void render(SpriteBatch batch) {
        batch.draw(keyframe, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() { return bounds; }
    public TextureRegion getKeyframe() { return keyframe; }
}

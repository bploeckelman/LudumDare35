package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Rope extends ObjectBase {
    public Array<Rope> group;
    public boolean vertical;

    public Rope(Rectangle bounds, float rotation, boolean flipX, TextureRegion textureRegion, Array<Rope> group) {
        super(bounds, rotation, flipX);
        this.group = group;
        keyframe = textureRegion;
        texturePixmap = getPixmap();
    }

    @Override
    public void update(float delta) {

    }
}

package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld35.utils.Assets;

public class Spikes extends ObjectBase {
    public Spikes(Rectangle bounds, float rotation, boolean flipX, TextureRegion textureRegion) {
        super(bounds, rotation, flipX);
        keyframe = textureRegion;
        texturePixmap = getPixmap();
    }

    @Override
    public void update(float delta) {

    }
}

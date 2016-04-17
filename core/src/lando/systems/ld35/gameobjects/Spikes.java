package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld35.utils.Assets;

public class Spikes extends ObjectBase {
    public Spikes(Rectangle bounds, float rotation, boolean flipX) {
        super(bounds, rotation, flipX);
        keyframe = Assets.spikesTexture;
    }

    @Override
    public void update(float delta) {

    }
}

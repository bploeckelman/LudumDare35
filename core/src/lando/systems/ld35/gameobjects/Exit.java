package lando.systems.ld35.gameobjects;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld35.utils.Assets;

/**
 * Brian Ploeckelman created on 4/17/2016.
 */
public class Exit extends ObjectBase {

    private static final float DURATION = 0.5f;

    private MutableFloat animTimer;

    public Exit(Rectangle bounds, float rotation, boolean flipX) {
        super(bounds, rotation, flipX);
        animTimer = new MutableFloat(0f);
        Tween.to(animTimer, -1, DURATION)
                .target(Assets.netAnimation.getAnimationDuration())
                .repeatYoyo(-1, 0f)
                .start(Assets.tween);
    }

    @Override
    public void update(float delta) {
        keyframe = Assets.netAnimation.getKeyFrame(animTimer.floatValue());
    }

}

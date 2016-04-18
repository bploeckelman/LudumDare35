package lando.systems.ld35.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.utils.Assets;
import lando.systems.ld35.utils.Config;
import lando.systems.ld35.utils.accessors.ColorAccessor;
import lando.systems.ld35.utils.accessors.RectangleAccessor;

/**
 * Brian Ploeckelman created on 4/17/2016.
 */
public class LevelButton extends Button {

    public final int          levelId;
    public       MutableFloat alpha;
    public       MutableFloat angle;
    public       Color        color;

    private final String  levelIdString;
    private       Vector2 accum;
    private       Vector2 floatOffset;
    private       Vector2 textPos;
    private       boolean settled;
    private       boolean drawText;

    public LevelButton(int levelId, Rectangle bounds) {
        super(Assets.buttonTexture, bounds);
        this.levelId = levelId;
        this.alpha = new MutableFloat(0f);
        this.angle = new MutableFloat(MathUtils.random(-5f, 5f));
        this.color = new Color(1f, 1f, 1f, 1f);
        this.levelIdString = Integer.toString(levelId + 1);
        this.accum = new Vector2(MathUtils.random(0.1f, 1f),
                                 MathUtils.random(0.1f, 1f));
        this.floatOffset = new Vector2();
        final GlyphLayout layout = new GlyphLayout(Assets.font_round_32, levelIdString);
        this.textPos = new Vector2(bounds.x + (bounds.width / 2f - layout.width / 2f),
                                   bounds.y + (bounds.height / 2f) + (layout.height / 2f) + 15f);
        this.settled = false;
        this.drawText = false;
        this.bounds.y = -200f;
        final Color newColor = new Color();
        if (levelId <= Assets.getMaxLevelCompleted()) newColor.set(Config.balloonColor);  // game balloon red
        else                                          newColor.set(18f / 255f, 227f / 255f, 119f / 255f, 1f); // greenish, sorta
        Tween.to(angle, -1, MathUtils.random(1f, 1.5f))
             .target(-1f * angle.floatValue())
             .repeatYoyo(-1, 0f)
             .start(Assets.tween);
        Timeline.createSequence()
                .push(Tween.to(this.bounds, RectangleAccessor.Y, 1.2f)
                           .target(bounds.y)
                           .ease(Bounce.OUT)
                           .delay(levelId * 0.3f)
                           .setCallback(new TweenCallback() {
                               @Override
                               public void onEvent(int type, BaseTween<?> source) {
                                   drawText = true;
                               }
                           }))
                .push(Tween.to(this.color, ColorAccessor.RGB, 0.3f)
                           .target(newColor.r, newColor.g, newColor.b)
                           .delay(levelId * 0.1f))
                .start(Assets.tween);

    }

    public void update(float dt) {
        accum.add(dt, dt);
        floatOffset.set(MathUtils.cos(accum.x * 2f) * 1.5f,
                        MathUtils.sin(accum.y * 5f) * 2.0f);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(color);
        batch.draw(region,
                   bounds.x + floatOffset.x,
                   bounds.y + floatOffset.y,
                   bounds.width / 2f,
                   bounds.height / 2f,
                   bounds.width,
                   bounds.height,
                   1f,
                   1f,
                   angle.floatValue());
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void renderText(SpriteBatch batch) {
        if (!drawText) return;
        Assets.font_round_32.draw(batch, levelIdString, textPos.x + floatOffset.x, textPos.y + floatOffset.y);
    }

}

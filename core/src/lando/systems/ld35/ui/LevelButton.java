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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.screens.LevelSelectScreen;
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
    public        boolean active;
    GlyphLayout layout;

    public LevelButton(int levelId, float x, float y, float w, float h) {
        this(levelId, new Rectangle(x, y, w, h));
    }

    public LevelButton(int levelId, Rectangle bounds) {
        super(Assets.buttonTexture, bounds);
        this.levelId = levelId;
        this.alpha = new MutableFloat(0f);
        this.angle = new MutableFloat(MathUtils.random(-5f, 5f));
        this.color = new Color(1f, 1f, 1f, 1f);

        if      (levelId == Integer.MIN_VALUE) this.levelIdString = "<";
        else if (levelId == Integer.MAX_VALUE) this.levelIdString = ">";
        else {
            this.levelIdString = Integer.toString(levelId + 1);
        }

        this.accum = new Vector2(MathUtils.random(0.1f, 1f),
                                 MathUtils.random(0.1f, 1f));
        this.floatOffset = new Vector2();
        this.layout = new GlyphLayout(Assets.font_round_32, levelIdString);
        this.textPos = new Vector2(bounds.x + (bounds.width / 2f),
                                   bounds.y + ((bounds.height) / 1.7f));
        this.drawText = false;
        this.settled = false;
        this.active = levelId <= Assets.getMaxLevelCompleted();
        final Color newColor = new Color();
        if (!active)  newColor.set(Config.balloonColor);  // game balloon red
        else          newColor.set(18f / 255f, 227f / 255f, 119f / 255f, 1f); // greenish, sorta

        Tween.to(angle, -1, MathUtils.random(1f, 1.5f))
             .target(-1f * angle.floatValue())
             .repeatYoyo(-1, 0f)
             .start(Assets.tween);

        // Only bounce in actual level buttons, not the 'paging' buttons
        if (levelId != Integer.MIN_VALUE && levelId != Integer.MAX_VALUE) {
            this.bounds.y = -130f;
            Timeline.createSequence()
                    .push(Tween.to(this.bounds, RectangleAccessor.Y, 0.9f)
                               .target(bounds.y)
                               .ease(Bounce.OUT)
                               .delay((levelId % LevelSelectScreen.LEVELS_PER_PAGE) * 0.15f)
                               .setCallback(new TweenCallback() {
                                   @Override
                                   public void onEvent(int type, BaseTween<?> source) {
                                       drawText = true;
                                   }
                               }))
                    .push(Tween.to(this.color, ColorAccessor.RGB, 0.3f)
                               .target(newColor.r, newColor.g, newColor.b))
                    .start(Assets.tween);
        } else {
            // Show pagination buttons (visibility checks handled in LevelSelectScreen)
            this.drawText = true;
            this.settled = true;
            this.color.set(Config.pageBtnColor);
        }
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
        Assets.font_round_32.getData().setScale(1);
        layout.setText(Assets.font_round_32, levelIdString);
        float scale = (bounds.width/3)/ layout.width;
        Assets.font_round_32.getData().setScale(scale);
        layout.setText(Assets.font_round_32, levelIdString);
        Assets.fontShader.setUniformf("u_scale",scale);

        Assets.font_round_32.draw(batch, levelIdString, textPos.x + floatOffset.x - (layout.width/3), textPos.y + floatOffset.y + layout.height/2f);
        Assets.font_round_32.getData().setScale(1);
    }

}

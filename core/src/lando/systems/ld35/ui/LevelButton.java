package lando.systems.ld35.ui;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld35.utils.Assets;

/**
 * Brian Ploeckelman created on 4/17/2016.
 */
public class LevelButton extends Button {

    public MutableFloat alpha;
    public boolean      drawText;

    public final int levelId;

    private final String levelIdString;
    private float accumX;
    private float accumY;
    private float textX;
    private float textY;

    public LevelButton(int levelId, Rectangle bounds) {
        super(Assets.buttonTexture, bounds);
        this.alpha = new MutableFloat(0f);
        this.drawText = false;
        this.levelId = levelId;
        this.levelIdString = Integer.toString(levelId);

        this.accumX = MathUtils.random(0.1f, 1f);
        this.accumY = MathUtils.random(0.1f, 1f);

        final GlyphLayout layout = new GlyphLayout();
        layout.setText(Assets.font_round_32, levelIdString);
        this.textX = bounds.x + (bounds.width / 2f - layout.width / 2f);
        this.textY = bounds.y + (bounds.height / 2f) + (layout.height / 2f) + 15f;
    }

    public void update(float dt) {
        accumY += dt;

        float danceMagicDance = MathUtils.sin(accumY * 4f) * 0.4f;
        float magicDanceMagic = MathUtils.sin(accumY * 2f) * 0.2f;
        bounds.x += magicDanceMagic;
        bounds.y += danceMagicDance;

        textX += magicDanceMagic;
        textY += danceMagicDance;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(1f, 1f, 1f, alpha.floatValue());
        batch.draw(region, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void renderText(SpriteBatch batch) {
        if (!drawText) return;
        batch.setColor(1f, 1f, 1f, alpha.floatValue());
        Assets.font_round_32.draw(batch, levelIdString, textX, textY);
        batch.setColor(1f, 1f, 1f, 1f);
    }

}

package lando.systems.ld35.ui;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.utils.Assets;

/**
 * Brian Ploeckelman created on 4/17/2016.
 */
public class LevelButton extends Button {

    public MutableFloat alpha;
    public boolean      drawText;

    public final int levelId;

    private final String  levelIdString;
    private       Vector2 accum;
    private       Vector2 floatOffset;
    private       Vector2 textPos;

    public LevelButton(int levelId, Rectangle bounds) {
        super(Assets.buttonTexture, bounds);
        this.alpha = new MutableFloat(0f);
        this.drawText = false;
        this.levelId = levelId;
        this.levelIdString = Integer.toString(levelId + 1);
        this.accum = new Vector2(MathUtils.random(0.1f, 1f),
                                 MathUtils.random(0.1f, 1f));
        this.floatOffset = new Vector2();

        final GlyphLayout layout = new GlyphLayout(Assets.font_round_32, levelIdString);
        this.textPos = new Vector2(bounds.x + (bounds.width / 2f - layout.width / 2f),
                                   bounds.y + (bounds.height / 2f) + (layout.height / 2f) + 15f);
    }

    public void update(float dt) {
        accum.add(dt, dt);
        floatOffset.set(MathUtils.cos(accum.x * 2f) * 1.5f,
                        MathUtils.sin(accum.y * 5f) * 2.0f);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(1f, 1f, 1f, alpha.floatValue());
        batch.draw(region, bounds.x + floatOffset.x, bounds.y + floatOffset.y, bounds.width, bounds.height);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void renderText(SpriteBatch batch) {
        if (!drawText) return;
        batch.setColor(1f, 1f, 1f, alpha.floatValue());
        Assets.font_round_32.draw(batch, levelIdString, textPos.x + floatOffset.x, textPos.y + floatOffset.y);
        batch.setColor(1f, 1f, 1f, 1f);
    }

}

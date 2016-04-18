package lando.systems.ld35.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld35.utils.Assets;

/**
 * Brian Ploeckelman created on 4/16/2016.
 */
public class Button {

    private static final float offset = 4f;

    public final TextureRegion region;
    public final Rectangle     bounds;

    public boolean active;
    public boolean drawNinePatch;

    public Button(TextureRegion region, Rectangle bounds) {
        this.region = region;
        this.bounds = new Rectangle(bounds);
        this.active = false;
        this.drawNinePatch = true;
    }

    public Button(TextureRegion region, Rectangle bounds, boolean drawNinePatch) {
        this(region, bounds);
        this.drawNinePatch = drawNinePatch;
    }

    public boolean checkForTouch(float screenX, float screenY) {
        return bounds.contains(screenX,screenY);
    }

    public void render(SpriteBatch batch) {
        if (drawNinePatch) {
            if (active) Assets.selectedNinepatch.draw(batch,
                                                      bounds.x - offset,
                                                      bounds.y - offset,
                                                      bounds.width + 2f * offset,
                                                      bounds.height + 2f * offset);
            else        Assets.transparentNinepatch.draw(batch,
                                                      bounds.x - offset,
                                                      bounds.y - offset,
                                                      bounds.width + 2f * offset,
                                                      bounds.height + 2f * offset);
        }
        batch.draw(region, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}

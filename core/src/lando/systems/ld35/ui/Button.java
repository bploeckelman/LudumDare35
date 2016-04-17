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

    public Button(TextureRegion region, Rectangle bounds) {
        this.region = region;
        this.bounds = bounds;
        this.active = false;
    }

    public boolean checkForTouch(float screenX, float screenY) {
        return bounds.contains(screenX,screenY);
    }

    public void render(SpriteBatch batch) {
        if (active) Assets.selectedNinepatch.draw(batch, bounds.x - offset, bounds.y - offset, bounds.width + 2f * offset, bounds.height + 2f * offset);
        else        Assets.transparentNinepatch.draw(batch, bounds.x - offset, bounds.y - offset, bounds.width + 2f * offset, bounds.height + 2f * offset);
        batch.draw(region, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}

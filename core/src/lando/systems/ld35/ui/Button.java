package lando.systems.ld35.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld35.utils.Assets;

/**
 * Brian Ploeckelman created on 4/16/2016.
 */
public class Button {

    public final TextureRegion region;
    public final Rectangle     bounds;

    public boolean active;

    public Button(TextureRegion region, Rectangle bounds) {
        this.region = region;
        this.bounds = bounds;
        this.active = false;
    }

    public boolean checkForTouch(float screenX, float screenY) {
        active = bounds.contains(screenX, screenY);
        return active;
    }

    public void render(SpriteBatch batch) {
        if (active) Assets.backgroundNinepatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        else        Assets.transparentNinepatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.draw(region, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}

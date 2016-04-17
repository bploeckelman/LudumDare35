package lando.systems.ld35.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld35.gameobjects.Balloon;

/**
 * Brian Ploeckelman created on 4/16/2016.
 */
public class StateButton extends Button {

    public final Balloon.State state;
    public boolean enabled;

    public StateButton(Balloon.State state, TextureRegion region, Rectangle bounds) {
        super(region, bounds);
        this.state = state;
        this.enabled = false;
    }

}

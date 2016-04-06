package lando.systems.ld35.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.utils.Assets;
import lando.systems.ld35.utils.Config;
import lando.systems.ld35.utils.Utils;

/**
 * Brian Ploeckelman created on 1/17/2016.
 */
public class GameScreen extends BaseScreen implements InputProcessor {

    public GameScreen() {
        super();
        Utils.glClearColor(Config.bgColor);
        Gdx.input.setInputProcessor(this);
    }

    // ------------------------------------------------------------------------
    // BaseScreen Implementation ----------------------------------------------
    // ------------------------------------------------------------------------

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            LudumDare35.game.screen = new MenuScreen();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(Assets.testTexture,
                   camera.viewportWidth  / 2f - Assets.testTexture.getWidth() / 2f,
                   camera.viewportHeight / 2f - Assets.testTexture.getHeight() / 2f,
                   Assets.testTexture.getWidth(),
                   Assets.testTexture.getHeight());
        batch.end();
    }

    // ------------------------------------------------------------------------
    // InputListener Interface ------------------------------------------------
    // ------------------------------------------------------------------------
    private Vector2 touchPosScreen    = new Vector2();
    private Vector3 touchPosUnproject = new Vector3();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPosUnproject = camera.unproject(new Vector3(screenX, screenY, 0));
        touchPosScreen.set(touchPosUnproject.x, touchPosUnproject.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    // ------------------------------------------------------------------------
    // Private Implementation -------------------------------------------------
    // ------------------------------------------------------------------------

}

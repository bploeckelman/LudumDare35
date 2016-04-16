package lando.systems.ld35.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.gameobjects.Balloon;
import lando.systems.ld35.gameobjects.LevelInfo;
import lando.systems.ld35.ui.StateButton;
import lando.systems.ld35.utils.Assets;
import lando.systems.ld35.utils.Config;
import lando.systems.ld35.utils.Utils;

/**
 * Brian Ploeckelman created on 4/16/2016.
 */
public class GameScreen extends BaseScreen implements InputProcessor {

    LevelInfo level;
    Balloon playerBalloon;
    Array<StateButton> stateButtons;
    Pool<Rectangle> rectPool;

    public GameScreen() {
        super();
        rectPool = Pools.get(Rectangle.class);

        loadLevel(0);
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
        playerBalloon.update(dt, level);
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        level.setView(camera);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        level.renderBackground();
        playerBalloon.render(batch);
        level.renderForeground();

        for (StateButton stateButton : stateButtons) {
            stateButton.render(batch);
        }
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

        StateButton enabledButton = null;
        for (StateButton stateButton : stateButtons) {
            if (stateButton.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
                playerBalloon.changeState(stateButton.state);
                enabledButton = stateButton;
            }
        }
        if (enabledButton != null) {
            for (StateButton stateButton : stateButtons) {
                if (stateButton != enabledButton) {
                    stateButton.active = false;
                }
            }
        }

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

    private void loadLevel(int levelId){
        level = new LevelInfo(levelId, rectPool);
        playerBalloon = new Balloon(level.details.getStart());
        layoutUI();
    }

    private void resetLevel(){
        // TODO reset the level quickly
    }

    private void layoutUI() {
        // TODO: center buttons nicely
        stateButtons = new Array<StateButton>();
        stateButtons.add(new StateButton(Balloon.State.NORMAL,
                                         new TextureRegion(Assets.balloonTexture),
                                         new Rectangle(10 * 1f + 32 * 0f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.LIFT,
                                         new TextureRegion(Assets.rocketTexture),
                                         new Rectangle(10 * 2f + 32 * 1f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.HEAVY,
                                         new TextureRegion(Assets.weightTexture),
                                         new Rectangle(10 * 3f + 32 * 2f, 10, 32, 32)));
    }

}

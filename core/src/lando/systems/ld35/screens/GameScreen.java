package lando.systems.ld35.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
import lando.systems.ld35.utils.LevelBoundry;
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
//        Vector3 worldPoint = camera.unproject(new Vector3(screenX, screenY, 0));
//        Array<LevelBoundry> cells = level.getTiles((int)worldPoint.x /32, (int)worldPoint.y / 32, (int)worldPoint.x /32, (int)worldPoint.y /32);
//        if (cells.size > 0){
//            for (LevelBoundry boundry: cells) {
//                Texture t = boundry.tile.getTile().getTextureRegion().getTexture();
//                if (!t.getTextureData().isPrepared()) {
//                    t.getTextureData().prepare();
//                }
//                Pixmap tilePixmap = t.getTextureData().consumePixmap();
//                int pxX = (int)(worldPoint.x % 32f) + boundry.tile.getTile().getTextureRegion().getRegionX();
//                int pxY = (int)(worldPoint.y % 32f) + boundry.tile.getTile().getTextureRegion().getRegionY();
//                Gdx.app.log("Touch", "X:" + pxX + " Y:" + pxY);
//
//                int pix = tilePixmap.getPixel(pxX, tilePixmap.getHeight() - 1 - pxY);
//                Gdx.app.log("Touch", pix+"");
//            }
//        }
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
        playerBalloon = new Balloon(level.details.getStart(), this);
        layoutUI();
    }

    private void resetLevel(){
        // TODO reset the level quickly
    }

    private void layoutUI() {
        int numButtons = 6;
        float padding = 10f;
        float size = 32f;
        float width = (padding + size) * numButtons;
        float leftMargin = camera.viewportWidth / 2f - width / 2f;

        stateButtons = new Array<StateButton>();
        stateButtons.add(new StateButton(Balloon.State.NORMAL, Assets.balloonTexture,
                                         new Rectangle(leftMargin + 10 * 0f + 32 * 0f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.LIFT, Assets.rocketTexture,
                                         new Rectangle(leftMargin + 10 * 1f + 32 * 1f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.HEAVY, Assets.weightTexture,
                                         new Rectangle(leftMargin + 10 * 2f + 32 * 2f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.SPINNER, Assets.torusTexture,
                                         new Rectangle(leftMargin + 10 * 3f + 32 * 3f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.MAGNET, Assets.magnetTexture,
                                         new Rectangle(leftMargin + 10 * 4f + 32 * 4f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.BUZZSAW, Assets.buzzsawTexture,
                                         new Rectangle(leftMargin + 10 * 5f + 32 * 5f, 10, 32, 32)));
        stateButtons.get(0).active = true;
   }

}

package lando.systems.ld35.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.ui.LevelButton;
import lando.systems.ld35.utils.Assets;
import lando.systems.ld35.utils.Config;
import lando.systems.ld35.utils.Level;
import lando.systems.ld35.utils.Utils;

/**
 * Brian Ploeckelman created on 4/17/2016.
 */
public class LevelSelectScreen extends BaseScreen {

    public static final int LEVELS_PER_PAGE = 9;

    private final float MARGIN_TOP = 80f; // NOTE: change if title text scale changes
    private final float MARGIN_BOTTOM = 20f;
    private final float MARGIN_SIDE = 10f;

    int                numPages = 1;
    int                currentPage;

    Array<LevelButton> buttons;
    GlyphLayout        layout;
    BitmapFont         font;

    Rectangle buttonRegionBounds;
    float     buttonSize;
    float     buttonHeight;
    int       buttonsWide;

    LevelButton pagePrevBtn;
    LevelButton pageNextBtn;
    float timeoutTimer;

    public LevelSelectScreen(int levelToCenter) {
        timeoutTimer = 0;
        layout = new GlyphLayout();
        font = Assets.font_round_32;
        Utils.glClearColor(Config.bgColor);
        Gdx.input.setInputProcessor(this);
        currentPage = 1 + levelToCenter/LEVELS_PER_PAGE;

        generateLevelButtons();

        pagePrevBtn = new LevelButton(Integer.MIN_VALUE, MARGIN_SIDE, hudCamera.viewportHeight / 2f - buttonSize / 4f, buttonSize / 2f, buttonSize / 2f);
        pageNextBtn = new LevelButton(Integer.MAX_VALUE, hudCamera.viewportWidth - buttonSize / 2f - MARGIN_SIDE, hudCamera.viewportHeight / 2f - buttonSize / 4f, buttonSize / 2f, buttonSize / 2f);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            LudumDare35.game.screen = new MenuScreen();
        }
        timeoutTimer += dt;
        if (Gdx.input.justTouched()){
            timeoutTimer = 0;
        }
        if (timeoutTimer > LudumDare35.game.resolver.limitTimer()){
            LudumDare35.game.resetGame();
        }

        for (LevelButton button : buttons) {
            button.update(dt);
        }

        pagePrevBtn.update(dt);
        pageNextBtn.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float x, y;

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        batch.setShader(null);
        for (LevelButton button : buttons) {
            button.render(batch);
        }

        if (isPrevButtonVisible()) pagePrevBtn.render(batch);
        if (isNextButtonVisible()) pageNextBtn.render(batch);

        batch.setShader(Assets.fontShader);
        for (LevelButton button : buttons) {
            button.renderText(batch);
        }

        if (isPrevButtonVisible()) pagePrevBtn.renderText(batch);
        if (isNextButtonVisible()) pageNextBtn.renderText(batch);

        Assets.fontShader.setUniformf("u_scale",1.2f);
        font.getData().setScale(1.2f);
        font.setColor(Color.WHITE);

        layout.setText(font, "Levels");
        x = camera.viewportWidth / 2f - layout.width / 2f;
        y = camera.viewportHeight - layout.height;
        font.draw(batch, "Levels", x, y);

        batch.end();
        batch.setShader(null);
    }

    private boolean isPrevButtonVisible() {
        return (currentPage > 1);
    }

    private boolean isNextButtonVisible() {
        return (currentPage < numPages);
    }

    private void generateLevelButtons() {
        numPages = (int) Math.ceil(Level.values().length / (float) LEVELS_PER_PAGE);

        buttonsWide = 1;
        while (buttonsWide * buttonsWide < LEVELS_PER_PAGE) {
            buttonsWide++;
        }

        buttonHeight = hudCamera.viewportHeight - MARGIN_BOTTOM - MARGIN_TOP;
        buttonSize = Math.min(hudCamera.viewportWidth / buttonsWide, buttonHeight / buttonsWide);
        buttonRegionBounds = new Rectangle(
                hudCamera.viewportWidth / 2f - (buttonsWide * buttonSize) / 2f,
                MARGIN_BOTTOM,
                buttonsWide * buttonSize,
                buttonHeight);

        int levelIndex = (currentPage - 1) * LEVELS_PER_PAGE;

        buttons = new Array<LevelButton>();
        for (int y = buttonsWide - 1; y >= 0; --y) {
            for (int x = 0; x < buttonsWide; ++x) {
                if ( levelIndex >= Level.values().length) break;

                buttons.add(
                        new LevelButton(levelIndex,
                                buttonRegionBounds.x + x * buttonSize,
                                buttonRegionBounds.y + y * buttonSize,
                                buttonSize,
                                buttonSize));
                levelIndex++;
            }
        }
    }

    // ------------------------------------------------------------------------
    // InputListener Interface ------------------------------------------------
    // ------------------------------------------------------------------------
    private Vector2 touchPosScreen    = new Vector2();
    private Vector3 touchPosUnproject = new Vector3();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPosUnproject = hudCamera.unproject(new Vector3(screenX, screenY, 0));
        touchPosScreen.set(touchPosUnproject.x, touchPosUnproject.y);

        for (LevelButton levelButton : buttons) {
            if (levelButton.active && levelButton.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
                LudumDare35.game.screen = new GameScreen(levelButton.levelId);
            }
        }

        if (isPrevButtonVisible() && pagePrevBtn.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
            currentPage--;
            generateLevelButtons();
        }
        if (isNextButtonVisible() && pageNextBtn.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
            currentPage++;
            generateLevelButtons();
        }

        return false;
    }

}

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
import lando.systems.ld35.ui.Button;
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

    boolean showConfirmDlg;
    Rectangle resetConfirmDlgRect;
    Button confirmYesBtn;
    Button confirmNoBtn;
    Button resetProgressBtn;
    String confirmString1;
    String confirmString2;

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

        showConfirmDlg = false;
        confirmString1 = "Clear level progress";
        confirmString2 = "Are you sure?";

        float width = hudCamera.viewportWidth / 2f;
        float height = hudCamera.viewportHeight / 4f;
        resetConfirmDlgRect = new Rectangle(hudCamera.viewportWidth / 2f - width / 2f, hudCamera.viewportHeight / 2f - height / 2f, width, height);

        float buttonWidth = 100f;
        float buttonHeight = 32f;
        float buttonPadding = 10f;
        Rectangle confirmYesRect = new Rectangle(resetConfirmDlgRect.x + resetConfirmDlgRect.width / 2f - buttonWidth - buttonPadding,
                                                 resetConfirmDlgRect.y + buttonPadding, buttonWidth, buttonHeight);
        Rectangle confirmNoRect = new Rectangle(resetConfirmDlgRect.x + resetConfirmDlgRect.width / 2f + buttonPadding,
                                                resetConfirmDlgRect.y + buttonPadding, buttonWidth, buttonHeight);
        confirmYesBtn = new Button(Assets.levelResetButtonTexture, confirmYesRect, false);
        confirmNoBtn  = new Button(Assets.levelResetButtonTexture, confirmNoRect, false);

        Rectangle resetProgressBtnRect = new Rectangle(buttonPadding, buttonPadding, buttonWidth, buttonHeight);
        resetProgressBtn = new Button(Assets.levelResetButtonTexture, resetProgressBtnRect, false);
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

        resetProgressBtn.render(batch);

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

        // wtf, which scale is used?
        Assets.fontShader.setUniformf("u_scale", 0.7f);
        font.getData().setScale(0.7f);
        layout.setText(font, "Clear");
        font.draw(batch, "Clear",
                resetProgressBtn.bounds.x + resetProgressBtn.bounds.width / 2f - layout.width / 2f + 3f,
                resetProgressBtn.bounds.y + resetProgressBtn.bounds.height - 8f);

        batch.end();
        batch.setShader(null);

        if (!showConfirmDlg) return;
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            Assets.trayNinepatch.draw(batch, resetConfirmDlgRect.x, resetConfirmDlgRect.y, resetConfirmDlgRect.width, resetConfirmDlgRect.height);
            confirmYesBtn.render(batch);
            confirmNoBtn.render(batch);
        }
        batch.setShader(Assets.fontShader);
        {
            // draw main confirm dialog text
            Assets.fontShader.setUniformf("u_scale", 0.6f);
            font.getData().setScale(0.6f);
            layout.setText(font, confirmString1);
            font.draw(batch, confirmString1, resetConfirmDlgRect.x + resetConfirmDlgRect.width / 2f - layout.width / 2f, resetConfirmDlgRect.y + resetConfirmDlgRect.height - layout.height - 10f);
            layout.setText(font, confirmString2);
            font.draw(batch, confirmString2, resetConfirmDlgRect.x + resetConfirmDlgRect.width / 2f - layout.width / 2f, resetConfirmDlgRect.y + resetConfirmDlgRect.height - layout.height - 10f - 25f);

            // draw yes, no button text
            Assets.fontShader.setUniformf("u_scale", 0.7f);
            font.getData().setScale(0.7f);
            layout.setText(font, "Yes");
            font.draw(batch, "Yes",
                    confirmYesBtn.bounds.x + confirmYesBtn.bounds.width / 2f - layout.width / 2f + 3f,
                    confirmYesBtn.bounds.y + confirmYesBtn.bounds.height - 8f);
            layout.setText(font, "No");
            font.draw(batch, "No",
                    confirmNoBtn.bounds.x + confirmNoBtn.bounds.width / 2f - layout.width / 2f + 3f,
                    confirmNoBtn.bounds.y + confirmNoBtn.bounds.height - 8f);
        }
        batch.end();
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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchPosUnproject = hudCamera.unproject(new Vector3(screenX, screenY, 0));
        touchPosScreen.set(touchPosUnproject.x, touchPosUnproject.y);

        if (resetProgressBtn.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
            showConfirmDlg = !showConfirmDlg;
            return true;
        }
        if (showConfirmDlg && confirmNoBtn.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
            showConfirmDlg = false;
            return true;
        }
        if (showConfirmDlg && confirmYesBtn.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
            LudumDare35.game.resetGame();
            return true;
        }

        // Don't register button clicks if the confirm dialog is up
        if (showConfirmDlg) return false;

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

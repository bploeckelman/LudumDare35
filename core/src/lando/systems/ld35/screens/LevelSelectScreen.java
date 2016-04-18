package lando.systems.ld35.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
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

    int                numLevels;
    Array<LevelButton> buttons;
    GlyphLayout        layout;
    BitmapFont         font;

    public LevelSelectScreen() {
        numLevels = Level.values().length;
        layout = new GlyphLayout();
        font = Assets.font_round_32;
        Utils.glClearColor(Config.bgColor);
        Gdx.input.setInputProcessor(this);

        int buttonsWide = 1;
        while (buttonsWide * buttonsWide < numLevels) {
            buttonsWide++;
        }

        float marginTop = 80f; // Change if title text scale changes
        float marginBottom = 20f;
        float buttonWidth = Math.min(hudCamera.viewportWidth / buttonsWide,
                                     (hudCamera.viewportHeight - marginBottom - marginTop) / buttonsWide);
        final Rectangle buttonRegionBounds = new Rectangle(
                hudCamera.viewportWidth / 2f - (buttonsWide * buttonWidth) / 2f,
                marginBottom,
                buttonsWide * buttonWidth,
                hudCamera.viewportHeight - marginBottom - marginTop);

        int levelIndex = 0;
        buttons = new Array<LevelButton>();
        for (int y = buttonsWide - 1; y >= 0; --y) {
            for (int x = 0; x < buttonsWide; ++x) {
                if (levelIndex >= numLevels) break;

                final LevelButton levelButton = new LevelButton(levelIndex,
                        new Rectangle(buttonRegionBounds.x + x * buttonWidth,
                                      buttonRegionBounds.y + y * buttonWidth,
                                      buttonWidth,
                                      buttonWidth));
                buttons.add(levelButton);

                Tween.to(buttons.get(levelIndex).alpha, -1, 0.3f)
                        .target(1f)
                        .delay(levelIndex * 0.2f)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                levelButton.drawText = true;
                            }
                        })
                        .start(Assets.tween);

                levelIndex++;
            }
        }
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            LudumDare35.game.screen = new MenuScreen();
        }

        for (LevelButton button : buttons) {
            button.update(dt);
        }
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

        batch.setShader(Assets.fontShader);
        for (LevelButton button : buttons) {
            button.renderText(batch);
        }

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
            if (levelButton.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
                LudumDare35.game.screen = new GameScreen(levelButton.levelId);
            }
        }

        return false;
    }

}

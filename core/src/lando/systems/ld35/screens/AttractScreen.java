package lando.systems.ld35.screens;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.utils.Assets;
import lando.systems.ld35.utils.accessors.ColorAccessor;

/**
 * Created by Brian on 4/11/2017
 */
public class AttractScreen extends BaseScreen {

    private static final float TIMEOUT_SECONDS = 10;

    Color color = new Color(1f, 0f, 1f, 1f);
    float timer = 0f;

    public AttractScreen() {
        Tween.to(color, ColorAccessor.A, 1f)
                .target(0.1f)
                .repeatYoyo(-1, 0f)
                .start(Assets.tween);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.justTouched()) {
            LudumDare35.game.screen = new LevelSelectScreen();
        }

        timer += dt;
        if (timer > TIMEOUT_SECONDS) {
            LudumDare35.game.screen = new MenuScreen();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0f, 1f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(Assets.whitePixelTexture, 10, 10, camera.viewportWidth-20, camera.viewportHeight-20);
        Assets.drawString(batch, "Much attract", 100, camera.viewportHeight - 100, color, 1f);
        Assets.drawString(batch, "so allure", 200, camera.viewportHeight - 200, color, 1f);
        Assets.drawString(batch, "wow", 300, camera.viewportHeight - 300, color, 1f);

        Assets.drawString(batch, "" + (int) (timer + 1), 20, camera.viewportHeight - 30, Color.YELLOW, 1f);
        batch.end();
    }

}

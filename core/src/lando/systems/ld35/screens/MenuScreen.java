package lando.systems.ld35.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.utils.Assets;

/**
 * Brian Ploeckelman created on 1/17/2016.
 */
public class MenuScreen extends BaseScreen {

    public MenuScreen() {
        super();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

       if (Gdx.input.justTouched()){
           LudumDare35.game.screen = new GameScreen();
       }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0f, 1f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Assets.drawString(batch, "Balloon Mania", 200, 300, Color.WHITE, 1);
        batch.end();
    }

}

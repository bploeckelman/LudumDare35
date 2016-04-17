package lando.systems.ld35.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld35.utils.Config;

/**
 * Brian Ploeckelman created on 1/17/2016.
 */
public abstract class BaseScreen {

    public OrthographicCamera camera;
    public OrthographicCamera hudCamera;

    public BaseScreen() {
        float aspect = Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        camera = new OrthographicCamera(Config.gameWidth, Config.gameWidth / aspect);
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        hudCamera = new OrthographicCamera(Config.gameWidth, Config.gameWidth / aspect);
        hudCamera.translate(hudCamera.viewportWidth / 2, hudCamera.viewportHeight / 2, 0);
        hudCamera.update();
    }

    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);

}

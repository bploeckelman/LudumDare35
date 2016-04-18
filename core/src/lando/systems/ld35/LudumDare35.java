package lando.systems.ld35;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import lando.systems.ld35.screens.BaseScreen;
import lando.systems.ld35.screens.MenuScreen;
import lando.systems.ld35.utils.Assets;
import lando.systems.ld35.utils.SoundManager;

public class LudumDare35 extends ApplicationAdapter {

    public static LudumDare35 game;
    public static long startTime = 0l;

    public BaseScreen screen;

    @Override
    public void create() {
        Assets.load();
        SoundManager.load();
        float progress = 0f;
        do {
            progress = Assets.update();
        } while (progress != 1f);
        screen = new MenuScreen();
        game = this;
        startTime = TimeUtils.millis();
    }

    @Override
    public void resume() {
        Assets.load();
        SoundManager.load();
        game = this;
    }

    @Override
    public void dispose() {
        Assets.dispose();
        SoundManager.dispose();
    }

    @Override
    public void render() {
        float dt = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f);
        Assets.tween.update(dt);
        SoundManager.update(dt);
        screen.update(dt);
        screen.render(Assets.batch);
    }

}

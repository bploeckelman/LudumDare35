package lando.systems.ld35;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.TimeUtils;
import lando.systems.ld35.screens.BaseScreen;
import lando.systems.ld35.screens.MenuScreen;
import lando.systems.ld35.utils.Assets;
import lando.systems.ld35.utils.SoundManager;


public class LudumDare35 extends ApplicationAdapter {

    public static LudumDare35 game;
    public static long startTime = 0l;

    public BaseScreen screen;
    public ActionResolver resolver;

    public LudumDare35(ActionResolver actionResolver) {
        resolver = actionResolver;
    }

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

        if (!resolver.showMouseCursor()) {
            int x = 16;
            int y = 16;
            Pixmap pm = new Pixmap(x, y, Pixmap.Format.RGBA8888);
            Pixmap.setBlending(Pixmap.Blending.None);
            pm.setColor(0f, 0f, 0f, 0f);
            pm.fillRectangle(0, 0, x, y);
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        }

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

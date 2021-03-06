package lando.systems.ld35.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.utils.Assets;

/**
 * Brian Ploeckelman created on 1/17/2016.
 */
public class MenuScreen extends BaseScreen {

    public static final int ATTRACT_TIMEOUT_SECONDS = 10;

    GlyphLayout layout;
    String      clickText;
    String      title;
    Vector2     titlePos;
    MutableFloat alpha;
    Color        color;
    String[] names = new String[]{"Doug Graham", "Brian Ploeckelman", "Colin Kennedy", "Ian McNamara", "Brian Rossman", "Luke Bain", "Troy Sullivan"};
    float timer = 0f;

    public MenuScreen() {
        super();
        Gdx.input.setInputProcessor(null);
        title = "Shift 'n Drift";
        clickText = "Touch to begin!";
        Assets.font_round_32.getData().setScale(1.5f);
        layout = new GlyphLayout(Assets.font_round_32, title);
        titlePos = new Vector2(camera.viewportWidth / 2f - layout.width / 2f, camera.viewportHeight - layout.height);
        Assets.font_round_32.getData().setScale(1f);
        alpha = new MutableFloat(0.1f);
        Tween.to(alpha, -1, 0.5f)
                .target(1f)
                .repeatYoyo(-1, 0f)
                .start(Assets.tween);
        color = new Color(1f, 1f, 1f, alpha.floatValue());
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.justTouched()) {
            LudumDare35.game.screen = new LevelSelectScreen(0);
        }

        color.set(1f, 1f, 1f, alpha.floatValue());

        timer += dt;
        if (timer > LudumDare35.game.resolver.menuScreenTimer()) {
            LudumDare35.game.screen = new AttractScreen();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0f, 1f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(Assets.titleTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        float logoWidth = Assets.libgdxTexture.getRegionWidth()/ 2;
        float logoHeight=  Assets.libgdxTexture.getRegionHeight() /2;
        batch.draw(Assets.libgdxTexture, (camera.viewportWidth - logoWidth)/2, 10, logoWidth, logoHeight);
        Assets.drawString(batch, title, titlePos.x, titlePos.y, Color.RED, 1.5f);
        layout.setText(Assets.font_round_32, clickText);
        Assets.drawString(batch, clickText, camera.viewportWidth / 2f - layout.width / 2f, 380, color, 1f);

        Assets.font_round_32.getData().setScale(.7f);
        String madeBy = "Made with <3 by:";
        layout.setText(Assets.font_round_32, madeBy);
        Assets.drawString(batch, madeBy, camera.viewportWidth / 2f - layout.width / 2f, 330, Color.WHITE, .7f);
        float nameY = 300;
        for (String name : names){
            Assets.font_round_32.getData().setScale(.45f);
            layout.setText(Assets.font_round_32, name);
            Assets.drawString(batch, name, camera.viewportWidth / 2f - layout.width / 2f, nameY, Color.WHITE, .45f);
            nameY -= 25;
        }


        Assets.font_round_32.getData().setScale(0.5f);
        layout.setText(Assets.font_round_32, "originally made for ludum dare 35");
        Assets.drawString(batch, "originally made for ludum dare 35", camera.viewportWidth / 2f - layout.width / 2f, 20 + logoHeight + layout.height, Color.WHITE, 0.5f);

        Assets.drawString(batch, "" + (int) (LudumDare35.game.resolver.menuScreenTimer() - timer + 1), 10, camera.viewportHeight - 10, Color.YELLOW, 0.25f);
        batch.end();
    }

}

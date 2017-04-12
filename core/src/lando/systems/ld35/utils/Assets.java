package lando.systems.ld35.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.ParticleSystem.ParticleManager;
import lando.systems.ld35.utils.accessors.*;

/**
 * Brian Ploeckelman created on 9/10/2015.
 */
public class Assets {

    public static AssetManager mgr;

    public static TweenManager  tween;
    public static GlyphLayout   glyphLayout;
    public static SpriteBatch   batch;
    public static BitmapFont    font;
    public static BitmapFont    font_round_32;
    public static ShaderProgram fontShader;
    public static ShaderProgram fontNoShadowShader;

    public static TextureAtlas atlas;

    public static TextureRegion   whitePixelTexture;
    public static TextureRegion   whiteCircleTexture;
    public static TextureRegion   testTexture;
    public static TextureRegion   balloonTexture;
    public static TextureRegion   rocketTexture;
    public static TextureRegion   weightTexture;
    public static TextureRegion   torusTexture;
    public static TextureRegion   magnetTexture;
    public static TextureRegion   buzzsawTexture;
    public static TextureRegion   deadTexture;
    public static TextureRegion   spikesTexture;
    public static TextureRegion   moteTexture;
    public static TextureRegion   buttonTexture;
    public static TextureRegion   hotairTexture;
    public static TextureRegion   titleTexture;
    public static TextureRegion   mainMenuButtonTexture;
    public static TextureRegion   levelResetButtonTexture;
    public static TextureRegion   libgdxTexture;
    public static TextureRegion   touchTexture;
    public static TextureRegion[] cloudTextures;

    public static NinePatch transparentNinepatch;
    public static NinePatch selectedNinepatch;
    public static NinePatch trayNinepatch;

    public static Animation balloonToBalloonAnimation;
    public static Animation balloonToRocketAnimation;
    public static Animation balloonToWeightAnimation;
    public static Animation balloonToMagnetAnimation;
    public static Animation balloonToBuzzsawAnimation;
    public static Animation balloonToTorusAnimation;
    public static Animation balloonToPopAnimation;
    public static Animation buzzsawAnimation;

    public static Animation[] birdAnimations;

    public static Animation netAnimation;
    public static Animation fanAnimation;

    public static String prefsName;
    public static String prefMaxLevelCompleted;
    public static int maxLevelCompleted;


    public static boolean initialized;
    public static ParticleManager particles;


    public static void load() {
        if (tween == null) {
            tween = new TweenManager();
            Tween.setCombinedAttributesLimit(4);
            Tween.registerAccessor(Color.class, new ColorAccessor());
            Tween.registerAccessor(Rectangle.class, new RectangleAccessor());
            Tween.registerAccessor(Vector2.class, new Vector2Accessor());
            Tween.registerAccessor(Vector3.class, new Vector3Accessor());
            Tween.registerAccessor(OrthographicCamera.class, new CameraAccessor());
        }

        particles = new ParticleManager();

        prefsName = "shift-n-drift";
        prefMaxLevelCompleted = "maxLevelCompleted";

        glyphLayout = new GlyphLayout();
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(2f);
        font.getData().markupEnabled = true;

        final TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
        params.minFilter = Texture.TextureFilter.Linear;
        params.magFilter = Texture.TextureFilter.Linear;

        atlas = new TextureAtlas(Gdx.files.internal("sprites.atlas"));

        mgr = new AssetManager();
        mgr.load("title.png", Texture.class);

        Preferences prefs = Gdx.app.getPreferences(Assets.prefsName);
        if (prefs.getInteger(prefMaxLevelCompleted, -999) == -999) {
            prefs.putInteger(prefMaxLevelCompleted, -1);
        }
        prefs.flush();





        initialized = false;
    }

    public static float update() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1f;
        initialized = true;
        batch = new SpriteBatch();

        testTexture        = atlas.findRegion("badlogic");
        whitePixelTexture  = atlas.findRegion("white-pixel");
        whiteCircleTexture = atlas.findRegion("white-circle");
        balloonTexture     = atlas.findRegion("balloon");
        rocketTexture      = atlas.findRegion("rocket");
        weightTexture      = atlas.findRegion("weight");
        torusTexture       = atlas.findRegion("balloon_to_torus", 5);
        magnetTexture      = atlas.findRegion("balloon_to_magnet", 6);
        buzzsawTexture     = atlas.findRegion("balloon_to_buzzsaw", 5);
        deadTexture        = atlas.findRegion("balloon_pop", 3);
        spikesTexture      = atlas.findRegion("spikes");
        moteTexture        = atlas.findRegion("mote");
        buttonTexture      = atlas.findRegion("button_level");
        hotairTexture      = atlas.findRegion("hotair_balloon");
        titleTexture       = new TextureRegion(mgr.get("title.png", Texture.class));
        mainMenuButtonTexture = atlas.findRegion("button_level_select");
        levelResetButtonTexture = atlas.findRegion("button_level_reset");
        libgdxTexture      = atlas.findRegion("logo");
        touchTexture       = atlas.findRegion("touch");

        cloudTextures = new TextureRegion[3];
        cloudTextures[0] = atlas.findRegion("cloud1");
        cloudTextures[1] = atlas.findRegion("cloud2");
        cloudTextures[2] = atlas.findRegion("cloud3");

        balloonToBalloonAnimation = new Animation(.2f, balloonTexture, balloonTexture);
        balloonToRocketAnimation  = new Animation(.2f, atlas.findRegions("balloon_to_rocket"));
        balloonToWeightAnimation  = new Animation(.2f, atlas.findRegions("balloon_to_weight"));
        balloonToMagnetAnimation  = new Animation(.2f, atlas.findRegions("balloon_to_magnet"));
        balloonToBuzzsawAnimation = new Animation(.2f, atlas.findRegions("balloon_to_buzzsaw"));
        balloonToTorusAnimation   = new Animation(.2f, atlas.findRegions("balloon_to_torus"));
        balloonToPopAnimation     = new Animation(.1f, atlas.findRegions("balloon_pop"));
        buzzsawAnimation          = new Animation(.1f, atlas.findRegions("buzz_saw_rotate"));
        buzzsawAnimation.setPlayMode(Animation.PlayMode.LOOP);

        fanAnimation = new Animation(.1f, atlas.findRegions("fan"));
        fanAnimation.setPlayMode(Animation.PlayMode.LOOP);
        netAnimation = new Animation(.1f, atlas.findRegions("net"));
        netAnimation.setPlayMode(Animation.PlayMode.LOOP);

        birdAnimations = new Animation[3];
        birdAnimations[0] = new Animation(.02f, atlas.findRegions("birdgreen"));
        birdAnimations[0].setPlayMode(Animation.PlayMode.LOOP);
        birdAnimations[1] = new Animation(.02f, atlas.findRegions("birdorange"));
        birdAnimations[1].setPlayMode(Animation.PlayMode.LOOP);
        birdAnimations[2] = new Animation(.2f, atlas.findRegions("birdgull"));
        birdAnimations[2].setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        Texture distText = new Texture(Gdx.files.internal("fonts/simply_round_32.png"), true);
        distText.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);

        font_round_32 = new BitmapFont(Gdx.files.internal("fonts/simply_round_32.fnt"), new TextureRegion(distText), false);

        fontShader = new ShaderProgram(Gdx.files.internal("shaders/dist.vert"), Gdx.files.internal("shaders/dist.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }
        fontNoShadowShader = new ShaderProgram(Gdx.files.internal("shaders/dist.vert"), Gdx.files.internal("shaders/dist_no_shadow.frag"));

        transparentNinepatch = new NinePatch(atlas.findRegion("ninepatch"), 6, 6, 6, 6);
        selectedNinepatch    = new NinePatch(atlas.findRegion("ninepatch-select"), 6, 6, 6, 6);
        trayNinepatch        = new NinePatch(atlas.findRegion("ninepatch-tray"), 6, 6, 6, 6);

        return 1f;
    }

    public static void dispose() {
        atlas.dispose();
        batch.dispose();
        font.dispose();
        mgr.clear();
    }

    private static ShaderProgram compileShaderProgram(FileHandle vertSource, FileHandle fragSource) {
        ShaderProgram.pedantic = false;
        final ShaderProgram shader = new ShaderProgram(vertSource, fragSource);
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Failed to compile shader program:\n" + shader.getLog());
        }
        else if (shader.getLog().length() > 0) {
            Gdx.app.debug("SHADER", "ShaderProgram compilation log:\n" + shader.getLog());
        }
        return shader;
    }

    public static void drawString(SpriteBatch batch, String text, float x, float y, Color c, float scale){
        batch.setShader(Assets.fontShader);
        Assets.fontShader.setUniformf("u_scale", scale);
        font_round_32.getData().setScale(scale);
        font_round_32.setColor(c);
        font_round_32.draw(batch, text, x, y);
        font_round_32.getData().setScale(1f);
        batch.setShader(null);
    }

    public static int getMaxLevelCompleted() {
        if (LudumDare35.game.resolver.unlockAll()){
            return 59;
        }
        return Gdx.app.getPreferences(prefsName).getInteger(prefMaxLevelCompleted, 0);
    }

    public static void setMaxLevelCompleted(int levelIndex) {
        if (levelIndex < getMaxLevelCompleted()) return;
        Gdx.app.getPreferences(prefsName)
               .putInteger(prefMaxLevelCompleted, levelIndex)
               .flush();
    }

    public static void resetGameState(){
        Preferences prefs = Gdx.app.getPreferences(prefsName);
        prefs.putInteger(prefMaxLevelCompleted, 0);
        //TODO reset all game state
        prefs.flush();
    }
}

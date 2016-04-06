package lando.systems.ld35.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld35.utils.accessors.*;

/**
 * Brian Ploeckelman created on 9/10/2015.
 */
public class Assets {

    public static AssetManager mgr;

    public static TweenManager tween;
    public static GlyphLayout  glyphLayout;
    public static SpriteBatch  batch;
    public static BitmapFont   font;

    public static Texture whitePixelTexture;
    public static Texture whiteCircleTexture;
    public static Texture testTexture;

    public static boolean initialized;

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

        glyphLayout = new GlyphLayout();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(2f);
        font.getData().markupEnabled = true;

        final TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
        params.minFilter = Texture.TextureFilter.Linear;
        params.magFilter = Texture.TextureFilter.Linear;

        mgr = new AssetManager();
        mgr.load("badlogic.jpg",     Texture.class, params);
        mgr.load("white-pixel.png",  Texture.class, params);
        mgr.load("white-circle.png", Texture.class, params);

        initialized = false;
    }

    public static float update() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1f;
        initialized = true;

        testTexture        = mgr.get("badlogic.jpg",     Texture.class);
        whitePixelTexture  = mgr.get("white-pixel.png",  Texture.class);
        whiteCircleTexture = mgr.get("white-circle.png", Texture.class);

        return 1f;
    }

    public static void dispose() {
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

}

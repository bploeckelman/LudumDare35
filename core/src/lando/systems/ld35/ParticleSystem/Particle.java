package lando.systems.ld35.ParticleSystem;

/**
 * Created by Doug on 4/18/2016.
 */
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import lando.systems.ld35.gameobjects.Fan;
import lando.systems.ld35.gameobjects.LevelInfo;
import lando.systems.ld35.gameobjects.ObjectBase;
import lando.systems.ld35.utils.Assets;


public class Particle implements Pool.Poolable{

    Vector2 pos;
    Vector2 vel;
    Vector2 accel;
    Color initialColor;
    Color finalColor;
    float scale;
    float timeToLive;
    float totalTTL;
    TextureRegion texture;
    public static       float MAX_SPEED     = 200f;

    public Particle(){
        pos = new Vector2();
        vel = new Vector2();
        accel = new Vector2();
        initialColor = new Color();
        finalColor = new Color();
        scale = .1f;
    }

    @Override
    public void reset() {

        timeToLive = -1;
    }

    public void init(Vector2 p ,Vector2 v, Vector2 a, Color iC, Color fC, float s, float t) {
        init(p, v, a, iC, fC, s, t, new TextureRegion(Assets.moteTexture));

    }

    public void init(Vector2 p ,Vector2 v, Vector2 a, Color iC, Color fC, float s, float t, TextureRegion tex) {
        pos = p;
        vel = v;
        accel = a;
        initialColor = iC;
        finalColor = fC;
        scale = s;
        timeToLive = t;
        totalTTL = t;
        texture = tex;
    }

    public void update(float dt, LevelInfo level){
        timeToLive -= dt;
        for (ObjectBase obj : level.mapObjects){
            if (obj instanceof Fan){
                Fan f = (Fan) obj;
                Vector2 force = f.getWindForce(pos);
                if (!force.epsilonEquals( Vector2.Zero, 1f))
                    vel.add(force.add(MathUtils.random(10f) -5f, MathUtils.random(10f) -5f).scl(dt * 10));
            }
        }
        vel.add(accel.x * dt, accel.y * dt);
        vel.x = MathUtils.clamp(vel.x, -MAX_SPEED, MAX_SPEED);
        vel.y = MathUtils.clamp(vel.y, -MAX_SPEED, MAX_SPEED);
        pos.add(vel.x * dt, vel.y * dt);
    }

    public void render(SpriteBatch batch){
        batch.setColor(finalColor.cpy().lerp(initialColor, timeToLive / totalTTL));
        batch.draw(texture, pos.x, pos.y, scale, scale);
        batch.setColor(Color.WHITE);
    }
}
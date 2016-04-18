package lando.systems.ld35.ParticleSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import lando.systems.ld35.gameobjects.LevelInfo;

/**
 * Created by Doug on 4/18/2016.
 */
public class ParticleManager {

    private final Array<Particle> activeParticles = new Array<Particle>();

    private final Pool<Particle> particlePool = new Pool<Particle>() {
        @Override
        protected Particle newObject() {
            return new Particle();
        }
    };

    public ParticleManager(){

    }

    public void addExplosion(Vector2 pos, Vector2 vel){
        for (int i = 0; i < 200; i ++){
            Particle part = particlePool.obtain();
            float speed = MathUtils.random() * 50;
            float dir = MathUtils.random(360f);
            float x = pos.x + .3f + (MathUtils.random()/3f);
            part.init(new Vector2(x, pos.y), new Vector2(vel.x + MathUtils.sinDeg(dir) * speed, vel.y + MathUtils.cosDeg(dir) * speed ), new Vector2(0,-50f),
                    new Color(1,0,0,1), new Color(1,0,0,0), 2f, 4);
            activeParticles.add(part);
        }
    }

    public void update(float dt, LevelInfo level){
        int len = activeParticles.size;
        for (int i = len -1; i >= 0; i--){
            Particle part = activeParticles.get(i);
            part.update(dt, level);
            if (part.timeToLive <= 0){
                activeParticles.removeIndex(i);
                particlePool.free(part);
            }
        }
    }

    public void render(SpriteBatch batch){
        for (Particle part : activeParticles){
            part.render(batch);
        }
    }

    public void clear(){
        int len = activeParticles.size;
        for (int i = len -1; i >= 0; i--){
            Particle part = activeParticles.removeIndex(i);
            particlePool.free(part);
        }
    }
}

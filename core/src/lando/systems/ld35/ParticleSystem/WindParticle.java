package lando.systems.ld35.ParticleSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld35.utils.Assets;

/**
 * Created by Doug on 4/16/2016.
 */
public class WindParticle {
    public Vector2 pos;
    public Vector2 vel;
    public float TTL;

    public static       float MAX_SPEED     = 100f;
    Pool<Vector2> vector2Pool;
    Array<Vector2> trail;


    public WindParticle(Vector2 p){
        if (vector2Pool == null){
            vector2Pool = Pools.get(Vector2.class);
        }
        pos = p;
        vel = new Vector2();
        TTL = 2;
        trail = new Array<Vector2>();
    }

    public void update(float dt){
        trail.add(vector2Pool.obtain().set(pos.x, pos.y));
        pos.add(vel.x * dt, vel.y * dt);
        vel.scl(.99f);
        vel.x = MathUtils.clamp(vel.x, -MAX_SPEED, MAX_SPEED);
        vel.y = MathUtils.clamp(vel.y, -MAX_SPEED, MAX_SPEED);
        TTL -= dt;
    }

    public void render(SpriteBatch batch){
        float alpha = 0;
        for (Vector2 p : trail){
            batch.setColor(new Color(1,1,1, alpha * TTL/2f));
            batch.draw(Assets.moteTexture, p.x, p.y, 3, 3);
            alpha += .01f;
        }

        batch.setColor(Color.WHITE);
    }

    public void kill(){
        TTL = 0;
        vector2Pool.freeAll(trail);
        trail.clear();
    }
}

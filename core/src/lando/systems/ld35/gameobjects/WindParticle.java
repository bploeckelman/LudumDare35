package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Doug on 4/16/2016.
 */
public class WindParticle {
    public Vector2 pos;
    public Vector2 vel;

    public WindParticle(Vector2 p){
        pos = p;
        vel = new Vector2();
    }

    public void render(SpriteBatch batch){

    }
}

package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.utils.Assets;

/**
 * Created by dsgraham on 3/16/17.
 */
public class TouchAnimation {
    float animTime;
    private Vector2 pos;
    float growTime = .1f;
    float maxWidth = 30f;
    float totalTime = .5f;

    public TouchAnimation(){
        animTime = 0;
        pos = new Vector2();
    }

    public void setPoint(float x, float y){
        pos.set(x, y);
        animTime = 0;
    }

    public void update(float dt){
        animTime += dt;
    }

    public void render(SpriteBatch batch){
        if (animTime > totalTime) return; // Early Out

        float width = maxWidth;
        float alpha = 1;

        if (animTime < growTime){
            width = animTime * (1f/growTime) * maxWidth;
            alpha = animTime * (1f/growTime);
        } else {
            float dt = (animTime - growTime) / (totalTime - growTime);
            alpha = 1f - dt;
        }



        batch.setColor(1,1,1,alpha);
        batch.draw(Assets.touchTexture, pos.x - width/2f, pos.y - width /2f, width, width);
        batch.setColor(Color.WHITE);
    }
}

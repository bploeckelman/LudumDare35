package lando.systems.ld35.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Doug on 4/16/2016.
 */
public class WindField {
    public Vector2 origin;
    public Rectangle bounds;
    public Vector2 direction;

    public WindField( Vector2 o, Rectangle b, Vector2 d){
        origin = o;
        bounds = b;
        direction = d;
    }
}

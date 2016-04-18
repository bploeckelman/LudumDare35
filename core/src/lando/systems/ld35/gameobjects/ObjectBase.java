package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld35.utils.Assets;

public abstract class ObjectBase {
    static float scaleY = 1f;

    TextureRegion keyframe;
    Pixmap texturePixmap;
    Rectangle bounds;
    Rectangle intersectorRectangle;
    float scaleX = 1;
    float rotation = 0;
    float originX = 0;
    float originY = 0;
    public Rectangle realWorldBounds;
    public Vector2 center;



    public ObjectBase(Rectangle bounds, float rotation, boolean flipX) {
        this.keyframe = new TextureRegion(Assets.testTexture);
        this.bounds = bounds;
        this.rotation = rotation;
        realWorldBounds = new Rectangle(bounds);
        if(flipX) {
            scaleX = -1;
            originX = bounds.width / 2;
        }
        intersectorRectangle = new Rectangle();
        center = new Vector2();
        realWorldBounds.getCenter(center);
    }

    public abstract void update(float delta);

    public Rectangle collision(Balloon balloon) {
        if (texturePixmap != null && balloon.bounds.overlaps(bounds)) {
            if (Intersector.intersectRectangles(bounds, balloon.bounds, intersectorRectangle)){
                Rectangle textureArea = new Rectangle(intersectorRectangle.x - bounds.x + keyframe.getRegionX(),
                    intersectorRectangle.y - bounds.y + keyframe.getRegionY(),
                    intersectorRectangle.width, intersectorRectangle.height);

                int regionY = keyframe.getRegionY();
                // This may need to be <=
                for (int x = 0; x < textureArea.width; x++){
                    for (int y = 0; y <  textureArea.height; y++){
                        int texX = x + (int)textureArea.x;
                        int texY = 31 - (int)(y + intersectorRectangle.y - bounds.y) + regionY;
                        int pix = texturePixmap.getPixel(texX, texY);
                        int index = (int)( intersectorRectangle.x - bounds.x) + x + (int)(intersectorRectangle.y - bounds.y + y) * 32;
                        if((pix & 0xFF) != 0x00) {
                            return intersectorRectangle;
                        }
                    }
                }
            }
        }

        return null;
    }

    public boolean collisionMap(Balloon balloon, boolean[] intersectMap) {
        boolean collides = false;
        if (texturePixmap != null && balloon.bounds.overlaps(bounds)) {
            if (Intersector.intersectRectangles(bounds, balloon.bounds, intersectorRectangle)){
                Rectangle textureArea = new Rectangle(intersectorRectangle.x - bounds.x + keyframe.getRegionX(),
                        intersectorRectangle.y - bounds.y + keyframe.getRegionY(),
                        intersectorRectangle.width, intersectorRectangle.height);

                int regionY = keyframe.getRegionY();
                // This may need to be <=
                for (int x = 0; x < textureArea.width; x++){
                    for (int y = 0; y <  textureArea.height; y++){
                        int texX = x + (int)textureArea.x;
                        int texY = 31 - (int)(y + intersectorRectangle.y - bounds.y) + regionY;
                        int pix = texturePixmap.getPixel(texX, texY);
                        int index = (int)( intersectorRectangle.x - bounds.x) + x + (int)(intersectorRectangle.y - bounds.y + y) * 32;
                        if((pix & 0xFF) != 0x00) {
                            collides = true;
                            intersectMap[index] = true;
                        }
                    }
                }
            }
        }

        return collides;
    }

    public void render(SpriteBatch batch) {
        batch.draw(keyframe, bounds.x, bounds.y, originX, originY, bounds.width, bounds.height, scaleX, scaleY, rotation);
    }

    public Rectangle getBounds() { return bounds; }
    public TextureRegion getKeyframe() { return keyframe; }
    public Pixmap getPixmap() {
        Texture t = keyframe.getTexture();
        if (!t.getTextureData().isPrepared()) {
            t.getTextureData().prepare();
        }
        return t.getTextureData().consumePixmap();
    }
}

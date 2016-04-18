package lando.systems.ld35.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Rope extends ObjectBase {
    private boolean vertical;
    private String groupName;

    public Rope(Rectangle bounds, float rotation, boolean flipX, TextureRegion textureRegion, String groupName) {
        super(bounds, rotation, flipX);
        this.groupName = groupName;
        keyframe = textureRegion;
        texturePixmap = getPixmap();
    }

    @Override
    public void update(float delta) {

    }

    public String getGroupName() {
        return groupName;
    }
}

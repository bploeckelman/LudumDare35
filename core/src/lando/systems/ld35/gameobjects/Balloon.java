package lando.systems.ld35.gameobjects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.utils.Assets;
import lando.systems.ld35.utils.LevelBoundry;
import lando.systems.ld35.utils.SoundManager;
import lando.systems.ld35.utils.Statistics;

/**
 * Created by Doug on 4/16/2016.
 */
public class Balloon {
    public enum State {NORMAL, LIFT, HEAVY, SPINNER, MAGNET, BUZZSAW, POP, DEAD}

    public static ObjectMap<State, Animation> stateToAnimationMap;

    public static final float ANIM_DURATION = 0.5f;
    public static       float MAX_SPEED     = 100f;
    public static       float BOUNDS_MARGIN = 0;

    public Vector2       position;
    public Vector2       velocity;
    public State         currentState;
    public TextureRegion currentTexture;
    public boolean       animating;
    public MutableFloat  animationTimer;
    public Animation     currentAnimation;
    public Rectangle     bounds;
    public Rectangle     intersectorRectangle;
    public Pixmap        tilePixmap;
    public boolean[]     intersectMap;
    public Vector2       center;
    float rotation;
    Vector2 magnetForce;
    float accumulator;
    Pixmap              _collisionPixmap;
    public Texture      collisionTex;
    Vector2 massOfCollision;
    boolean collided;

    public Balloon(Vector2 position) {
        accumulator = 0;
        collided = false;
        this.center = new Vector2();
        this.currentState = State.NORMAL;
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.currentTexture = Assets.balloonTexture;
        this.animating = false;
        this.animationTimer = new MutableFloat(0);
        this.currentAnimation = Assets.balloonToBalloonAnimation;
        this.bounds = new Rectangle(position.x, position.y, 32 - (BOUNDS_MARGIN * 2f), 32 - (BOUNDS_MARGIN * 2f));
        this.intersectorRectangle = new Rectangle();
        this.intersectMap = new boolean[32 * 32];
        magnetForce = new Vector2();
        massOfCollision = new Vector2();
        bounds.x = position.x + BOUNDS_MARGIN;
        bounds.y = position.y + BOUNDS_MARGIN;
        bounds.getCenter(center);

        if (stateToAnimationMap == null) {
            stateToAnimationMap = new ObjectMap<State, Animation>();
            stateToAnimationMap.put(State.NORMAL, Assets.balloonToBalloonAnimation);
            stateToAnimationMap.put(State.LIFT, Assets.balloonToRocketAnimation);
            stateToAnimationMap.put(State.HEAVY, Assets.balloonToWeightAnimation);
            stateToAnimationMap.put(State.SPINNER, Assets.balloonToTorusAnimation);
            stateToAnimationMap.put(State.MAGNET,  Assets.balloonToMagnetAnimation);
            stateToAnimationMap.put(State.BUZZSAW, Assets.balloonToBuzzsawAnimation);
            stateToAnimationMap.put(State.POP, Assets.balloonToPopAnimation);
            stateToAnimationMap.put(State.DEAD, Assets.balloonToPopAnimation);
        }
        _collisionPixmap = new Pixmap(32, 32, Pixmap.Format.RGB888);
        collisionTex = new Texture(_collisionPixmap);
    }

    public void changeState(State state) {
        SoundManager.playBalloonSound(state);
        currentState = state;

        // Tween animation from 'previous' state to 'balloon'
        animating = true;
        animationTimer.setValue(currentAnimation.getAnimationDuration());
        Tween.to(animationTimer, -1, ANIM_DURATION / 2f)
                .target(0f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        // Tween animation from 'balloon' to new state
                        currentAnimation = stateToAnimationMap.get(currentState);
                        Tween.to(animationTimer, -1, ANIM_DURATION / 2f)
                             .target(currentAnimation.getAnimationDuration())
                             .setCallback(new TweenCallback() {
                                 @Override
                                 public void onEvent(int type, BaseTween<?> source) {
                                     animating = false;
                                     setTextureForCurrentState();
                                 }
                             })
                             .start(Assets.tween);
                    }
                })
                .start(Assets.tween);
    }

    public void kill(LevelInfo level) {
        if (currentState == State.POP || currentState == State.DEAD) return;
        currentState = State.POP;
        Statistics.endTime = TimeUtils.millis();
        Statistics.numDeaths++;
        SoundManager.playBalloonSound(currentState);
        currentAnimation = Assets.balloonToPopAnimation;
        Assets.particles.addExplosion(center, velocity);
        animating = true;
        Assets.tween.killTarget(animationTimer);
        animationTimer.setValue(0);
        Tween.to(animationTimer, -1, Assets.balloonToPopAnimation.getAnimationDuration())
            .target(Assets.balloonToPopAnimation.getAnimationDuration())
            .setCallback(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    animating = false;
                    currentState = State.DEAD;
                    setTextureForCurrentState();
                }
            })
            .start(Assets.tween);
    }

    Vector2 nextPos = new Vector2();
    public void update(float dt, LevelInfo levelInfo){
        accumulator+= dt;
        if (!collided) {
            switch (currentState) {
                case LIFT:
                    velocity.y += 100 * dt;
                    break;
                case HEAVY:
                    velocity.y -= 100 * dt;
                    break;
                case DEAD:
                    velocity.y -= 1000 * dt;
                    break;
            }
        }

        bounds.x = position.x + BOUNDS_MARGIN;
        bounds.y = position.y + BOUNDS_MARGIN;
        bounds.getCenter(center);


        magnetForce.set(0,0);

        // Interact with map objects
        for (ObjectBase obj : levelInfo.mapObjects) {
            // Interact with fans
            if (obj instanceof Fan && currentState != State.SPINNER) {
                Fan f = (Fan) obj;
                velocity.add(f.getWindForce(center).scl(dt));
            }


            // Interact with magnets
            if (obj instanceof ForceEntity && currentState == State.MAGNET){
                ForceEntity f = (ForceEntity)obj;
                magnetForce.add(f.getMagneticForce(center));
            }

        }

        if (currentState == State.MAGNET){
            velocity.add(magnetForce.scl(dt));
            rotation = (float)Math.toDegrees(Math.atan2(magnetForce.y, magnetForce.x))-90f;
        } else {
            rotation = 0;
        }

        velocity.x = MathUtils.clamp(velocity.x, -MAX_SPEED, MAX_SPEED);
        velocity.y = MathUtils.clamp(velocity.y, -MAX_SPEED, MAX_SPEED);

        nextPos.set(position.x, position.y).add(velocity.x * dt, velocity.y * dt);

        if (!collided) {
            float yFloat = MathUtils.sin(accumulator * 4f) * .4f;
            if (currentState != State.DEAD && currentState != State.SPINNER) velocity.y += yFloat;
        }

        if (currentState != State.SPINNER) velocity.scl(.99f);

        collided = false;
        massOfCollision.set(0,0);
        int tileX = (int)(nextPos.x / 32);
        int tileY = (int)(nextPos.y / 32);
        Array<LevelBoundry> cells = levelInfo.getTiles(tileX - 1, tileY - 1, tileX + 1, tileY + 1);
        bounds.x = nextPos.x + BOUNDS_MARGIN;
        bounds.y = nextPos.y + BOUNDS_MARGIN;
        if (cells.size > 0){

            for (LevelBoundry boundry: cells){
                if (Intersector.intersectRectangles(boundry.rect, bounds, intersectorRectangle)){
                    if (tilePixmap == null) {
                        Texture t = boundry.tile.getTile().getTextureRegion().getTexture();
                        if (!t.getTextureData().isPrepared()) {
                            t.getTextureData().prepare();
                        }
                        tilePixmap = t.getTextureData().consumePixmap();
                    }

                    Rectangle textureArea = new Rectangle(intersectorRectangle.x - boundry.rect.x + boundry.tile.getTile().getTextureRegion().getRegionX(),
                                                      intersectorRectangle.y - boundry.rect.y + boundry.tile.getTile().getTextureRegion().getRegionY(),
                                                      intersectorRectangle.width, intersectorRectangle.height);
                    Rectangle overlapRect = new Rectangle(intersectorRectangle.x - bounds.x, intersectorRectangle.y - bounds.y, intersectorRectangle.width, intersectorRectangle.height);
                    int regionY = boundry.tile.getTile().getTextureRegion().getRegionY();
                    // This may need to be <=
                    for (int x = 0; x < textureArea.width; x++){
                        for (int y = 0; y <  textureArea.height; y++){
                            int texX = x + (int)textureArea.x;
                            int texY = 31 - (int)(y + Math.abs(intersectorRectangle.y - boundry.rect.y)) + regionY;
                            int pix = tilePixmap.getPixel(texX, texY);
                            if (intersectorRectangle.x - bounds.x >= 32 || intersectorRectangle.x - bounds.x < 0) continue;
                            if (intersectorRectangle.y - bounds.y >= 32 || intersectorRectangle.y - bounds.y < 0) continue;
                            int index = (int)( intersectorRectangle.x - bounds.x) + x + (int)(intersectorRectangle.y - bounds.y + y) * 32;
                            if (index >= intersectMap.length) continue;
                            if ((pix & 0xFF) != 0x00) intersectMap[index] = true;
                        }
                    }
//                    Gdx.app.log("Collision", textureArea.toString());
                }

            }

        }

        // Collide with map Objects
        for (ObjectBase obj : levelInfo.mapObjects) {
            if(obj instanceof Spikes) {
                continue;
            }

            if (obj instanceof Rope) {
                if (currentState == State.BUZZSAW) {
                    continue;
                }

                obj.collisionMap(this, intersectMap);
                continue;
            }

            if (Intersector.intersectRectangles(obj.realWorldBounds, bounds, intersectorRectangle)){
                collided = true;
                if (intersectorRectangle.width > intersectorRectangle.height){
                    massOfCollision.add(Math.signum(velocity.x) * 30, Math.signum(velocity.y) * -300);
                } else {
                    massOfCollision.add(Math.signum(velocity.x) * -300, Math.signum(velocity.y) * 30);
                }
            }
        }
        boolean debug = LudumDare35.game.resolver.showDebug();
        if (debug) {
        _collisionPixmap.setColor(Color.BLACK);
        _collisionPixmap.fill();
        }
        for (int i = 0; i < intersectMap.length;i++){
            int cX = i %32;
            int cY = i /32;
            if (intersectMap[i]){
                collided = true;
                if (debug) {
                    _collisionPixmap.drawPixel(cX, cY, Color.RED.toIntBits());
                }
                int x = 16 - (i % 32);
                int y = 16 - (i / 32);
                if (x <= 0) x--;
                if (y <= 0) y--;
                massOfCollision.add(x, y);
            }
        }
        if (debug) {
            collisionTex = new Texture(_collisionPixmap);
        }
        if (collided){
            massOfCollision.nor();
//            massOfCollision.scl(-1);
            float dot = 2f * massOfCollision.dot(velocity);  // r = d - 2(d . n)n
            if (Math.abs(massOfCollision.angle(velocity)) > 90)
                velocity.sub(massOfCollision.x *dot, massOfCollision.y * dot);
            if (currentState == State.DEAD){
                velocity.scl(.8f);
            } else {
                velocity.scl(.9f);
            }
//            nextPos = position;
            if (currentState != State.DEAD)
                velocity.add(massOfCollision.x * 10, massOfCollision.y * 10);
        } else {
            position.set(nextPos);
        }

        for(int i =0; i < intersectMap.length; i++){
            intersectMap[i] = false;
        }




    }

    public void render(SpriteBatch batch){
        if (animating && currentAnimation != null) {
            batch.draw(currentAnimation.getKeyFrame(animationTimer.floatValue()), position.x, position.y, 32, 32);
        } else {
            if (currentState == State.BUZZSAW){
                batch.draw(Assets.buzzsawAnimation.getKeyFrame(accumulator), position.x, position.y, 16, 16, 32, 32, 1, 1, rotation);
//                batch.draw(Assets.whitePixelTexture, position.x, position.y, 16, 16, 32, 32, 1, 1, rotation);

            } else {
                batch.draw(currentTexture, position.x, position.y, 16, 16, 32, 32, 1, 1, rotation);
            }
        }
        if (LudumDare35.game.resolver.showDebug()) {
            Vector2 norm = velocity.cpy();
            norm.nor();
            for (int i = 0; i < velocity.len(); i++){
                batch.draw(Assets.whitePixelTexture, center.x + i * norm.x, center.y + i * norm.y, 1, 1);
            }
            batch.setColor(Color.GREEN);
            norm = massOfCollision.cpy();
            norm.nor();
            for (int i = 0; i < 30; i++){
                batch.draw(Assets.whitePixelTexture, center.x + i * norm.x, center.y + i * norm.y, 1, 1);
            }
            batch.setColor(Color.WHITE);
        }
    }

    // ------------------------------------------------------------------------
    // Private Implementation
    // ------------------------------------------------------------------------

    private void setTextureForCurrentState() {
        switch(currentState){
            case NORMAL:   currentTexture = Assets.balloonTexture; break;
            case LIFT:     currentTexture = Assets.rocketTexture; break;
            case HEAVY:    currentTexture = Assets.weightTexture; break;
            case SPINNER:  currentTexture = Assets.torusTexture; break;
            case MAGNET:   currentTexture = Assets.magnetTexture; break;
            case BUZZSAW:  currentTexture = Assets.buzzsawTexture; break;
            case POP:      currentTexture = Assets.deadTexture; break;
            case DEAD:     currentTexture = Assets.deadTexture; break;
            default:       currentTexture = Assets.testTexture; break;
        }
    }

}

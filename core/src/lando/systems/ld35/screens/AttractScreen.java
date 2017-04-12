package lando.systems.ld35.screens;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.ParticleSystem.WindParticle;
import lando.systems.ld35.backgroundobjects.Bird;
import lando.systems.ld35.backgroundobjects.Cloud;
import lando.systems.ld35.gameobjects.*;
import lando.systems.ld35.ui.StateButton;
import lando.systems.ld35.utils.*;
import lando.systems.ld35.utils.accessors.ColorAccessor;

/**
 * Created by Brian on 4/11/2017
 */
public class AttractScreen extends BaseScreen {

    private static final float TIMEOUT_SECONDS = 60;

    LevelInfo level;
    Balloon playerBalloon;

    Rectangle buttonTrayRect;
    Array<StateButton> stateButtons;

    Array<Cloud> clouds;
    Array<Bird> birds;

    Array<Vector2> windGrid;
    Array<WindParticle> dustMotes;
    boolean updateWindField;

    float actionTimer = 1f;

    int mapWidth;
    Vector2 tempVec2;

    Pool<Rectangle> rectPool;


    Color color = new Color(1f, 0f, 1f, 1f);
    float timer = 0f;

    public AttractScreen() {
        Tween.to(color, ColorAccessor.A, 1f)
                .target(0.1f)
                .repeatYoyo(-1, 0f)
                .start(Assets.tween);

        clouds = new Array<Cloud>();
        birds = new Array<Bird>();
        tempVec2 = new Vector2();

        updateWindField = true;
        dustMotes = new Array<WindParticle>();
        rectPool = Pools.get(Rectangle.class);

        loadLevel(MathUtils.random(1, Level.values().length - 2));
        updateWindGrid();
        updateCamera(0f, true);

        Utils.glClearColor(Config.bgColor);
        Assets.particles.clear();
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.justTouched()) {
            LudumDare35.game.resetGame();
        }

        timer += dt;
        if (timer > TIMEOUT_SECONDS) {
            LudumDare35.game.resetGame();
        }

        updateCamera(dt, false);
        updateDust(dt);
        Assets.particles.update(dt, level);
        updateBackgroundObjects(dt);
        level.update(dt);

        updateFakeInput(dt);

        playerBalloon.update(dt, level);

        updateMapObjects(dt);
        updateWinds();

        if (playerBalloon.currentState == Balloon.State.DEAD) {
            loadLevel(level.levelIndex);
        }
    }

    private void updateFakeInput(float dt) {
        actionTimer -= dt;
        if (actionTimer > 0f) {
            return;
        }

        // do a random action
        int action = MathUtils.random(0,5);
        switch (action) {
            case 0:
                if (playerBalloon.currentState != Balloon.State.NORMAL) {
                    playerBalloon.changeState(Balloon.State.NORMAL);
                    deactivateButtons();
                    stateButtons.get(0).active = true;
                }
                break;
            case 1:
                if (playerBalloon.currentState != Balloon.State.LIFT) {
                    playerBalloon.changeState(Balloon.State.LIFT);
                    deactivateButtons();
                    stateButtons.get(1).active = true;
                }
                break;
            case 2:
                if (playerBalloon.currentState != Balloon.State.HEAVY) {
                    playerBalloon.changeState(Balloon.State.HEAVY);
                    deactivateButtons();
                    stateButtons.get(2).active = true;
                }
                break;
            case 3:
                if (playerBalloon.currentState != Balloon.State.SPINNER) {
                    playerBalloon.changeState(Balloon.State.SPINNER);
                    deactivateButtons();
                    stateButtons.get(3).active = true;
                }
                break;
            case 4:
                if (playerBalloon.currentState != Balloon.State.MAGNET) {
                    playerBalloon.changeState(Balloon.State.MAGNET);
                    deactivateButtons();
                    stateButtons.get(4).active = true;
                }
                break;
            case 5:
                if (playerBalloon.currentState != Balloon.State.BUZZSAW) {
                    playerBalloon.changeState(Balloon.State.BUZZSAW);
                    deactivateButtons();
                    stateButtons.get(5).active = true;
                }
                break;
        }

        // reset to random time for next action
        actionTimer = MathUtils.random(2f, 5f);
    }

    private void deactivateButtons() {
        for (StateButton button : stateButtons) {
            button.active = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        level.setView(camera);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        for (Cloud c : clouds){
            c.render(batch, camera);
        }

        for (Bird b : birds){
            b.render(batch);
        }

//        if (hotairBalloon != null) hotairBalloon.render(batch);

        level.renderBackground();

        for (WindParticle mote : dustMotes){
            mote.render(batch);
        }

        Assets.particles.render(batch);

        level.renderForeground(batch);

        playerBalloon.render(batch);

        batch.setProjectionMatrix(hudCamera.combined);
        if (LudumDare35.game.resolver.showFPS()) {
            Assets.font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, hudCamera.viewportHeight);
        }

        if (LudumDare35.game.resolver.showDebug()) {
            batch.draw(playerBalloon.collisionTex, 0, hudCamera.viewportHeight - 80, 32, -32);
        }

        Assets.trayNinepatch.draw(batch, buttonTrayRect.x, buttonTrayRect.y, buttonTrayRect.width, buttonTrayRect.height);
        for (int i = 0; i < stateButtons.size; ++i) {
            StateButton stateButton = stateButtons.get(i);
            if (stateButton.enabled) {
                stateButton.render(batch);
            }
        }


//        Gdx.gl.glClearColor(0f, 1f, 0f, 1f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
//        batch.draw(Assets.whitePixelTexture, 10, 10, camera.viewportWidth-20, camera.viewportHeight-20);
//        Assets.drawString(batch, "Much attract", 100, camera.viewportHeight - 100, color, 1f);
//        Assets.drawString(batch, "so allure", 200, camera.viewportHeight - 200, color, 1f);
//        Assets.drawString(batch, "wow!", 300, camera.viewportHeight - 300, color, 1f);
//
        Assets.drawString(batch, "" + (int) (TIMEOUT_SECONDS - timer + 1), 10, camera.viewportHeight - 10, Color.YELLOW, 0.25f);
        batch.end();
    }


    // ----------------------------------------------------------------------------------


    private void loadLevel(int levelId){
        level = new LevelInfo(levelId, rectPool);
        playerBalloon = new Balloon(level.details.getStart());
        layoutUI();
    }

    private void layoutUI() {
        int numButtons = 6;
        float padding = 10f;
        float size = 32f;
        float width = (padding + size) * numButtons;
        float leftMargin = camera.viewportWidth / 2f - width / 2f;

        stateButtons = new Array<StateButton>();
        stateButtons.add(new StateButton(Balloon.State.NORMAL, Assets.balloonTexture,
                new Rectangle(leftMargin + 10 * 0f + 32 * 0f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.LIFT, Assets.rocketTexture,
                new Rectangle(leftMargin + 10 * 1f + 32 * 1f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.HEAVY, Assets.weightTexture,
                new Rectangle(leftMargin + 10 * 2f + 32 * 2f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.SPINNER, Assets.torusTexture,
                new Rectangle(leftMargin + 10 * 3f + 32 * 3f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.MAGNET, Assets.magnetTexture,
                new Rectangle(leftMargin + 10 * 4f + 32 * 4f, 10, 32, 32)));
        stateButtons.add(new StateButton(Balloon.State.BUZZSAW, Assets.buzzsawTexture,
                new Rectangle(leftMargin + 10 * 5f + 32 * 5f, 10, 32, 32)));
        stateButtons.get(0).active = true;

        for (int i = 0; i < stateButtons.size; ++i) {
            stateButtons.get(i).enabled = true;
        }

        buttonTrayRect = new Rectangle(leftMargin - 10f, 0, width + 10, 52);
    }

    private void updateWinds(){
        if (updateWindField){
            windGrid = null;
            updateWindField = false;
            for (int i = 0; i < level.mapObjects.size; i++ ){
                if (level.mapObjects.get(i) instanceof Fan){
                    Fan f = (Fan) level.mapObjects.get(i);
                    f.calcWindField();
                }
            }
            updateWindGrid();
        }
    }

    private void updateWindGrid(){
        int mapHeight = level.foregroundLayer.getHeight();

        if (windGrid == null) {
            mapWidth = level.foregroundLayer.getWidth();
            windGrid = new Array<Vector2>();
            for (int i = 0; i < mapHeight * mapWidth; i++) {
                windGrid.add(new Vector2());
            }
        }

        for (int i = 0; i < mapWidth * mapHeight; i++) {
            float x = 16 + (i % mapWidth * 32);
            float y = 16 + (i / mapWidth * 32);
            tempVec2.set(x, y);
            Vector2 pointV = windGrid.get(i);
            pointV.set(0,0);

            for (int j = 0; j < level.mapObjects.size; j++ ) {
                if (level.mapObjects.get(j) instanceof Fan) {
                    Fan f = (Fan) level.mapObjects.get(j);
                    pointV.add(f.getWindForce(tempVec2));
                }
            }
        }
    }

    private void updateCamera(float dt, boolean initial){
//        if (pauseGame) {
//            camera.update();
//            return;
//        }

        Vector2 targetCameraPosition = getCameraTarget();

        Vector2 dir = targetCameraPosition.cpy().sub(camera.position.x, camera.position.y);
        if (initial){
            camera.position.set(targetCameraPosition.x, targetCameraPosition.y, 0);
        } else {
            camera.position.add(dir.x * dt, dir.y * dt, 0);
        }
        camera.update();
    }

    Vector2 targetCameraPosition = new Vector2();
    private Vector2 getCameraTarget(){
        //TODO: make this not janky when the board is smaller than the screen.
        targetCameraPosition.set(playerBalloon.position);
        targetCameraPosition.x = MathUtils.clamp(targetCameraPosition.x, camera.viewportWidth/2f, level.foregroundLayer.getWidth()*32 -camera.viewportWidth/2f );
        targetCameraPosition.y = MathUtils.clamp(targetCameraPosition.y, Math.min(camera.viewportHeight/2f - buttonTrayRect.height, level.foregroundLayer.getHeight()*16), Math.max(level.foregroundLayer.getHeight()*32 -camera.viewportHeight/2f, level.foregroundLayer.getHeight()*16) );

        return targetCameraPosition;
    }


    private void updateDust(float dt){
        int newTotal = (int)(level.foregroundLayer.getWidth() * level.foregroundLayer.getHeight() * .1f);
        for (int i = 0; i < newTotal; i++){
            dustMotes.add(new WindParticle(new Vector2(MathUtils.random(level.foregroundLayer.getWidth()*32), MathUtils.random(level.foregroundLayer.getHeight()*32))));
        }

        for (int i = dustMotes.size -1; i >= 0; i--){
            WindParticle mote = dustMotes.get(i);
            int index = getWindMapIndex(mote.pos);
            if (index >= 0 && index < windGrid.size) {
                tempVec2.set(windGrid.get(index));
//            for (ObjectBase obj : level.mapObjects){
//                if (obj instanceof Fan){
//                    Fan f = (Fan) obj;
//                    Vector2 force = f.getWindForce(mote.pos);
//                    if (!force.epsilonEquals( Vector2.Zero, 1f))
//                        mote.vel.add(force.add(MathUtils.random(10f) -5f, MathUtils.random(10f) -5f).scl(dt * 10));
//                }
//            }
                if (!tempVec2.epsilonEquals(Vector2.Zero, 1f))
                    mote.vel.add(tempVec2.add(MathUtils.random(10f) - 5f, MathUtils.random(10f) - 5f).scl(dt * 10));
            }
            mote.update(dt);
            if (mote.TTL < 0 || mote.vel.len2() < 50) {
                dustMotes.removeIndex(i);
                continue;
            }
            if (level.getCell((int)mote.pos.x /32, (int)mote.pos.y / 32) != null){
                dustMotes.removeIndex(i);

            }
        }
    }

    private int getWindMapIndex(Vector2 pos){
        int worldX = (int)pos.x / 32;
        int worldY = (int)pos.y / 32;
        return worldX + worldY * mapWidth;
    }

    private void updateBackgroundObjects(float dt){
        for (int i = clouds.size -1; i >= 0; i--){
            Cloud c = clouds.get(i);
            c.update(dt, level);
            if (!c.alive){
                clouds.removeIndex(i);
            }
        }

        while (clouds.size < level.foregroundLayer.getHeight() / 2){
            clouds.add(new Cloud(new Vector2(level.foregroundLayer.getWidth() * 32 + MathUtils.random(200f), MathUtils.random(level.foregroundLayer.getHeight()*32))));
        }

        for (int i = birds.size -1; i >= 0; i--){
            Bird b = birds.get(i);
            b.update(dt, level);
            if (!b.alive){
                birds.removeIndex(i);
            }
        }

        if (MathUtils.randomBoolean(.0015f)){
            birds.add(new Bird(level));
        }

//        if (hotairBalloon != null){
//            hotairBalloon.update(dt, level);
//            if (!hotairBalloon.alive) hotairBalloon = null;
//        } else {
//            if (MathUtils.randomBoolean(.001f)){
//                hotairBalloon = new HotairBalloon(level);
//            }
//        }
    }

    private void updateMapObjects(float dt) {
        for (ObjectBase obj : level.mapObjects) {
            // Interact with level exit
            /*
            if (obj instanceof Exit && playerBalloon.currentState != Balloon.State.DEAD) {
                if (playerBalloon.bounds.overlaps(obj.getBounds())) {
//                    pauseGame = true;
                    playerBalloon.changeState(Balloon.State.NORMAL);
                    Tween.to(playerBalloon.position, Vector2Accessor.XY, 2f)
                            .target(obj.realWorldBounds.x, obj.realWorldBounds.y)
                            .ease(Elastic.OUT)
                            .start(Assets.tween);
                    Timeline.createSequence()
                            .push(Tween.to(camera, CameraAccessor.XYZ, 1.5f)
                                    .target(obj.center.x, obj.center.y, .1f)
                                    .ease(Quad.OUT))
                            .pushPause(.5f)
                            .push(Tween.call(new TweenCallback() {
                                @Override
                                public void onEvent(int i, BaseTween<?> baseTween) {
                                    dustMotes.clear();
//                                    timeoutDelay = 0;

                                    Assets.setMaxLevelCompleted(level.levelIndex + 1);
                                    level.nextLevel();
                                    updateWindField = true;
                                    Statistics.numLevelsCompleted = Assets.getMaxLevelCompleted();
                                    // TODO: check for game over
//                                    enableButtons();

                                    playerBalloon = new Balloon(level.details.getStart());
                                    for (StateButton button : stateButtons) {
                                        button.active = false;
                                    }
                                    stateButtons.get(0).active = true;
                                    camera.position.x = playerBalloon.center.x;
                                    camera.position.y = playerBalloon.center.y;
                                    Vector2 camtarget = getCameraTarget();
                                    Tween.to(camera, CameraAccessor.XYZ, 1f)
                                            .target(camtarget.x, camtarget.y, 1)
                                            .ease(Quad.IN)
                                            .setCallback(new TweenCallback() {
                                                @Override
                                                public void onEvent(int type, BaseTween<?> source) {
                                                    pauseGame = false;
                                                }
                                            })
                                            .start(Assets.tween);
                                }
                            }))
                            .start(Assets.tween);
                }
            }
            */

            if (obj instanceof Spikes) {
                if (obj.collision(playerBalloon) != null) {
                    playerBalloon.kill(level);

                }
            }
            if (obj instanceof Door){
                Door d = (Door) obj;
                if (d.updateWindField){
                    d.updateWindField = false;
                    updateWindField = true;
                }
            }
            if (obj instanceof Rope) {
                if (playerBalloon.currentState == Balloon.State.BUZZSAW && obj.collision(playerBalloon) != null) {
                    // TODO: Animate this?  Maybe some particle effects?
                    // Kill the rope!
                    SoundManager.playSound(SoundManager.SoundOptions.RopeSnap);
                    String ropeGroupName = ((Rope) obj).getGroupName();
                    Array<Rope> ropeGroup = level.ropeGroups.get(ropeGroupName);
                    if (ropeGroup != null) {
                        level.mapObjects.removeAll(ropeGroup, true);
                        Array<TriggerableEntity> objectsToTrigger = level.triggeredByRopeGroup.get(ropeGroupName);
                        for (TriggerableEntity triggerableEntity : objectsToTrigger) {
                            triggerableEntity.onTrigger();
                        }
                    }
                }
            }
            // TODO: interact with other stuff
        }
    }


}

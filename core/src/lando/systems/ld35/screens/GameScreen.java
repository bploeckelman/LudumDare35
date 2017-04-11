package lando.systems.ld35.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.TimeUtils;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.ParticleSystem.WindParticle;
import lando.systems.ld35.backgroundobjects.Bird;
import lando.systems.ld35.backgroundobjects.Cloud;
import lando.systems.ld35.backgroundobjects.HotairBalloon;
import lando.systems.ld35.gameobjects.*;
import lando.systems.ld35.ui.Button;
import lando.systems.ld35.ui.StateButton;
import lando.systems.ld35.utils.*;
import lando.systems.ld35.utils.accessors.CameraAccessor;
import lando.systems.ld35.utils.accessors.ColorAccessor;
import lando.systems.ld35.utils.accessors.Vector2Accessor;

/**
 * Brian Ploeckelman created on 4/16/2016.
 */
public class GameScreen extends BaseScreen {

    static float        TIMEOUTLIMIT = 10;
    static float        CONTINUETIME = 10;
    LevelInfo           level;
    String              levelName;
    Balloon             playerBalloon;
    Array<WindParticle> dustMotes;
    Array<Cloud>        clouds;
    Array<Bird>         birds;
    HotairBalloon       hotairBalloon;
    Array<StateButton>  stateButtons;
    Button              resetLevelButton;
    Button              mainMenuButton;
    Pool<Rectangle>     rectPool;
    Rectangle           buttonTrayRect;
    boolean             pauseGame;
    boolean             updateWindField;
    boolean             drawStats;
    Color               retryTextColor;
    Color               levelNameColor;
    Array<Vector2>      windGrid;
    int                 mapWidth;
    Vector2             tempVec2;
    TouchAnimation      touchPoint;
    float               timeoutDelay;
    boolean             showContinue;
    float               continueTimer;

    public GameScreen(int levelIndex) {
        super();
        showContinue = false;
        timeoutDelay = 0;
        tempVec2 = new Vector2();
        touchPoint = new TouchAnimation();
        pauseGame = false;
        updateWindField = true;
        drawStats = false;
        rectPool = Pools.get(Rectangle.class);
        dustMotes = new Array<WindParticle>();
        clouds = new Array<Cloud>();
        birds = new Array<Bird>();
        retryTextColor = new Color(Config.balloonColor);
        loadLevel(levelIndex);
        updateWindGrid();
        updateCamera(0f, true);

        Utils.glClearColor(Config.bgColor);
        Gdx.input.setInputProcessor(this);

        retryTextColor.a = 0.3f;
        Tween.to(retryTextColor, ColorAccessor.A, 1.0f).target(1f).repeatYoyo(-1, 0f).start(Assets.tween);

        Assets.particles.clear();

        levelName = Level.values()[levelIndex].name().replace("_", " ");
        levelNameColor = new Color(1f, 1f, 1f, 1f);
        Tween.to(levelNameColor, ColorAccessor.A, 3f).target(0f).start(Assets.tween);
    }

    // ------------------------------------------------------------------------
    // BaseScreen Implementation ----------------------------------------------
    // ------------------------------------------------------------------------

    @Override
    public void update(float dt) {
        timeoutDelay += dt;

        if (timeoutDelay > TIMEOUTLIMIT && !showContinue){
            setShowContinue();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            LudumDare35.game.screen = new LevelSelectScreen();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)){
            LudumDare35.game.resetGame();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pauseGame = !pauseGame;
        }
        if (Gdx.input.justTouched()){
            timeoutDelay = 0;
            showContinue = false;
        }

        touchPoint.update(dt);
        updateCamera(dt, false);
        updateDust(dt);
        Assets.particles.update(dt, level);
        updateBackgroundObjects(dt);
        level.update(dt);

        if (pauseGame || showContinue) { // Don't move the player or check for interactions
            return;
        }
        playerBalloon.update(dt, level);

        updateMapObjects(dt);
        updateWinds();
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

        if (hotairBalloon != null) hotairBalloon.render(batch);

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
        mainMenuButton.render(batch);
        resetLevelButton.render(batch);
        touchPoint.render(batch);
        batch.setShader(Assets.fontShader);
        // Draw button number if its the active state
//        Assets.fontShader.setUniformf("u_scale", 0.45f);
//        Assets.font_round_32.getData().setScale(.45f);
//        for (int i = 0; i < stateButtons.size; ++i) {
//            StateButton stateButton = stateButtons.get(i);
//            if (stateButton.enabled) {
//                Assets.font_round_32.setColor(stateButton.active ? Color.GREEN: Color.WHITE);
//                Assets.font_round_32.draw(batch, Integer.toString(i + 1),
//                                          stateButton.bounds.x + stateButton.bounds.width - 8f,
//                                          stateButton.bounds.y + stateButton.bounds.height - 3f);
//            }
//        }
        Assets.fontShader.setUniformf("u_scale", 0.75f);
        Assets.font_round_32.getData().setScale(0.6f);
        Assets.glyphLayout.setText(Assets.font_round_32, "Suicide");
        Assets.font_round_32.draw(batch, "Suicide",
                                  resetLevelButton.bounds.x + resetLevelButton.bounds.width / 2f - Assets.glyphLayout.width / 2f + 4f,
                                  resetLevelButton.bounds.y + resetLevelButton.bounds.height - 7f);

        // Draw Level Name
        Assets.fontShader.setUniformf("u_scale", 1f);
        Assets.font_round_32.getData().setScale(1f);
        Assets.glyphLayout.setText(Assets.font_round_32, levelName);
        Assets.font_round_32.setColor(levelNameColor);
        Assets.font_round_32.draw(batch, levelName,
                camera.viewportWidth / 2f - Assets.glyphLayout.width / 2f,
                camera.viewportHeight / 2f - Assets.glyphLayout.height / 2f + 50f);
        Assets.font_round_32.setColor(Color.WHITE);

        if (playerBalloon.currentState == Balloon.State.DEAD) {
            batch.setShader(null);
            batch.setColor(new Color(0,0,0,.6f));
            batch.draw(Assets.whitePixelTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
            batch.setColor(Color.WHITE);
            batch.setShader(Assets.fontShader);

            Assets.fontShader.setUniformf("u_scale", 1.5f);
            Assets.font_round_32.getData().setScale(1.5f);
            Assets.font_round_32.setColor(retryTextColor);
            Assets.glyphLayout.setText(Assets.font_round_32, "Touch to retry");
            Assets.font_round_32.draw(batch, "Touch to retry",
                                      camera.viewportWidth / 2f - Assets.glyphLayout.width / 2f,
                                      camera.viewportHeight - 40 - Assets.glyphLayout.height);
            Assets.font_round_32.setColor(1f, 1f, 1f, 1f);

            drawStats = true;
            drawStatisticsText(batch);
        }
        batch.end();
        batch.setShader(null);
    }

    // ------------------------------------------------------------------------
    // InputListener Interface ------------------------------------------------
    // ------------------------------------------------------------------------
    private Vector2 touchPosScreen    = new Vector2();
    private Vector3 touchPosUnproject = new Vector3();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPosUnproject = hudCamera.unproject(new Vector3(screenX, screenY, 0));
        touchPosScreen.set(touchPosUnproject.x, touchPosUnproject.y);
        touchPoint.setPoint(touchPosScreen.x, touchPosScreen.y);

        if ((playerBalloon.currentState == Balloon.State.POP ||
             playerBalloon.currentState == Balloon.State.DEAD) && drawStats) {
            resetLevel();
            drawStats = false;
            return false;
        }

        StateButton activatedButton = null;
        for (StateButton stateButton : stateButtons) {
            if (!stateButton.enabled) continue;
            if (stateButton.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
                playerBalloon.changeState(stateButton.state);
                activatedButton = stateButton;
                stateButton.active = true;
                Statistics.numShapeShifts++;
            }
        }
        if (activatedButton != null) {
            for (StateButton stateButton : stateButtons) {
                if (stateButton != activatedButton) {
                    stateButton.active = false;
                }
            }
        }

        if (mainMenuButton.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
            LudumDare35.game.screen = new LevelSelectScreen();
            return false;
        }

        if (resetLevelButton.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
            playerBalloon.kill(level);
            // TODO: move to 'game completed trigger'
            Statistics.endTime = TimeUtils.millis();
            Statistics.numResets++;
            return false;
        }

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        handleHotkeys(keycode);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
//        Vector3 worldPoint = camera.unproject(new Vector3(screenX, screenY, 0));
//        Array<LevelBoundry> cells = level.getTiles((int)worldPoint.x /32, (int)worldPoint.y / 32, (int)worldPoint.x /32, (int)worldPoint.y /32);
//        if (cells.size > 0){
//            for (LevelBoundry boundry: cells) {
//                Texture t = boundry.tile.getTile().getTextureRegion().getTexture();
//                if (!t.getTextureData().isPrepared()) {
//                    t.getTextureData().prepare();
//                }
//                Pixmap tilePixmap = t.getTextureData().consumePixmap();
//                int pxX = (int)(worldPoint.x % 32f) + boundry.tile.getTile().getTextureRegion().getRegionX();
//                int pxY = 32 - (int)(worldPoint.y % 32f) + boundry.tile.getTile().getTextureRegion().getRegionY();
//                Gdx.app.log("Touch", "X:" + pxX + " Y:" + pxY);
//
//                int pix = tilePixmap.getPixel(pxX,  pxY);
//                Gdx.app.log("Touch", pix+"");
//            }
//        }
        return false;
    }

    // ------------------------------------------------------------------------
    // Private Implementation -------------------------------------------------
    // ------------------------------------------------------------------------

    private void loadLevel(int levelId){
        level = new LevelInfo(levelId, rectPool);
        playerBalloon = new Balloon(level.details.getStart(), this);
        layoutUI();
    }

    private void resetLevel(){
        // TODO reset the level quickly
        loadLevel(level.levelIndex);
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
        enableButtons();

        buttonTrayRect = new Rectangle(leftMargin - 10f, 0, width + 10, 52);

        mainMenuButton = new Button(Assets.mainMenuButtonTexture, new Rectangle(padding, padding, 32, 32), false);
        resetLevelButton = new Button(Assets.levelResetButtonTexture, new Rectangle(camera.viewportWidth - 100 - padding, padding, 100, 32), false);
   }


   Vector2 targetCameraPosition = new Vector2();
    private Vector2 getCameraTarget(){
        //TODO: make this not janky when the board is smaller than the screen.
        targetCameraPosition.set(playerBalloon.position);
        targetCameraPosition.x = MathUtils.clamp(targetCameraPosition.x, camera.viewportWidth/2f, level.foregroundLayer.getWidth()*32 -camera.viewportWidth/2f );
        targetCameraPosition.y = MathUtils.clamp(targetCameraPosition.y, Math.min(camera.viewportHeight/2f - buttonTrayRect.height, level.foregroundLayer.getHeight()*16), Math.max(level.foregroundLayer.getHeight()*32 -camera.viewportHeight/2f, level.foregroundLayer.getHeight()*16) );

        return targetCameraPosition;
    }

    private void updateCamera(float dt, boolean initial){
        if (pauseGame) {
            camera.update();
            return;
        }

        Vector2 targetCameraPosition = getCameraTarget();

        Vector2 dir = targetCameraPosition.cpy().sub(camera.position.x, camera.position.y);
        if (initial){
            camera.position.set(targetCameraPosition.x, targetCameraPosition.y, 0);
        } else {
            camera.position.add(dir.x * dt, dir.y * dt, 0);
        }
        camera.update();
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

        if (hotairBalloon != null){
            hotairBalloon.update(dt, level);
            if (!hotairBalloon.alive) hotairBalloon = null;
        } else {
            if (MathUtils.randomBoolean(.001f)){
                hotairBalloon = new HotairBalloon(level);
            }
        }
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

    private void updateMapObjects(float dt) {
        for (ObjectBase obj : level.mapObjects) {
            // Interact with level exit
            if (obj instanceof Exit && playerBalloon.currentState != Balloon.State.DEAD) {
                if (playerBalloon.bounds.overlaps(obj.getBounds())) {
                    pauseGame = true;
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
                                    Assets.setMaxLevelCompleted(level.levelIndex);
                                    level.nextLevel();
                                    updateWindField = true;
                                    Statistics.numLevelsCompleted = Assets.getMaxLevelCompleted() + 1;
                                    // TODO: check for game over
                                    enableButtons();
                                    playerBalloon = new Balloon(level.details.getStart(), GameScreen.this);
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

    private void handleHotkeys(int keycode) {
        if (playerBalloon.currentState == Balloon.State.POP ||
            playerBalloon.currentState == Balloon.State.DEAD)
        {
            return;
        }

        switch (keycode) {
            case Input.Keys.NUM_1: if (stateButtons.get(0).enabled) activateButton(0); break;
            case Input.Keys.NUM_2: if (stateButtons.get(1).enabled) activateButton(1); break;
            case Input.Keys.NUM_3: if (stateButtons.get(2).enabled) activateButton(2); break;
            case Input.Keys.NUM_4: if (stateButtons.get(3).enabled) activateButton(3); break;
            case Input.Keys.NUM_5: if (stateButtons.get(4).enabled) activateButton(4); break;
            case Input.Keys.NUM_6: if (stateButtons.get(5).enabled) activateButton(5); break;
            default: break;
        }
    }

    private void activateButton(int index) {
        for (int i = 0; i < stateButtons.size; ++i) {
            final StateButton button = stateButtons.get(i);
            button.active = (index == i);
            if (button.active) {
                playerBalloon.changeState(button.state);
                Statistics.numShapeShifts++;
            }
        }
    }

    private void enableButtons() {
        for (int i = 0; i < stateButtons.size; ++i) {
            stateButtons.get(i).enabled = level.details.uiButtonStates[i];
        }
    }

    GlyphLayout layout;
    BitmapFont font;
    String[] statsText = new String[] {
              "Statistics:"
            , " shape shifts"
            , " levels completed"
            , " resets"
            , " deaths"
            , " minutes "
            , "Thanks for playing!"
    };

    private void updateWindGrid(){
        int mapHeight = level.foregroundLayer.getHeight();
        if (windGrid == null){
            mapWidth = level.foregroundLayer.getWidth();
            windGrid = new Array<Vector2>();
            for (int i = 0; i < mapHeight * mapWidth; i++){
                windGrid.add(new Vector2());
            }
        }
        for (int i = 0; i < mapWidth * mapHeight; i++){
            float x = 16 + (i % mapWidth * 32);
            float y = 16 + (i /mapWidth * 32);
            tempVec2.set(x, y);
            Vector2 pointV = windGrid.get(i);
            pointV.set(0,0);
            for (int j = 0; j < level.mapObjects.size; j++ ){
                if (level.mapObjects.get(j) instanceof Fan){
                    Fan f = (Fan) level.mapObjects.get(j);
                    pointV.add(f.getWindForce(tempVec2));
                }
            }
        }
    }

    private int getWindMapIndex(Vector2 pos){
        int worldX = (int)pos.x / 32;
        int worldY = (int)pos.y / 32;
        return worldX + worldY * mapWidth;
    }

    private void drawStatisticsText(SpriteBatch batch) {
        if (!drawStats) return;
        if (layout == null) layout = new GlyphLayout();
        if (font   == null) font = Assets.font_round_32;

        Statistics.numLevelsCompleted = Assets.getMaxLevelCompleted() + 1;



        int i = 0;
        float padding = 10f;
        float marginLeft = 100f;
        float marginTop = 130f;
        float lineY = 0f;
        for (String text : statsText) {
            // Special header shit
            if (i == 0) {
                Assets.fontShader.setUniformf("u_scale", 1.f);
                font.getData().setScale(1.f);
                font.setColor(Config.balloonColor);
                layout.setText(font, text);
                lineY = camera.viewportHeight - (marginTop + layout.height);
                font.draw(batch, text, marginLeft, lineY);
                lineY -= layout.height + padding;
            }
            // Special footer shit
            else if (i == statsText.length - 1) {
                Assets.fontShader.setUniformf("u_scale", 0.8f);
                font.getData().setScale(0.8f);
                font.setColor(Config.balloonColor);
                layout.setText(font, text);
                lineY -= layout.height + 2f * padding;
                font.draw(batch, text, marginLeft, lineY);
            }
            // Everything else
            else {
                Assets.fontShader.setUniformf("u_scale", 0.6f);
                font.getData().setScale(0.6f);
                font.setColor(1f, 1f, 1f, 1f);
                String fullText;
                switch (i) {
                    case 1: fullText = Integer.toString(Statistics.numShapeShifts) + text; break;
                    case 2: fullText = Integer.toString(Statistics.numLevelsCompleted) + text; break;
                    case 3: fullText = Integer.toString(Statistics.numResets) + text; break;
                    case 4: fullText = Integer.toString(Statistics.numDeaths) + text; break;
                    case 5: {
                        long millis = Statistics.endTime - LudumDare35.startTime;
                        long minutes = (millis / 1000l) / 60;
                        fullText = Long.toString(minutes) + text;
                    } break;
                    default: fullText = "0" + text;
                }
                layout.setText(font, fullText);
                lineY -= layout.height + padding;
                font.draw(batch, fullText, 1.5f * marginLeft, lineY);
            }

            ++i;
        }

        Assets.fontShader.setUniformf("u_scale", 1.0f);
        font.getData().setScale(1.0f);
        font.setColor(1f, 1f, 1f, 1f);
    }

    public void setShowContinue(){
        showContinue = true;
        continueTimer = 0;
    }

}

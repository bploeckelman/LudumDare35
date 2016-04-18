package lando.systems.ld35.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Sine;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import lando.systems.ld35.gameobjects.Balloon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SoundManager{

    public enum SoundOptions {
        Bounce,
        Deflate,
        Inflate,
        Laser1,
        Laser2,
        Laser3,
        Magnet,
        Pop,
        Rocket,
        Saw,
        Squeak,
        WeightDrop,
        RopeSnap
    }

    public enum MusicOptions {
    }

    private enum MusicPieces {

    }

    private static HashMap<SoundOptions, Sound> soundMap = new HashMap<SoundOptions, Sound>();
    private static HashMap<MusicPieces, Sound> musicMap = new HashMap<MusicPieces, Sound>();

    public static Music gameMusic;
    public static MutableFloat musicVolume;

    public static void load() {

        soundMap.put(SoundOptions.Bounce, Gdx.audio.newSound(Gdx.files.internal("sound/bounce.mp3")));
        soundMap.put(SoundOptions.Deflate, Gdx.audio.newSound(Gdx.files.internal("sound/deflate.mp3")));
        soundMap.put(SoundOptions.Inflate, Gdx.audio.newSound(Gdx.files.internal("sound/inflate.mp3")));
        soundMap.put(SoundOptions.Laser1, Gdx.audio.newSound(Gdx.files.internal("sound/lazer1.mp3")));
        soundMap.put(SoundOptions.Laser2, Gdx.audio.newSound(Gdx.files.internal("sound/lazer2.mp3")));
        soundMap.put(SoundOptions.Laser3, Gdx.audio.newSound(Gdx.files.internal("sound/lazer3.mp3")));
        soundMap.put(SoundOptions.Magnet, Gdx.audio.newSound(Gdx.files.internal("sound/magnet.mp3")));
        soundMap.put(SoundOptions.Pop, Gdx.audio.newSound(Gdx.files.internal("sound/pop.mp3")));
        soundMap.put(SoundOptions.Rocket, Gdx.audio.newSound(Gdx.files.internal("sound/rocket2.mp3")));
        soundMap.put(SoundOptions.Saw, Gdx.audio.newSound(Gdx.files.internal("sound/saw.mp3")));
        soundMap.put(SoundOptions.Squeak, Gdx.audio.newSound(Gdx.files.internal("sound/squeek.mp3")));
        soundMap.put(SoundOptions.WeightDrop, Gdx.audio.newSound(Gdx.files.internal("sound/weight_dropped.mp3")));
        soundMap.put(SoundOptions.RopeSnap, Gdx.audio.newSound(Gdx.files.internal("sound/rope_snap.mp3")));

        musicVolume = new MutableFloat(0);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/elevator.mp3"));
        gameMusic.setLooping(true);
        gameMusic.play();
        setMusicVolume(.3f);
    }


    public static void update(float dt){
        gameMusic.setVolume(musicVolume.floatValue());
    }
    // -----------------------------------------------------------------------------------------------------------------

    public static void dispose() {
        SoundOptions[] allSounds = SoundOptions.values();
        for (SoundOptions allSound : allSounds) {
            soundMap.get(allSound).dispose();
        }

        MusicPieces[] allMusicPieces = MusicPieces.values();
        for (MusicPieces musicPiece : allMusicPieces) {
            musicMap.get(musicPiece).dispose();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    //private static Random _rand = new Random();

    private static SoundOptions _currentBalloonSoundOption;
    public static void playBalloonSound(Balloon.State state) {
        SoundOptions balloonSound = getStateSoundOption(state);

        if (balloonSound != _currentBalloonSoundOption) {
            stopSound(_currentBalloonSoundOption);
        }

        _currentBalloonSoundOption = balloonSound;
        playSound(balloonSound);
    }

    private static SoundOptions getStateSoundOption(Balloon.State balloonState) {
        SoundOptions balloonSound;

        switch (balloonState) {
            case LIFT:
                balloonSound = SoundOptions.Deflate;
                break;
            case HEAVY:
                balloonSound = SoundOptions.WeightDrop;
                break;
            case SPINNER:
                balloonSound = SoundOptions.Inflate;
                break;
            case MAGNET:
                balloonSound = SoundOptions.Magnet;
                break;
            case POP:
                balloonSound = SoundOptions.Pop;
                break;
            case BUZZSAW:
                balloonSound = SoundOptions.Saw;
                break;
            default:
                balloonSound = SoundOptions.Squeak;
                break;
        }

        return balloonSound;
    }

    public static long playSound(SoundOptions soundOption) {
        //Gdx.app.log("DEBUG", "SoundManager.playSound | soundOption='" + String.valueOf(soundOption) + "'");
        return soundMap.get(soundOption).play();
    }

    public static void stopSound(SoundOptions soundOption) {
        Sound sound = soundMap.get(soundOption);
        if (sound != null) {
            sound.stop();
        }
    }

    private static MusicOptions currentOption;
    private static long currentLoopID;
    private static Sound currentLoopSound;

    public static void setMusicVolume(float level){
        Assets.tween.killTarget(musicVolume);
        Tween.to(musicVolume, 1, 4f)
                .target(level)
                .ease(Sine.IN)
                .start(Assets.tween);
    }

    public static void playMusic(MusicOptions musicOption) {

        /*
//        Gdx.app.log("DEBUG", "SoundManager.playMusic | musicOption='" + String.valueOf(musicOption) + "'");

        currentOption = musicOption;
        // Kill any currently play loop
        if (currentLoopSound != null) {
            currentLoopSound.stop(currentLoopID);
        }

        switch (musicOption) {

            case DNUORGREDNU:
                currentLoopSound = musicMap.get(MusicPieces.DNUORGREDNU);
                currentLoopID = currentLoopSound.loop();
                break;

            case MARIO_MAJOR:
                musicMap.get(MusicPieces.MARIO_MAJOR_INTRO).play();
                Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        // Are we still in this case?
                        if (currentOption == MusicOptions.MARIO_MAJOR) {
                            currentLoopID = musicMap.get(MusicPieces.MARIO_MAJOR_LOOP).loop();
                            currentLoopSound = musicMap.get(MusicPieces.MARIO_MAJOR_LOOP);
                        }
                    }
                })
                        .delay(2.6f)
                        .start(LudumDare33.tween);
                break;

            case MARIO_MAJOR_BK:
                currentLoopSound = musicMap.get(MusicPieces.MARIO_MAJOR_LOOP_BK);
                currentLoopID = currentLoopSound.loop();
                break;

            case MARIO_MINOR:
                musicMap.get(MusicPieces.MARIO_MINOR_INTRO).play();
                Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        // Are we still in this case?
                        if (currentOption == MusicOptions.MARIO_MINOR) {
                            currentLoopID = musicMap.get(MusicPieces.MARIO_MINOR_LOOP).loop();
                            currentLoopSound = musicMap.get(MusicPieces.MARIO_MINOR_LOOP);
                        }
                    }
                })
                        .delay(3.2f)
                        .start(LudumDare33.tween);
                break;

            case METRIOD_BK:
                currentLoopSound = musicMap.get(MusicPieces.METROID_LOOP_BK);
                currentLoopID = currentLoopSound.loop();
                break;

            case ZELDA_BK:
                currentLoopSound = musicMap.get(MusicPieces.ZELDA_MYSTERIOUS_LOOP_BK);
                currentLoopID = currentLoopSound.loop();
                break;

            default:
//                Gdx.app.log("ERROR", "SoundManager.playMusic | Unrecognized music option.");
*/
    }
}

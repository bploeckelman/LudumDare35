package lando.systems.ld34.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Author: Ian McNamara <ian.mcnamara@wisc.edu>
 * Teaching and Research Application Development
 * Copyright 2015 Board of Regents of the University of Wisconsin System
 */
public class SoundManager{

    public enum SoundOptions {
        NECK_CRACK
    }



    public enum MusicOptions {
    }

    private enum MusicPieces {

    }

    private static HashMap<SoundOptions, Sound> soundMap = new HashMap<SoundOptions, Sound>();
    private static ArrayList<Sound> whipList = new ArrayList<Sound>();
    private static ArrayList<Sound> screamList = new ArrayList<Sound>();
    private static HashMap<MusicPieces, Sound> musicMap = new HashMap<MusicPieces, Sound>();

    public static void load() {
        whipList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/crack1.mp3")));
        whipList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/crack2.mp3")));
        whipList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/crack3.mp3")));
        whipList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/crack4.mp3")));
        whipList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/crack5.mp3")));
        screamList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/scream1.mp3")));
        screamList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/scream2.mp3")));
        screamList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/scream3.mp3")));
        screamList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/scream4.mp3")));
        screamList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/scream5.mp3")));
        screamList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/scream6.mp3")));
        screamList.add(Gdx.audio.newSound(Gdx.files.internal("sounds/scream7.mp3")));
        soundMap.put(SoundOptions.NECK_CRACK, Gdx.audio.newSound(Gdx.files.internal("sounds/neckcrack.mp3")));
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static void dispose() {
        SoundOptions[] allSounds = SoundOptions.values();
        for (SoundOptions allSound : allSounds) {
            soundMap.get(allSound).dispose();
        }

        for (Sound whipSound : whipList) {
            whipSound.dispose();
        }

        for (Sound screamSound : screamList) {
            screamSound.dispose();
        }

        MusicPieces[] allMusicPieces = MusicPieces.values();
        for (MusicPieces musicPiece : allMusicPieces) {
            musicMap.get(musicPiece).dispose();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Random _rand = new Random();


    public static void playWhip() {
        playRandom(whipList);
    }

    public static void playScream() {
        playRandom(screamList);
    }

    private static void playRandom(ArrayList<Sound> soundList) {
        int value = _rand.nextInt(soundList.size());
        soundList.get(value).play();
    }

    public static void playSound(SoundOptions soundOption) {
        //Gdx.app.log("DEBUG", "SoundManager.playSound | soundOption='" + String.valueOf(soundOption) + "'");
        soundMap.get(soundOption).play();
    }

    private static MusicOptions currentOption;
    private static long currentLoopID;
    private static Sound currentLoopSound;

    public static void setMusicVolume(float level){
        if (currentLoopSound != null){
            currentLoopSound.setVolume(currentLoopID, level);
        }
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

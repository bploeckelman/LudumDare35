package lando.systems.ld35.desktop;

import lando.systems.ld35.ActionResolver;

/**
 * Created by dsgraham on 3/9/17.
 */
public class DesktopActionResolver implements ActionResolver {
    public boolean fullScreen = true;
    public boolean freePlay = false;
    public boolean showMouseCursor = false;
    public int livesPerCredit = 5;
    public int continuesPerCredit = 3;
    public boolean showFPS = false;
    public boolean playMusic = true;
    public boolean showDebug = false;

    public DesktopActionResolver(){
    }

    @Override
    public boolean isFullScreen() {
        return fullScreen;
    }

    @Override
    public boolean isFreePlay() {
        return freePlay;
    }

    @Override
    public int livesPerCredit() {
        return livesPerCredit;
    }

    @Override
    public int continuesPerCredit() {
        return continuesPerCredit;
    }

    @Override
    public boolean showFPS() {
        return showFPS;
    }

    @Override
    public boolean showDebug(){
        return showDebug;
    }

    @Override
    public boolean showMouseCursor() {
        return showMouseCursor;
    }

    @Override
    public boolean playMusic() {
       return playMusic;
    }
}

package lando.systems.ld35;

/**
 * Created by dsgraham on 3/9/17.
 */
public interface ActionResolver {
    boolean isFullScreen();
    boolean showMouseCursor();
    boolean isFreePlay();
    int livesPerCredit();
    int continuesPerCredit();
    int menuScreenTimer();
    int attractScreenTimer();
    int limitTimer();
    int warningTimer();
    int continueTimer();
    boolean showFPS();
    boolean playMusic();
    boolean showDebug();
    boolean unlockAll();
}

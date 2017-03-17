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
    boolean showFPS();
    boolean playMusic();
}

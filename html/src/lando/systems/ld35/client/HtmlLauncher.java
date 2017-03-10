package lando.systems.ld35.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import lando.systems.ld35.ActionResolver;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.utils.Config;

public class HtmlLauncher extends GwtApplication implements ActionResolver{

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Config.gameWidth, Config.gameHeight);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new LudumDare35(this);
        }

        @Override
        public boolean isFullScreen() {
                return false;
        }

        @Override
        public boolean isFreePlay() {
                return true;
        }

        @Override
        public int livesPerCredit() {
                return 0;
        }

        @Override
        public int continuesPerCredit() {
                return 0;
        }

        @Override
        public boolean showFPS() {
                return false;
        }

        @Override
        public boolean showMouseCursor() {
                return false;
        }
}
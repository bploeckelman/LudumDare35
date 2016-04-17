package lando.systems.ld35.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import lando.systems.ld35.LudumDare35;
import lando.systems.ld35.utils.Config;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Config.gameWidth, Config.gameHeight);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new LudumDare35();
        }
}
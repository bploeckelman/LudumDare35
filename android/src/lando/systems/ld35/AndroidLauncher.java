package lando.systems.ld35;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication implements ActionResolver{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new LudumDare35(this), config);
	}

	@Override
	public boolean isFullScreen() {
		return false;
	}

	@Override
	public boolean showMouseCursor() {
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
}

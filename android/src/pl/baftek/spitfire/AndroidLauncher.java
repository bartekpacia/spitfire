package pl.baftek.spitfire;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.golfgl.gdxgamesvcs.GpgsClient;
import pl.baftek.spitfire.game.SpitfireGame;

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GpgsClient playGamesClient = new GpgsClient().initialize(this, false);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        SpitfireGame game = new SpitfireGame(playGamesClient);
        config.useImmersiveMode = true;
        initialize(game, config);
    }
}
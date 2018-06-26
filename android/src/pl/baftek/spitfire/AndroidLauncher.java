package pl.baftek.spitfire;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.golfgl.gdxgamesvcs.GpgsClient;
import pl.baftek.spitfire.game.SpitfireGame;

public class AndroidLauncher extends AndroidApplication
{
    private GpgsClient playGamesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        playGamesClient = new GpgsClient().initialize(this, false);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        SpitfireGame game = new SpitfireGame(playGamesClient);
        config.useImmersiveMode = true;
        initialize(game, config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (playGamesClient != null)
        {
            playGamesClient.onGpgsActivityResult(requestCode, resultCode, data);
        }
    }
}

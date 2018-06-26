package pl.baftek.spitfire.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.golfgl.gdxgamesvcs.NoGameServiceClient;
import pl.baftek.spitfire.game.SpitfireGame;

public class DesktopLauncher
{
	public static void main (String[] arg)
    {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = SpitfireGame.TITLE;
		config.width = SpitfireGame.WIDTH;
        config.height = SpitfireGame.HEIGHT;
        config.resizable = false;

		new LwjglApplication(new SpitfireGame(new NoGameServiceClient()), config);
	}
}
package pl.baftek.spitfire.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;
import pl.baftek.spitfire.game.SpitfireGame;

public class Lwjgl3Launcher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle(SpitfireGame.TITLE);
        config.setWindowSizeLimits(
                SpitfireGame.WIDTH,
                SpitfireGame.HEIGHT,
                SpitfireGame.WIDTH,
                SpitfireGame.HEIGHT
        );

        config.setResizable(false);
        new Lwjgl3Application(new SpitfireGame(new NoGameServiceClient()), config);
    }
}

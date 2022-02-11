package pl.baftek.spitfire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import de.golfgl.gdxgamesvcs.GameCenterClient;
import pl.baftek.spitfire.game.SpitfireGame;

public class IOSLauncher extends IOSApplication.Delegate {
    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();

        SpitfireGame game = new SpitfireGame() {
            @Override
            public void create() {
                gameServiceClient = new GameCenterClient(((IOSApplication) Gdx.app).getUIViewController());
                super.create();
            }
        };

        return new IOSApplication(game, config);
    }
}
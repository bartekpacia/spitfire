package pl.baftek.spitfire.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pl.baftek.spitfire.enums.GameState;
import pl.baftek.spitfire.game.SpitfireGame;

public abstract class AbstractScreen implements Screen {
    private static final String TAG = "AbstractScreen";
    protected SpitfireGame game;
    protected Stage stage;
    private OrthographicCamera camera;
    protected SpriteBatch spriteBatch;

    public static final float FONT_SIZE_0 = 0.2f;
    public static final float FONT_SIZE_1 = 0.3f;
    public static final float FONT_SIZE_2 = 0.35f;
    public static final float FONT_SIZE_3 = 0.45f;
    public static final float FONT_SIZE_4 = 0.6f;
    public static final float FONT_SIZE_5 = 0.75f;
    public static final float FONT_SIZE_6 = 0.9f;
    public static final float FONT_SIZE_7 = 1.5f;

    public AbstractScreen(SpitfireGame game) {
        this.game = game;

        createCamera();

        stage = new Stage(new StretchViewport(SpitfireGame.WIDTH, SpitfireGame.HEIGHT, camera));
        Gdx.input.setInputProcessor(stage);
        spriteBatch = new SpriteBatch();

        init();
        buildUI();
    }

    /**
     * This is where assets and everything non-layout related should be initialized.
     */
    protected abstract void init();

    /**
     * This is where UI should be initialized. It gets invoked just after {@link #init()} ()}.
     */
    protected abstract void buildUI();

    private void createCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SpitfireGame.WIDTH, SpitfireGame.HEIGHT);
        camera.update();
    }

    @Override
    public void render(float delta) {
        clearScreen();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        stage.act(delta);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resume() {
        Gdx.app.log(TAG, "resume()");
    }

    @Override
    public void pause() {
        Gdx.app.log(TAG, "pause()");

        if (SpitfireGame.getGameState() == GameState.RUN) {
            SpitfireGame.setGameState(GameState.PAUSE);
        }
    }

    @Override
    public void dispose() {
        game.dispose();
        spriteBatch.dispose();
        stage.dispose();
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }
}

package pl.baftek.spitfire.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.game.StringHelper;
import pl.baftek.spitfire.enums.GameState;

public abstract class AbstractScreen implements Screen
{
    private static final String TAG = "AbstractScreen";
    protected SpitfireGame game;
    protected Stage stage;
    private OrthographicCamera camera;
    protected SpriteBatch spriteBatch;

    public static Skin skin;
    protected TextButtonStyle whiteButtonStyle;
    protected TextButtonStyle redButtonStyle;
    protected TextButtonStyle orangeButtonStyle;
    protected TextButtonStyle greenButtonStyle;

    public static LabelStyle whiteLabelStyle;
    protected LabelStyle redLabelStyle;
    protected LabelStyle blueLabelStyle;
    protected LabelStyle orangeLabelStyle;
    protected LabelStyle yellowLabelStyle;
    protected LabelStyle greenLabelStyle;

    public static final float picoFontSize = 0.2f;
    public static final float ultraSmallFontSize = 0.3f;
    public static final float verySmallFontSize = 0.35f;
    public static final float smallFontSize = 0.45f;
    public static final float mediumFontSize = 0.6f;
    public static final float normalFontSize = 0.75f;
    public static final float bigFontSize = 0.9f;
    public static final float hugeFontSize = 1.5f;

    public AbstractScreen(SpitfireGame game)
    {
        this.game = game;

        createCamera();
        createStyles();

        stage = new Stage(new StretchViewport(SpitfireGame.WIDTH, SpitfireGame.HEIGHT, camera));
        Gdx.input.setInputProcessor(stage);
        spriteBatch = new SpriteBatch();

        init();
        buildUI();
    }

    /**
     * This is where assets and everything non-layout relatedshould be initialized.
     */
    protected abstract void init();

    /**
     * This is where UI should be initialized. It gets invoked just after {@link #init()} ()}.
     */
    protected abstract void buildUI();

    private void createStyles()
    {
        skin = new Skin(Gdx.files.internal(StringHelper.UI_SKIN_PATH));

        whiteButtonStyle = new TextButtonStyle();
        redButtonStyle = new TextButtonStyle();
        orangeButtonStyle = new TextButtonStyle();
        greenButtonStyle = new TextButtonStyle();

        whiteLabelStyle = new LabelStyle();
        redLabelStyle = new LabelStyle();
        blueLabelStyle = new LabelStyle();
        orangeLabelStyle = new LabelStyle();
        yellowLabelStyle = new LabelStyle();
        greenLabelStyle = new LabelStyle();

        whiteButtonStyle.font = game.font;
        whiteLabelStyle.font = game.font;

        redButtonStyle.font = game.font;
        redButtonStyle.fontColor = Color.RED;

        orangeButtonStyle.font = game.font;
        orangeButtonStyle.fontColor = Color.ORANGE;

        greenButtonStyle.font = game.font;
        greenButtonStyle.fontColor = Color.GREEN;

        redLabelStyle.font = game.font;
        redLabelStyle.fontColor = Color.RED;

        blueLabelStyle.font = game.font;
        blueLabelStyle.fontColor = Color.BLUE;

        orangeLabelStyle.font = game.font;
        orangeLabelStyle.fontColor = Color.ORANGE;

        yellowLabelStyle.font = game.font;
        yellowLabelStyle.fontColor = Color.YELLOW;

        greenLabelStyle.font = game.font;
        greenLabelStyle.fontColor = Color.GREEN;
    }

    private void createCamera()
    {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SpitfireGame.WIDTH, SpitfireGame.HEIGHT);
        camera.update();
    }

    @Override
    public void render(float delta)
    {
        clearScreen();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        stage.act(delta);
    }

    private void clearScreen()
    {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resume()
    {
        Gdx.app.log(TAG, "resume()");
    }

    @Override
    public void pause()
    {
        System.out.println("pause");

        if (SpitfireGame.getGameState() == GameState.RUN)
        {
            SpitfireGame.setGameState(GameState.PAUSE);
        }
    }

    @Override
    public void dispose()
    {
        skin.dispose();
        game.dispose();
        spriteBatch.dispose();
        stage.dispose();
    }

    @Override
    public void hide()
    {

    }

    @Override
    public void show()
    {

    }

    @Override
    public void resize(int width, int height)
    {

    }
}

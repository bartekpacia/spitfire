package pl.baftek.spitfire.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.golfgl.gdxgamesvcs.GameServiceException;
import de.golfgl.gdxgamesvcs.IGameServiceListener;
import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.game.StringHelper;
import pl.baftek.spitfire.ui.InfoDialog;
import pl.baftek.spitfire.ui.MyTextButton;
import pl.baftek.spitfire.ui.PopupLabel;

public class MenuScreen extends AbstractScreen
{
    private static final String TAG = "MenuScreen";
    private Table menuTable;

    private HorizontalGroup upGroup;
    private HorizontalGroup midGroup;
    private VerticalGroup buttonsGroup;

    private MyTextButton playerButton;

    private Label titleLabel;
    private Label creatorLabel;
    private Label signLabel;
    private Label versionTypeLabel;
    private TextButton bonusButton;

    private Texture leaderboardTexture;
    private Texture achievementsTexture;
    private Texture playGamesTexture;
    private Texture moneyTexture;

    private Image playGamesImage;

    public MenuScreen(SpitfireGame game)
    {
        super(game);
    }

    @Override
    protected void init()
    {
        leaderboardTexture = new Texture("leaderboards.png");
        achievementsTexture = new Texture("achievement.png");
        playGamesTexture = new Texture("play_games.png");
        moneyTexture = new Texture("money_tiny.png");

        boolean resumeSession = SpitfireGame.gameServiceClient.resumeSession();
        Gdx.app.log("SpitfireGame.gameServiceClient.resumeSession()", Boolean.toString(resumeSession));
    }

    @Override
    protected void buildUI()
    {
        initStatusGroup();
        initMidGroup();
        initButtons();
        initTitle();
        initPlayGamesImageAndLabel();
        initBonusButton();

        menuTable = new Table();

        menuTable.add(upGroup).padBottom(80).row();
        menuTable.add(midGroup).row();
        menuTable.add(versionTypeLabel).padBottom(200).row();
        menuTable.add(buttonsGroup).padBottom(60).row();
        menuTable.add(playGamesImage).row();
        menuTable.add(signLabel).padBottom(50).row();
        menuTable.add(creatorLabel).left().row();
        menuTable.top();
        menuTable.setFillParent(true);

        SpitfireGame.gameServiceClient.setListener(new IGameServiceListener()
        {
            @Override
            public void gsOnSessionActive()
            {
                updatePlayGamesUI();
                Gdx.app.log(TAG, "gsOnSessionActive();");
            }

            @Override
            public void gsOnSessionInactive()
            {
                updatePlayGamesUI();
                Gdx.app.log(TAG, "gsOnSessionInactive();");
            }

            @Override
            public void gsShowErrorToUser(GsErrorType et, String msg, Throwable t)
            {
                Gdx.app.error(et.name(), msg);
            }
        });

        stage.addActor(menuTable);
        updatePlayGamesUI();
    }

    private void initStatusGroup()
    {
        Label moneyLabel = new Label(Integer.toString(game.getMoney()), whiteLabelStyle);
        moneyLabel.setFontScale(verySmallFontSize);

        Image moneyImg = new Image(moneyTexture);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.space(10);
        horizontalGroup.addActor(moneyLabel);
        horizontalGroup.addActor(moneyImg);

        Label lvlLabel = new Label("LVL " + game.getPlayerLevel(), greenLabelStyle);
        lvlLabel.setFontScale(verySmallFontSize);

        Label xpLabel = new Label(game.getXp() + " / " + game.getXpForNextLevel() + " XP", greenLabelStyle);
        xpLabel.setFontScale(verySmallFontSize);

        upGroup = new HorizontalGroup();
        upGroup.space(30);
        upGroup.addActor(horizontalGroup);
        upGroup.addActor(lvlLabel);
        upGroup.addActor(xpLabel);
    }

    private void initMidGroup()
    {
        playerButton = new MyTextButton("placeholder", smallFontSize);

        playerButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new ProfileScreen(game));
                super.clicked(event, x, y);
            }
        });

        Image imageLeaderboards = new Image(leaderboardTexture);
        imageLeaderboards.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new LeaderboardsScreen(game));
                super.clicked(event, x, y);
            }
        });

        Image imageAchievements = new Image(achievementsTexture);
        imageAchievements.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                try
                {
                    SpitfireGame.gameServiceClient.showAchievements();
                } catch (GameServiceException e)
                {
                    e.printStackTrace();
                }
                super.clicked(event, x, y);
            }
        });

        HorizontalGroup imagesGroup = new HorizontalGroup();
        imagesGroup.space(50);
        imagesGroup.addActor(imageLeaderboards);
        imagesGroup.addActor(imageAchievements);

        midGroup = new HorizontalGroup();
        midGroup.pad(25, 25, 25, 25);
        midGroup.setDebug(true);
        midGroup.space(35);
        midGroup.addActor(playerButton);
        midGroup.addActor(imagesGroup);
    }

    private void initButtons()
    {
        MyTextButton buttonPlay = new MyTextButton("PLAY", normalFontSize);
        buttonPlay.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (SpitfireGame.gameServiceClient.isSessionActive())
                {
                    game.setScreen(new GameplayScreen(game));
                } else
                {
                    new PopupLabel("Login with Play Games!", Color.RED, stage);
                }

                super.clicked(event, x, y);
            }
        });

        MyTextButton buttonUpgrades = new MyTextButton("HANGAR", normalFontSize);
        buttonUpgrades.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new HangarScreen(game));
                super.clicked(event, x, y);
            }
        });

        MyTextButton settingsButton = new MyTextButton("SETTINGS", normalFontSize);
        settingsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new SettingsScreen(game));
                super.clicked(event, x, y);
            }
        });

        buttonsGroup = new VerticalGroup();
        buttonsGroup.space(40);

        buttonsGroup.addActor(buttonPlay);

        buttonsGroup.addActor(buttonUpgrades);
        buttonsGroup.addActor(settingsButton);
    }

    private void initPlayGamesImageAndLabel()
    {
        playGamesImage = new Image(playGamesTexture);
        signLabel = new Label(StringHelper.SIGNED_OUT, redLabelStyle);
        signLabel.setFontScale(verySmallFontSize);

        playGamesImage.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (SpitfireGame.gameServiceClient.isSessionActive())
                {
                    SpitfireGame.gameServiceClient.logOff();
                } else
                {
                    SpitfireGame.gameServiceClient.logIn();
                }

                updatePlayGamesUI();

                super.clicked(event, x, y);
            }
        });
    }

    private void initTitle()
    {
        titleLabel = new Label("SPITFIRE", redLabelStyle);
        titleLabel.setFontScale(hugeFontSize);

        creatorLabel = new Label("created by Baftek", whiteLabelStyle);
        creatorLabel.setFontScale(picoFontSize);

        versionTypeLabel = new Label(StringHelper.VERSION_NAME + StringHelper.VERSION, blueLabelStyle);
        versionTypeLabel.setFontScale(ultraSmallFontSize);
    }

    private void updatePlayGamesUI()
    {
        String string;
        if (SpitfireGame.gameServiceClient.isSessionActive())
        {
            string = StringHelper.SIGNED_IN;
            signLabel.setText(string);
            signLabel.setStyle(greenLabelStyle);
            playerButton.setText(SpitfireGame.gameServiceClient.getPlayerDisplayName());
        } else
        {
            string = StringHelper.SIGNED_OUT;
            signLabel.setText(string);
            signLabel.setStyle(redLabelStyle);
            playerButton.setText("Logged out");
        }
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);

        stage.draw();
    }

    private void initBonusButton()
    {
        bonusButton = new TextButton("DAILY BONUS!", orangeButtonStyle);
        bonusButton.getLabel().setFontScale(AbstractScreen.smallFontSize);
        bonusButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                InfoDialog dialog = new InfoDialog("Help", "For first flight of the day,\n you will get " + SpitfireGame.BONUS_MULTIPLIER + "x more money!", "OK")
                {
                    @Override
                    protected void result(Object object)
                    {
                        if (object.equals(true))
                        {
                            game.setScreen(new GameplayScreen(game));
                        }
                        super.result(object);
                    }
                };

                MyTextButton playButton = new MyTextButton("PLAY!", smallFontSize);
                dialog.button(playButton, true);

                dialog.show(stage);

                super.clicked(event, x, y);
            }
        });

        if (SpitfireGame.isDailyBonusAvailable())
        {
            stage.addActor(bonusButton);
            bonusButton.setPosition(SpitfireGame.WIDTH / 2 - bonusButton.getWidth() / 2, SpitfireGame.HEIGHT / 2 + 110);
        }
    }

    @Override
    public void dispose()
    {
        leaderboardTexture.dispose();
        achievementsTexture.dispose();
        playGamesTexture.dispose();
        moneyTexture.dispose();

        super.dispose();
    }
}
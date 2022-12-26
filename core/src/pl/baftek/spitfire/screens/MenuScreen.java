package pl.baftek.spitfire.screens;

import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.blueLabelStyle;
import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.greenLabelStyle;
import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.orangeButtonStyle;
import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.redLabelStyle;
import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.whiteLabelStyle;
import static pl.baftek.spitfire.game.StringHelper.LEADERBOARD_HIGH_SCORE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

public class MenuScreen extends AbstractScreen {
    private static final String TAG = "MenuScreen";
    private Table menuTable;

    private HorizontalGroup upGroup;
    private HorizontalGroup midGroup;
    private VerticalGroup buttonsGroup;

    private MyTextButton playerButton;
    private MyTextButton creditsButton;

    private Label titleLabel;
    private Label creatorLabel;
    private Label signLabel;
    private Label versionLabel;
    private TextButton bonusButton;

    private Image playGamesImage;

    public MenuScreen(SpitfireGame game) {
        super(game);
    }

    @Override
    protected void init() {
        playGamesImage = new Image(SpitfireGame.ResHelper.playGames);
        game.gameServiceClient.resumeSession();
    }

    @Override
    protected void buildUI() {
        initStatusGroup();
        initMidGroup();
        initButtons();
        initTitle();
        initPlayGamesImageAndLabel();
        initBonusButton();
        initCreditsButton();

        menuTable = new Table();

        menuTable.add(upGroup).padBottom(80).row();
        menuTable.add(midGroup).row();
        menuTable.add(versionLabel).padBottom(180).row();
        menuTable.add(buttonsGroup).padBottom(30).row();
        menuTable.add(playGamesImage).row();
        menuTable.add(signLabel).padBottom(40).row();
        menuTable.add(creatorLabel).left().bottom().row();
        menuTable.add(creditsButton).left().row();
        menuTable.top();
        menuTable.setFillParent(true);

        game.gameServiceClient.setListener(new IGameServiceListener() {
            @Override
            public void gsOnSessionActive() {
                updatePlayGamesUI();
                Gdx.app.log(TAG, "gsOnSessionActive();");
            }

            @Override
            public void gsOnSessionInactive() {
                updatePlayGamesUI();
                Gdx.app.log(TAG, "gsOnSessionInactive();");
            }

            @Override
            public void gsShowErrorToUser(GsErrorType et, String msg, Throwable t) {
                Gdx.app.error(et.name(), msg);
            }
        });

        stage.addActor(menuTable);
        updatePlayGamesUI();
    }

    private void initCreditsButton() {
        creditsButton = new MyTextButton("Credits", AbstractScreen.FONT_SIZE_3);
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog dialog = new InfoDialog("Credits", StringHelper.CREDITS_TEXT, "OK");
                dialog.show(stage);
                super.clicked(event, x, y);
            }
        });
    }

    private void initStatusGroup() {
        Label moneyLabel = new Label(Integer.toString(game.playerManager.getMoney()), whiteLabelStyle);
        moneyLabel.setFontScale(FONT_SIZE_2);

        Image moneyImg = new Image(SpitfireGame.ResHelper.smallMoney);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.space(10);
        horizontalGroup.addActor(moneyLabel);
        horizontalGroup.addActor(moneyImg);

        Label lvlLabel = new Label("LVL " + game.playerManager.getLevel(), greenLabelStyle);
        lvlLabel.setFontScale(FONT_SIZE_2);

        Label xpLabel = new Label(game.playerManager.getXp() + " / " + game.playerManager.getXpForNextLevel() + " XP", greenLabelStyle);
        xpLabel.setFontScale(FONT_SIZE_2);

        upGroup = new HorizontalGroup();
        upGroup.space(30);
        upGroup.addActor(horizontalGroup);
        upGroup.addActor(lvlLabel);
        upGroup.addActor(xpLabel);
    }

    private void initMidGroup() {
        playerButton = new MyTextButton("placeholder", FONT_SIZE_3);

        playerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ProfileScreen(game));
                super.clicked(event, x, y);
            }
        });

        Image imageLeaderboards = new Image(SpitfireGame.ResHelper.leaderboard);
        imageLeaderboards.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    game.gameServiceClient.showLeaderboards(LEADERBOARD_HIGH_SCORE);
                } catch (GameServiceException e) {
                    e.printStackTrace();
                }
                super.clicked(event, x, y);
            }
        });

        Image imageAchievements = new Image(SpitfireGame.ResHelper.achievements);
        imageAchievements.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    game.gameServiceClient.showAchievements();
                } catch (GameServiceException e) {
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

    private void initButtons() {
        MyTextButton buttonPlay = new MyTextButton("PLAY", FONT_SIZE_5);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!game.gameServiceClient.isSessionActive()) {
                    new PopupLabel("Login with Play Games!", Color.RED, stage);
                }
                game.setScreen(new GameplayScreen(game));
                super.clicked(event, x, y);
            }
        });

        MyTextButton buttonUpgrades = new MyTextButton("HANGAR", FONT_SIZE_5);
        buttonUpgrades.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HangarScreen(game));
                super.clicked(event, x, y);
            }
        });

        MyTextButton settingsButton = new MyTextButton("SETTINGS", FONT_SIZE_5);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
                super.clicked(event, x, y);
            }
        });

        buttonsGroup = new VerticalGroup();
        buttonsGroup.space(30);

        buttonsGroup.addActor(buttonPlay);

        buttonsGroup.addActor(buttonUpgrades);
        buttonsGroup.addActor(settingsButton);
    }

    private void initPlayGamesImageAndLabel() {
        playGamesImage = new Image(SpitfireGame.ResHelper.playGames);
        signLabel = new Label(StringHelper.SIGNED_OUT_ANDROID, redLabelStyle);
        signLabel.setFontScale(FONT_SIZE_2);

        playGamesImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameServiceClient.isSessionActive()) {
                    game.gameServiceClient.logOff();
                } else {
                    game.gameServiceClient.logIn();
                }

                updatePlayGamesUI();

                super.clicked(event, x, y);
            }
        });
    }

    private void initTitle() {
        titleLabel = new Label("SPITFIRE", redLabelStyle);
        titleLabel.setFontScale(FONT_SIZE_7);

        versionLabel = new Label(StringHelper.VERSION, blueLabelStyle);
        versionLabel.setFontScale(FONT_SIZE_1);

        creatorLabel = new Label("created by Baftek", whiteLabelStyle);
        creatorLabel.setFontScale(FONT_SIZE_0);
    }

    private void updatePlayGamesUI() {
        String string;
        if (game.gameServiceClient.isSessionActive()) {
            string = StringHelper.SIGNED_IN_ANDROID;
            signLabel.setText(string);
            signLabel.setStyle(greenLabelStyle);
            playerButton.setText(game.gameServiceClient.getPlayerDisplayName());
        } else {
            string = StringHelper.SIGNED_OUT_ANDROID;
            signLabel.setText(string);
            signLabel.setStyle(redLabelStyle);
            playerButton.setText("Logged out");
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stage.draw();
    }

    private void initBonusButton() {
        bonusButton = new TextButton("DAILY BONUS!", orangeButtonStyle);
        bonusButton.getLabel().setFontScale(AbstractScreen.FONT_SIZE_3);
        bonusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog dialog = new InfoDialog("Help", "For first flight of the day,\n you will get " + SpitfireGame.BONUS_MULTIPLIER + "x more money!", "OK") {
                    @Override
                    protected void result(Object object) {
                        if (object.equals(Boolean.TRUE)) {
                            game.setScreen(new GameplayScreen(game));
                        }
                        super.result(object);
                    }
                };

                MyTextButton playButton = new MyTextButton("PLAY!", FONT_SIZE_3);
                dialog.button(playButton, true);

                dialog.show(stage);

                super.clicked(event, x, y);
            }
        });

        if (SpitfireGame.isDailyBonusAvailable()) {
            stage.addActor(bonusButton);
            bonusButton.setPosition(SpitfireGame.WIDTH / 2 - bonusButton.getWidth() / 2, SpitfireGame.HEIGHT / 2 + 110);
        }
    }
}

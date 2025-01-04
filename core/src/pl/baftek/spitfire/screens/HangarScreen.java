package pl.baftek.spitfire.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import pl.baftek.spitfire.enums.PlayerType;
import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.game.StringHelper;
import pl.baftek.spitfire.ui.MyTextButton;

import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.orangeLabelStyle;
import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.whiteLabelStyle;

public class HangarScreen extends AbstractScreen {
    private Timer timer;

    private Table table;

    private Label currentPlaneLabel;
    private Label availabilityLabel;
    private Image planeImage;
    private Label descLabel;

    private State state;

    private static class State {
        private final PlayerType currentPlayerType;
        private final String availabilityString;
        private final String currentPlaneString;
        private final String action;
        private final String desc;
        private final boolean bought;
        private final Texture texture;

        public State(PlayerType playerType, SpitfireGame game) {
            this.currentPlayerType = playerType;

            if (game.isBought(playerType)) {
                game.setCurrentPlayerType(playerType);
            }

            if (playerType == PlayerType.SPITFIRE) {
                texture = SpitfireGame.ResHelper.spitfire;
                currentPlaneString = StringHelper.SPITIFRE;

                action = StringHelper.UPGRADE;
                desc = StringHelper.SPITFIRE_DESC;
                bought = true;
            } else if (playerType == PlayerType.MUSTANG) {
                texture = SpitfireGame.ResHelper.mustang;
                currentPlaneString = StringHelper.MUSTANG;
                desc = StringHelper.MUSTANG_DESC;

                if (game.isBought(PlayerType.MUSTANG)) {
                    action = StringHelper.UPGRADE;
                    bought = true;
                } else {
                    action = StringHelper.BUY + game.getPlanePrice(PlayerType.MUSTANG);
                    bought = false;
                }
            } else if (playerType == PlayerType.SZTURMOVIK) {
                texture = SpitfireGame.ResHelper.szturmovik;
                currentPlaneString = StringHelper.IL2;
                desc = StringHelper.IL2_DESC;

                if (game.isBought(PlayerType.SZTURMOVIK)) {
                    action = StringHelper.UPGRADE;
                    bought = true;
                } else {
                    action = StringHelper.BUY + game.getPlanePrice(PlayerType.SZTURMOVIK);
                    bought = false;
                }
            } else {
                desc = null;
                action = null;
                texture = null;
                currentPlaneString = null;
                bought = false;
            }

            availabilityString = Integer.toString(game.getCurrentPlaneAvailabilityLevel(playerType));
        }
    }

    HangarScreen(SpitfireGame game) {
        super(game);
    }

    @Override
    protected void init() {
        timer = new Timer();
        state = new State(game.getCurrentPlayerType(), game);
    }

    @Override
    protected void buildUI() {
        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        table.top();

        initUpGroup();
        initTitle();
        initContentGroup();
        initExitButton();

        stage.addActor(table);
    }

    private void initContentGroup() {
        planeImage = new Image();

        MyTextButton actionButton = new MyTextButton(state.action, FONT_SIZE_3);
        if (!state.bought) {
            Image moneyImage = new Image(SpitfireGame.ResHelper.smallMoney);
            actionButton.add(moneyImage).right();
        }
        actionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayerType playerType = state.currentPlayerType;

                //spitfire
                if (playerType == PlayerType.SPITFIRE) {
                    game.setScreen(new UpgradesScreen(game));
                }

                //mustang
                if (playerType == PlayerType.MUSTANG && !game.isBought(PlayerType.MUSTANG)) {
                    game.buyPlane(PlayerType.MUSTANG, stage);
                    game.setCurrentPlayerType(PlayerType.MUSTANG);
                    updateContentGroup(PlayerType.MUSTANG);
                } else if (playerType == PlayerType.MUSTANG && game.isBought(PlayerType.MUSTANG)) {
                    game.setScreen(new UpgradesScreen(game));
                }

                //il2
                if (playerType == PlayerType.SZTURMOVIK && !game.isBought(PlayerType.SZTURMOVIK)) {
                    game.buyPlane(PlayerType.SZTURMOVIK, stage);
                    game.setCurrentPlayerType(PlayerType.SZTURMOVIK);
                    updateContentGroup(PlayerType.SZTURMOVIK);
                } else if (playerType == PlayerType.SZTURMOVIK && game.isBought(PlayerType.SZTURMOVIK)) {
                    game.setScreen(new UpgradesScreen(game));
                }

                super.clicked(event, x, y);
            }
        });

        Label left = new Label("<", orangeLabelStyle);
        left.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayerType playerType = state.currentPlayerType;

                if (playerType == PlayerType.SPITFIRE) {
                    updateContentGroup(PlayerType.MUSTANG);
                } else if (playerType == PlayerType.MUSTANG) {
                    updateContentGroup(PlayerType.SZTURMOVIK);
                } else if (playerType == PlayerType.SZTURMOVIK) {
                    updateContentGroup(PlayerType.SPITFIRE);
                }

                super.clicked(event, x, y);
            }
        });

        Label right = new Label(">", orangeLabelStyle);
        right.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayerType playerType = state.currentPlayerType;

                if (playerType == PlayerType.SPITFIRE) {
                    updateContentGroup(PlayerType.SZTURMOVIK);
                } else if (playerType == PlayerType.SZTURMOVIK) {
                    updateContentGroup(PlayerType.MUSTANG);
                } else if (playerType == PlayerType.MUSTANG) {
                    updateContentGroup(PlayerType.SPITFIRE);
                }

                super.clicked(event, x, y);
            }
        });

        currentPlaneLabel = new Label(null, whiteLabelStyle);
        currentPlaneLabel.setFontScale(FONT_SIZE_4);

        availabilityLabel = new Label(null, orangeLabelStyle);
        availabilityLabel.setFontScale(FONT_SIZE_2);

        descLabel = new Label(null, whiteLabelStyle);
        descLabel.setFontScale(FONT_SIZE_1);
        descLabel.setAlignment(Align.center);

        table.add(left).padLeft(16).padRight(16);
        table.add(currentPlaneLabel).expandX();
        table.add(right).pad(16).padRight(16);
        table.row();

        table.add(availabilityLabel).colspan(3);
        table.row();

        table.add(actionButton).pad(32).colspan(3);
        table.row();

        table.add(planeImage).colspan(3);
        table.row();

        table.add(descLabel).colspan(3);
        table.row();

        // Set initial values
        updateContentGroup(game.getCurrentPlayerType());
    }

    private void updateContentGroup(PlayerType playerType) {
        if (game.isBought(playerType)) {
            game.setCurrentPlayerType(playerType);
        }

        state = new State(playerType, game);
        currentPlaneLabel.setText(state.currentPlaneString);
        availabilityLabel.setText(StringHelper.AVAILABLE_FROM_LEVEL + state.availabilityString);
        planeImage.setDrawable(new TextureRegionDrawable(new TextureRegion(state.texture)));
        descLabel.setText(state.desc);
    }

    private void initUpGroup() {
        HorizontalGroup moneyHG = new HorizontalGroup();

        Image moneyImage = new Image(SpitfireGame.ResHelper.smallMoney);

        Label moneyLabel = new Label(Integer.toString(game.playerManager.getMoney()), whiteLabelStyle);
        moneyLabel.setFontScale(FONT_SIZE_3);

        //refreshing money
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                moneyLabel.setText(Integer.toString(game.playerManager.getMoney()));
            }
        }, 0.5f, 0.5f);

        moneyHG.addActor(moneyLabel);
        moneyHG.addActor(moneyImage);

        table.add(moneyHG).colspan(3);
        table.row();
    }

    private void initTitle() {
        Label upgradesTitle = new Label(StringHelper.HANGAR, whiteLabelStyle);
        upgradesTitle.setFontScale(FONT_SIZE_6);

        table.add(upgradesTitle).colspan(3);
        table.row();
    }

    private void initExitButton() {
        MyTextButton exitButton = new MyTextButton(StringHelper.GO_TO_MENU, FONT_SIZE_3);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                super.clicked(event, x, y);
            }
        });

        table.add(exitButton).padTop(48).colspan(3);
        table.row();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        spriteBatch.begin();
        spriteBatch.draw(SpitfireGame.ResHelper.hangar, 0, 0);
        spriteBatch.end();

        stage.draw();
    }
}

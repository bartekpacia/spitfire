package pl.baftek.spitfire.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

//    private HorizontalGroup upGroup;
//    private VerticalGroup mainVerticalGroup;
//    private VerticalGroup contentVG;

    HangarScreen(SpitfireGame game) {
        super(game);
    }

    @Override
    protected void init() {
        timer = new Timer();
    }

    @Override
    protected void buildUI() {
//        upGroup = new HorizontalGroup();
//        upGroup.padTop(10);
//        upGroup.padBottom(10);
//        upGroup.space(10);
//
//        mainVerticalGroup = new VerticalGroup();
//        mainVerticalGroup.setDebug(true);
//        mainVerticalGroup.fill();
//        mainVerticalGroup.padTop(20);
//        mainVerticalGroup.padBottom(20);
//        mainVerticalGroup.space(20);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        table.top();

        initUpGroup();
        initTitle();
        initContentGroup(game.getCurrentPlayerType());
        initExitButton();

        // table.add(mainVerticalGroup).height(950).row();
        // table.add(exitButton).row();
        // table.top(); //sets table in upper part of the screen, not middle
        stage.addActor(table);
    }

    private void initContentGroup(final PlayerType playerType) {
        //init section
        String availabilityString;
        String currentPlaneString;
        String action;
        String desc;
        boolean bought;

        Texture texture;

//        contentVG = new VerticalGroup();
//        contentVG.space(10);

        // scrollerTable = new Table();
        // scrollerTable.space(30);

        System.out.println("playerType:" + playerType.toString());

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

        Image planeImage = new Image(texture);

        // private Table scrollerTable;
        MyTextButton actionButton = new MyTextButton(action, FONT_SIZE_3);
        if (!bought) {
            Image moneyImage = new Image(SpitfireGame.ResHelper.smallMoney);
            actionButton.add(moneyImage).right();
        }
        actionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //spitfire
                if (playerType == PlayerType.SPITFIRE) {
                    game.setScreen(new UpgradesScreen(game));
                }

                //mustang
                if (playerType == PlayerType.MUSTANG && !game.isBought(PlayerType.MUSTANG)) {
                    game.buyPlane(PlayerType.MUSTANG, stage);
                    game.setCurrentPlayerType(PlayerType.MUSTANG);
                    refreshContentGroup(PlayerType.MUSTANG);
                } else if (playerType == PlayerType.MUSTANG && game.isBought(PlayerType.MUSTANG)) {
                    game.setScreen(new UpgradesScreen(game));
                }

                //il2
                if (playerType == PlayerType.SZTURMOVIK && !game.isBought(PlayerType.SZTURMOVIK)) {
                    game.buyPlane(PlayerType.SZTURMOVIK, stage);
                    game.setCurrentPlayerType(PlayerType.SZTURMOVIK);
                    refreshContentGroup(PlayerType.SZTURMOVIK);
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
                if (playerType == PlayerType.SPITFIRE) {
                    refreshContentGroup(PlayerType.MUSTANG);
                } else if (playerType == PlayerType.MUSTANG) {
                    refreshContentGroup(PlayerType.SZTURMOVIK);
                } else if (playerType == PlayerType.SZTURMOVIK) {
                    refreshContentGroup(PlayerType.SPITFIRE);
                }

                super.clicked(event, x, y);
            }
        });

        Label right = new Label(">", orangeLabelStyle);
        right.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerType == PlayerType.SPITFIRE) {
                    refreshContentGroup(PlayerType.SZTURMOVIK);
                } else if (playerType == PlayerType.SZTURMOVIK) {
                    refreshContentGroup(PlayerType.MUSTANG);
                } else if (playerType == PlayerType.MUSTANG) {
                    refreshContentGroup(PlayerType.SPITFIRE);
                }

                super.clicked(event, x, y);
            }
        });

        Label currentPlaneLabel = new Label(currentPlaneString, whiteLabelStyle);
        currentPlaneLabel.setFontScale(FONT_SIZE_4);

        Label availabilityLabel = new Label(StringHelper.AVAILABLE_FROM_LEVEL + availabilityString, orangeLabelStyle);
        availabilityLabel.setFontScale(FONT_SIZE_2);

        Label descLabel = new Label(desc, whiteLabelStyle);
        descLabel.setFontScale(FONT_SIZE_1);
        descLabel.setAlignment(Align.center);

        table.add(availabilityLabel).colspan(3);
        table.row();

        table.add(left).padLeft(16).padRight(16);
        table.add(currentPlaneLabel).expandX();
        table.add(right).pad(16).padRight(16);
        table.row();

        table.add(actionButton).colspan(3);
        table.row();

        table.add(planeImage).colspan(3);
        table.row();

        table.add(descLabel).colspan(3);
        table.row();
    }

    private void refreshContentGroup(PlayerType playerType) {
        // mainVerticalGroup.removeActor(contentVG);
        initContentGroup(playerType);
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

        // upGroup.addActor(moneyHG);

        table.add(moneyHG).colspan(3);
        // table.add(moneyLabel).expandX().right().padRight(24);
        // table.add(moneyImage).expandX().left();
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

        table.add(exitButton).colspan(3);
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

package pl.baftek.spitfire.screens;


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
import com.badlogic.gdx.utils.Align;

import pl.baftek.spitfire.enums.PlayerType;
import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.game.StringHelper;
import pl.baftek.spitfire.ui.MyTextButton;
import pl.baftek.spitfire.ui.PopupLabel;

import static pl.baftek.spitfire.game.StringHelper.LEVEL;
import static pl.baftek.spitfire.game.StringHelper.UPGRADE;

public class UpgradesScreen extends AbstractScreen
{
    private final int ROW_HORIZONTAL_SPACING = 15;
    private Table table;

    private HorizontalGroup moneyHG;
    private VerticalGroup contentVG;
    private HorizontalGroup machineGunHG;
    private HorizontalGroup engineHG;
    private HorizontalGroup missileHG;

    private Label titleLabel;
    private Label currentPlaneLabel;
    private Label moneyLabel;
    private Image moneyImage;
    private MyTextButton backButton;

    private Texture bgTexture;
    private Texture moneyTexture;
    private Texture engineTexture;
    private Texture mgTexture;
    private Texture missileTexture;

    UpgradesScreen(SpitfireGame game)
    {
        super(game);
    }

    @Override
    protected void init()
    {
        bgTexture = new Texture("hangar.jpg");
        moneyTexture = new Texture("money_tiny.png");
        mgTexture = new Texture("machine_gun.png");
        engineTexture = new Texture("engine.png");
        missileTexture = new Texture("missile_icon.png");
    }

    @Override
    protected void buildUI()
    {
        table = new Table();
        table.pad(5, 5, 5, 5);

        initMoneyHG();
        initTitle();
        initContentVG();
        initBackButton();

        contentVG.addActor(machineGunHG);
        contentVG.addActor(engineHG);

        if(game.getCurrentPlayerType() == PlayerType.IL2)
        {
            contentVG.addActor(missileHG);
        }

        table.add(moneyHG).row();
        table.add(titleLabel).row();
        table.add(currentPlaneLabel).padBottom(100).row();
        table.add(contentVG).row();
        table.add(backButton).padTop(200).row();
        table.align(Align.top);
        table.setFillParent(true);

        stage.addActor(table);
    }

    private void initContentVG()
    {
        contentVG = new VerticalGroup();
        contentVG.space(10);

        initMachineGunHG();
        initEngineHG();
        initMissileHG();
    }

    private void initMachineGunHG()
    {
        machineGunHG = new HorizontalGroup();
        machineGunHG.space(ROW_HORIZONTAL_SPACING);

        Image image = new Image(mgTexture);

        VerticalGroup textVerticalGroup = new VerticalGroup();
        textVerticalGroup.columnAlign(Align.left);

        Label title = new Label(StringHelper.MACHINE_GUN, whiteLabelStyle);
        title.setFontScale(verySmallFontSize);
        final Label level = new Label(LEVEL + game.getMGLevel(game.getCurrentPlayerType()), whiteLabelStyle);
        level.setFontScale(verySmallFontSize);

        final Image moneyImg = new Image(moneyTexture);

        final MyTextButton upgradeButton = new MyTextButton(StringHelper.UPGRADE + game.getMGUpgradeCost(game.getCurrentPlayerType()), smallFontSize);
        upgradeButton.add(moneyImg).align(Align.right).space(10);
        upgradeButton.getLabel().setAlignment(Align.center);

        upgradeButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (game.getMGLevel(game.getCurrentPlayerType()) == 1)
                {

                    upgradeMG(2, upgradeButton, level);

                }

                else if (game.getMGLevel(game.getCurrentPlayerType()) == 2)
                {

                    upgradeMG(3, upgradeButton, level);

                }

                else if (game.getMGLevel(game.getCurrentPlayerType()) == 3)
                {

                    upgradeMG(4, upgradeButton, level);

                }

                else if (game.getMGLevel(game.getCurrentPlayerType()) == 4)
                {

                    upgradeMG(5, upgradeButton, level);

                }

                else if (game.getMGLevel(game.getCurrentPlayerType()) == 5)
                {

                    upgradeMG(6, upgradeButton, level);

                }

                else if (game.getMGLevel(game.getCurrentPlayerType()) == 6)
                {

                    upgradeMG(7, upgradeButton, level);

                }

                else if (game.getMGLevel(game.getCurrentPlayerType()) == 7)
                {

                    upgradeMG(8, upgradeButton, level);

                }

                else if (game.getMGLevel(game.getCurrentPlayerType()) == 8)
                {

                    upgradeMG(9, upgradeButton, level);

                }

                else if (game.getMGLevel(game.getCurrentPlayerType()) == 9)
                {

                    upgradeMG(10, upgradeButton, level);

                }

                else if (game.getMGLevel(game.getCurrentPlayerType()) == 10)
                {
                    new PopupLabel(StringHelper.MAXED_OUT_LC, Color.GREEN, stage);
                }

                super.clicked(event, x, y);
            }
        });

        if (game.getMGLevel(game.getCurrentPlayerType()) == 10)
        {
            upgradeButton.setVisible(false);
        }

        textVerticalGroup.addActor(title);
        textVerticalGroup.addActor(level);
        textVerticalGroup.addActor(upgradeButton);

        machineGunHG.addActor(image);
        machineGunHG.addActor(textVerticalGroup);

        level.setWidth(title.getWidth());
    }

    private void initEngineHG()
    {
        engineHG = new HorizontalGroup();
        engineHG.space(ROW_HORIZONTAL_SPACING);

        Image image = new Image(engineTexture);

        VerticalGroup textVerticalGroup = new VerticalGroup();
        textVerticalGroup.columnAlign(Align.left);

        Label title = new Label(StringHelper.ENGINE, whiteLabelStyle);
        title.setFontScale(verySmallFontSize);
        final Label level = new Label(LEVEL + game.getEngineLevel(game.getCurrentPlayerType()), whiteLabelStyle);
        level.setFontScale(verySmallFontSize);

        final Image moneyImg = new Image(moneyTexture);

        final MyTextButton upgradeButton = new MyTextButton(StringHelper.UPGRADE + game.getEngineUpgradeCost(game.getCurrentPlayerType()), smallFontSize);
        upgradeButton.add(moneyImg).align(Align.right).space(10);
        upgradeButton.getLabel().setAlignment(Align.center);

        upgradeButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (game.getEngineLevel(game.getCurrentPlayerType()) == 1)
                {
                    upgradeEngine(2, upgradeButton, level);
                }

                else if (game.getEngineLevel(game.getCurrentPlayerType()) == 2)
                {
                    upgradeEngine(3, upgradeButton, level);
                }

                else if (game.getEngineLevel(game.getCurrentPlayerType()) == 3)
                {
                    upgradeEngine(4, upgradeButton, level);
                }

                else if (game.getEngineLevel(game.getCurrentPlayerType()) == 4)
                {
                    upgradeEngine(5, upgradeButton, level);
                }

                else if (game.getEngineLevel(game.getCurrentPlayerType()) == 5)
                {

                    upgradeEngine(6, upgradeButton, level);

                }

                else if (game.getEngineLevel(game.getCurrentPlayerType()) == 6)
                {

                    upgradeEngine(7, upgradeButton, level);

                }

                else if (game.getEngineLevel(game.getCurrentPlayerType()) == 7)
                {

                    upgradeEngine(8, upgradeButton, level);

                }

                else if (game.getEngineLevel(game.getCurrentPlayerType()) == 8)
                {
                    upgradeEngine(9, upgradeButton, level);

                }

                else if (game.getEngineLevel(game.getCurrentPlayerType()) == 9)
                {

                    upgradeEngine(10, upgradeButton, level);

                }

                else if (game.getEngineLevel(game.getCurrentPlayerType()) == 10)
                {
                    new PopupLabel(StringHelper.MAXED_OUT_LC, Color.GREEN, stage);
                }

                super.clicked(event, x, y);
            }
        });

        if (game.getEngineLevel(game.getCurrentPlayerType()) == 10)
        {
            upgradeButton.setVisible(false);
        }

        textVerticalGroup.addActor(title);
        textVerticalGroup.addActor(level);
        textVerticalGroup.addActor(upgradeButton);

        engineHG.addActor(image);
        engineHG.addActor(textVerticalGroup);

        level.setWidth(title.getWidth());
    }

    private void initMissileHG()
    {
        missileHG = new HorizontalGroup();
        missileHG.space(ROW_HORIZONTAL_SPACING);

        Image image = new Image(missileTexture);

        VerticalGroup textVerticalGroup = new VerticalGroup();
        textVerticalGroup.columnAlign(Align.left);

        Label title = new Label(StringHelper.MISSILES, whiteLabelStyle);
        title.setFontScale(verySmallFontSize);
        final Label level = new Label(LEVEL + game.getMissileLevel(game.getCurrentPlayerType()), whiteLabelStyle);
        level.setFontScale(verySmallFontSize);

        final Image moneyImg = new Image(moneyTexture);

        final MyTextButton upgradeButton = new MyTextButton(StringHelper.UPGRADE + game.getMissileUpgradeCost(game.getCurrentPlayerType()), smallFontSize);
        upgradeButton.add(moneyImg).align(Align.right).space(10);
        upgradeButton.getLabel().setAlignment(Align.center);

        upgradeButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (game.getMissileLevel(game.getCurrentPlayerType()) == 1)
                {

                    upgradeMissile(2, upgradeButton, level);

                }

                else if (game.getMissileLevel(game.getCurrentPlayerType()) == 2)
                {

                    upgradeMissile(3, upgradeButton, level);

                }

                else if (game.getMissileLevel(game.getCurrentPlayerType()) == 3)
                {

                    upgradeMissile(4, upgradeButton, level);

                }

                else if (game.getMissileLevel(game.getCurrentPlayerType()) == 4)
                {

                    upgradeMissile(5, upgradeButton, level);

                }

                else if (game.getMissileLevel(game.getCurrentPlayerType()) == 5)
                {

                    upgradeMissile(6, upgradeButton, level);

                }

                else if (game.getMissileLevel(game.getCurrentPlayerType()) == 6)
                {

                    upgradeMissile(7, upgradeButton, level);

                }

                else if (game.getMissileLevel(game.getCurrentPlayerType()) == 7)
                {

                    upgradeMissile(8, upgradeButton, level);

                }

                else if (game.getMissileLevel(game.getCurrentPlayerType()) == 8)
                {

                    upgradeMissile(9, upgradeButton, level);

                }

                else if (game.getMissileLevel(game.getCurrentPlayerType()) == 9)
                {

                    upgradeMissile(10, upgradeButton, level);

                }

                else if (game.getMissileLevel(game.getCurrentPlayerType()) == 10)
                {
                    new PopupLabel(StringHelper.MAXED_OUT_LC, Color.GREEN, stage);
                }

                super.clicked(event, x, y);
            }
        });

        if (game.getMissileLevel(game.getCurrentPlayerType()) == 10)
        {
            upgradeButton.setVisible(false);
        }

        textVerticalGroup.addActor(title);
        textVerticalGroup.addActor(level);
        textVerticalGroup.addActor(upgradeButton);

        missileHG.addActor(image);
        missileHG.addActor(textVerticalGroup);

        level.setWidth(title.getWidth());
    }

    private void upgradeMG(int newLevel, TextButton upgradeButton, Label levelLabel)
    {
        if (game.getMoney() >= game.getMGUpgradeCost(game.getCurrentPlayerType()))
        {
            int olderUpgradeCost = game.getMGUpgradeCost(game.getCurrentPlayerType());
            game.subtractMoney(game.getMGUpgradeCost(game.getCurrentPlayerType()));
            game.setMGLevel(game.getCurrentPlayerType(), newLevel);
            upgradeButton.setText(UPGRADE + game.getMGUpgradeCost(game.getCurrentPlayerType()));
            System.out.println("MG upgraded to level " + game.getMGLevel(game.getCurrentPlayerType()) + ". Spent " + olderUpgradeCost);

            levelLabel.setText(LEVEL + game.getMGLevel(game.getCurrentPlayerType()));
            moneyLabel.setText(Integer.toString(game.getMoney()));

            new PopupLabel(StringHelper.UPGRADE_SUCCESSFULL, Color.GREEN, stage);
        }

        else
        {
            new PopupLabel(StringHelper.NO_MONEY, Color.RED, stage);
        }
    }

    private void upgradeEngine(int newLevel, TextButton upgradeButton, Label levelLabel)
    {
        if (game.getMoney() >= game.getEngineUpgradeCost(game.getCurrentPlayerType()))
        {
            int olderUpgradeCost = game.getEngineUpgradeCost(game.getCurrentPlayerType());
            game.subtractMoney(game.getEngineUpgradeCost(game.getCurrentPlayerType()));
            game.setEngineLevel(game.getCurrentPlayerType(), newLevel);
            upgradeButton.setText(UPGRADE + game.getEngineUpgradeCost(game.getCurrentPlayerType()));
            System.out.println("Engine upgraded to level " + game.getEngineLevel(game.getCurrentPlayerType()) + ". Spent " + olderUpgradeCost);

            levelLabel.setText(LEVEL + game.getEngineLevel(game.getCurrentPlayerType()));
            moneyLabel.setText(Integer.toString(game.getMoney()));

            new PopupLabel(StringHelper.UPGRADE_SUCCESSFULL, Color.GREEN, stage);
        }

        else
        {
            new PopupLabel(StringHelper.NO_MONEY, Color.RED, stage);
        }
    }

    private void upgradeMissile(int newLevel, TextButton upgradeButton, Label levelLabel)
    {
        if (game.getMoney() >= game.getMissileUpgradeCost(game.getCurrentPlayerType()))
        {
            int olderUpgradeCost = game.getMissileUpgradeCost(game.getCurrentPlayerType());
            game.subtractMoney(game.getMissileUpgradeCost(game.getCurrentPlayerType()));
            game.setMissileLevel(game.getCurrentPlayerType(), newLevel);
            upgradeButton.setText(UPGRADE + game.getMissileUpgradeCost(game.getCurrentPlayerType()));
            System.out.println("Missiles upgraded to level " + game.getMissileLevel(game.getCurrentPlayerType()) + ". Spent " + olderUpgradeCost);

            levelLabel.setText(LEVEL + game.getMissileLevel(game.getCurrentPlayerType()));
            moneyLabel.setText(Integer.toString(game.getMoney()));

            new PopupLabel(StringHelper.UPGRADE_SUCCESSFULL, Color.GREEN, stage);
        }

        else
        {
            new PopupLabel(StringHelper.NO_MONEY, Color.RED, stage);
        }
    }

    private void initBackButton()
    {
        backButton = new MyTextButton(StringHelper.BACK, smallFontSize);
        backButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new HangarScreen(game));
                super.clicked(event, x, y);
            }
        });
    }

    private void initMoneyHG()
    {
        moneyImage = new Image(moneyTexture);

        moneyLabel = new Label(Integer.toString(game.getMoney()), whiteLabelStyle);
        moneyLabel.setFontScale(verySmallFontSize);

        moneyHG = new HorizontalGroup();
        moneyHG.space(10);
        moneyHG.addActor(moneyLabel);
        moneyHG.addActor(moneyImage);
    }

    private void initTitle()
    {
        titleLabel = new Label("UPGRADES FOR", whiteLabelStyle);
        titleLabel.setFontScale(normalFontSize);

        currentPlaneLabel = new Label(game.getCurrentPlayerTypeString(), orangeLabelStyle);
        currentPlaneLabel.setFontScale(normalFontSize);
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);

        spriteBatch.begin();

        spriteBatch.draw(bgTexture, 0, 0);

        spriteBatch.end();

        stage.draw();
    }

    @Override
    public void dispose()
    {
        bgTexture.dispose();
        moneyTexture.dispose();
        mgTexture.dispose();
        engineTexture.dispose();
        missileTexture.dispose();

        super.dispose();
    }
}

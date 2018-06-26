package pl.baftek.spitfire.screens;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import pl.baftek.spitfire.enums.BoostType;
import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.game.StringHelper;
import pl.baftek.spitfire.ui.MyTextButton;

import static pl.baftek.spitfire.game.StringHelper.DESTROYED_ENEMIES;
import static pl.baftek.spitfire.game.StringHelper.HIGH_SCORE;
import static pl.baftek.spitfire.game.StringHelper.MONEY_EARNED;
import static pl.baftek.spitfire.game.StringHelper.SCORE;

public class ProfileScreen extends AbstractScreen
{
    private MyTextButton menuButton;

    private HorizontalGroup upHG;
    private HorizontalGroup containerHG;
    private VerticalGroup equipmentVG;
    private VerticalGroup leftGroup;
    private VerticalGroup rightGroup;

    private Label xpLabel;
    private Label statsLabel;

    ProfileScreen(SpitfireGame game)
    {
        super(game);
    }

    @Override
    protected void init()
    {

    }

    @Override
    protected void buildUI()
    {
        Table table = new Table();
        containerHG = new HorizontalGroup();
        containerHG.space(15);

        initUpHorizontalGroup();
        initXpLabel();
        initLeftGroup();
        initRightGroup();
        initEquipmentGroup();
        initMenuButton();

        containerHG.addActor(leftGroup);
        containerHG.addActor(rightGroup);

        table.add(upHG).padBottom(30).row();
        table.add(xpLabel).padBottom(30).row();
        table.add(statsLabel).padBottom(30).row();
        table.add(containerHG).padBottom(50).row();
        table.add(equipmentVG).padBottom(150).row();
        table.add(menuButton).padBottom(50).row();
        table.setFillParent(true);
        stage.addActor(table);
    }

    private void initEquipmentGroup()
    {
        equipmentVG = new VerticalGroup();
        equipmentVG.space(20);
        equipmentVG.setDebug(true);

        HorizontalGroup currentEquipmentVG = new HorizontalGroup();
        currentEquipmentVG.space(10);

        HorizontalGroup nextLevelRewardHG = new HorizontalGroup();
        nextLevelRewardHG.space(20);

        Label nextLevelRewardLabel = new Label("Next level reward", whiteLabelStyle);
        nextLevelRewardLabel.setFontScale(verySmallFontSize);

        Label rewardLabel = new Label(SpitfireGame.getNextLevelReward().toString(), whiteLabelStyle);
        rewardLabel.setFontScale(verySmallFontSize);

        if (SpitfireGame.getNextLevelReward() == BoostType.MG_BOOST)
        {
            rewardLabel.setText(StringHelper.MG_BOOST);
            rewardLabel.setStyle(yellowLabelStyle);
        } else if (SpitfireGame.getNextLevelReward() == BoostType.NUKE)
        {
            rewardLabel.setText(StringHelper.NUKE);
            rewardLabel.setStyle(redLabelStyle);
        } else if (SpitfireGame.getNextLevelReward() == BoostType.ENGINE_BOOST)
        {
            rewardLabel.setText(StringHelper.ENGINE_BOOST);
            rewardLabel.setStyle(blueLabelStyle);
        }

        Label currentEquipmentLabel = new Label("Equipment", whiteLabelStyle);
        currentEquipmentLabel.setFontScale(mediumFontSize);

        Label mgLabel = new Label("MG BOOSTS " + SpitfireGame.getDefaultMGBoostsCount(), yellowLabelStyle);
        mgLabel.setFontScale(ultraSmallFontSize);

        Label nukeLabel = new Label("NUKES " + SpitfireGame.getDefaultNukeCount(), redLabelStyle);
        nukeLabel.setFontScale(ultraSmallFontSize);

        Label engineLabel = new Label("ENGINE BOOSTS " + SpitfireGame.getDefaultEngineBoostsCount(), blueLabelStyle);
        engineLabel.setFontScale(ultraSmallFontSize);

        nextLevelRewardHG.addActor(nextLevelRewardLabel);
        nextLevelRewardHG.addActor(rewardLabel);

        currentEquipmentVG.addActor(mgLabel);
        currentEquipmentVG.addActor(nukeLabel);
        currentEquipmentVG.addActor(engineLabel);

        equipmentVG.addActor(currentEquipmentLabel);
        equipmentVG.addActor(currentEquipmentVG);

        if (game.getPlayerLevel() < SpitfireGame.LEVEL_FULLY_EQUIPPED)
        {
            equipmentVG.addActor(nextLevelRewardHG);
        }
    }

    private void initXpLabel()
    {
        String longString = Integer.toString(game.getXp()) + " / " + Integer.toString(game.getXpForNextLevel()) + " XP";

        xpLabel = new Label(longString, greenLabelStyle);
        xpLabel.setFontScale(smallFontSize);

        statsLabel = new Label("Your stats", whiteLabelStyle);
        statsLabel.setFontScale(mediumFontSize);
    }

    private void initLeftGroup()
    {
        leftGroup = new VerticalGroup();
        leftGroup.columnAlign(Align.left);
        leftGroup.space(40);

        Label highScoreLabel = new Label(HIGH_SCORE, whiteLabelStyle);
        highScoreLabel.setFontScale(smallFontSize);

        Label scoreLabel = new Label(SCORE, whiteLabelStyle);
        scoreLabel.setFontScale(smallFontSize);

        Label destroyedEnemiesLabel = new Label(DESTROYED_ENEMIES, whiteLabelStyle);
        destroyedEnemiesLabel.setFontScale(smallFontSize);

        Label moneyEarnedLabel = new Label(MONEY_EARNED, whiteLabelStyle);
        moneyEarnedLabel.setFontScale(smallFontSize);

        leftGroup.addActor(highScoreLabel);
        leftGroup.addActor(scoreLabel);
        leftGroup.addActor(destroyedEnemiesLabel);
        leftGroup.addActor(moneyEarnedLabel);
    }

    private void initRightGroup()
    {
        rightGroup = new VerticalGroup();
        rightGroup.columnAlign(Align.left);
        rightGroup.space(40);

        Label highScoreCountLabel = new Label(Integer.toString(game.getHighScore()), whiteLabelStyle);
        highScoreCountLabel.setFontScale(smallFontSize);

        Label scoreCountLabel = new Label(Integer.toString(game.getScore()), whiteLabelStyle);
        scoreCountLabel.setFontScale(smallFontSize);

        Label destroyedEnemiesCountLabel = new Label(Integer.toString(game.getDestroyedEnemies()), whiteLabelStyle);
        destroyedEnemiesCountLabel.setFontScale(smallFontSize);

        Label moneyEarnedCountLabel = new Label(Integer.toString(game.getEarnedMoney()), whiteLabelStyle);
        moneyEarnedCountLabel.setFontScale(smallFontSize);

        rightGroup.addActor(highScoreCountLabel);
        rightGroup.addActor(scoreCountLabel);
        rightGroup.addActor(destroyedEnemiesCountLabel);
        rightGroup.addActor(moneyEarnedCountLabel);
    }

    private void initUpHorizontalGroup()
    {
        upHG = new HorizontalGroup();
        upHG.space(40);

        String playerName = SpitfireGame.gameServiceClient.getPlayerDisplayName();

        Label textLabel = new Label(playerName, orangeLabelStyle);
        textLabel.setFontScale(mediumFontSize);

        Label countLabel = new Label("LVL " + Integer.toString(game.getPlayerLevel()), greenLabelStyle);
        countLabel.setFontScale(normalFontSize);

        upHG.addActor(textLabel);
        upHG.addActor(countLabel);

    }

    private void initMenuButton()
    {
        menuButton = new MyTextButton(StringHelper.GO_TO_MENU, smallFontSize);
        menuButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new MenuScreen(game));
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);
        stage.draw();
    }
}

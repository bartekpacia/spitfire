package pl.baftek.spitfire.screens;


import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.blueLabelStyle;
import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.greenLabelStyle;
import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.orangeLabelStyle;
import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.redLabelStyle;
import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.whiteLabelStyle;
import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.yellowLabelStyle;
import static pl.baftek.spitfire.game.StringHelper.HIGH_SCORE;
import static pl.baftek.spitfire.game.StringHelper.SCORE;

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

public class ProfileScreen extends AbstractScreen {
    private MyTextButton menuButton;

    private VerticalGroup upVG;
    private HorizontalGroup statsContainerHG;
    private VerticalGroup equipmentVG;
    private VerticalGroup leftGroup;
    private VerticalGroup rightGroup;

    private Label xpLabel;
    private Label statsLabel;

    ProfileScreen(SpitfireGame game) {
        super(game);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void buildUI() {
        Table table = new Table();
        statsContainerHG = new HorizontalGroup();
        statsContainerHG.space(15);

        initUpHorizontalGroup();
        initXpLabel();
        initLeftGroup();
        initRightGroup();
        initEquipmentGroup();
        initMenuButton();

        statsContainerHG.addActor(leftGroup);
        statsContainerHG.addActor(rightGroup);

        table.add(upVG).row();
        table.add(xpLabel).padBottom(30).row();
        table.add(statsLabel).padBottom(30).row();
        table.add(statsContainerHG).padBottom(50).row();
        table.add(equipmentVG).padBottom(150).row();
        table.add(menuButton).padBottom(50).row();
        table.setFillParent(true);
        stage.addActor(table);
    }

    private void initEquipmentGroup() {
        equipmentVG = new VerticalGroup();
        equipmentVG.space(20);
        equipmentVG.setDebug(true);

        HorizontalGroup currentEquipmentVG = new HorizontalGroup();
        currentEquipmentVG.space(10);

        HorizontalGroup nextLevelRewardHG = new HorizontalGroup();
        nextLevelRewardHG.space(20);

        Label nextLevelRewardLabel = new Label("Next level reward", whiteLabelStyle);
        nextLevelRewardLabel.setFontScale(FONT_SIZE_2);

        Label rewardLabel = new Label(game.playerManager.getNextLevelReward().toString(), whiteLabelStyle);
        rewardLabel.setFontScale(FONT_SIZE_2);

        if (game.playerManager.getNextLevelReward() == BoostType.MG_BOOST) {
            rewardLabel.setText(StringHelper.MG_BOOST);
            rewardLabel.setStyle(yellowLabelStyle);
        } else if (game.playerManager.getNextLevelReward() == BoostType.NUKE) {
            rewardLabel.setText(StringHelper.NUKE);
            rewardLabel.setStyle(redLabelStyle);
        } else if (game.playerManager.getNextLevelReward() == BoostType.ENGINE_BOOST) {
            rewardLabel.setText(StringHelper.ENGINE_BOOST);
            rewardLabel.setStyle(blueLabelStyle);
        }

        Label currentEquipmentLabel = new Label("Equipment", whiteLabelStyle);
        currentEquipmentLabel.setFontScale(FONT_SIZE_4);

        Label mgLabel = new Label("MG BOOSTS " + game.playerManager.getMgBoosts(), yellowLabelStyle);
        mgLabel.setFontScale(FONT_SIZE_1);

        Label nukeLabel = new Label("NUKES " + game.playerManager.getNukes(), redLabelStyle);
        nukeLabel.setFontScale(FONT_SIZE_1);

        Label engineLabel = new Label("ENGINE BOOSTS " + game.playerManager.getEngineBoosts(), blueLabelStyle);
        engineLabel.setFontScale(FONT_SIZE_1);

        nextLevelRewardHG.addActor(nextLevelRewardLabel);
        nextLevelRewardHG.addActor(rewardLabel);

        currentEquipmentVG.addActor(mgLabel);
        currentEquipmentVG.addActor(nukeLabel);
        currentEquipmentVG.addActor(engineLabel);

        equipmentVG.addActor(currentEquipmentLabel);
        equipmentVG.addActor(currentEquipmentVG);

        if (game.playerManager.getLevel() < SpitfireGame.LEVEL_FULLY_EQUIPPED) {
            equipmentVG.addActor(nextLevelRewardHG);
        }
    }

    private void initXpLabel() {
        String longString = Integer.toString(game.playerManager.getXp()) + " / " + Integer.toString(game.playerManager.getXpForNextLevel()) + " XP";

        xpLabel = new Label(longString, greenLabelStyle);
        xpLabel.setFontScale(FONT_SIZE_3);

        statsLabel = new Label("Your stats", whiteLabelStyle);
        statsLabel.setFontScale(FONT_SIZE_4);
    }

    private void initLeftGroup() {
        leftGroup = new VerticalGroup();
        leftGroup.columnAlign(Align.left);
        leftGroup.space(40);

        Label highScoreLabel = new Label(HIGH_SCORE, whiteLabelStyle);
        highScoreLabel.setFontScale(FONT_SIZE_3);

        Label scoreLabel = new Label(SCORE, whiteLabelStyle);
        scoreLabel.setFontScale(FONT_SIZE_3);

        leftGroup.addActor(highScoreLabel);
        leftGroup.addActor(scoreLabel);
    }

    private void initRightGroup() {
        rightGroup = new VerticalGroup();
        rightGroup.columnAlign(Align.left);
        rightGroup.space(40);

        Label highScoreCountLabel = new Label(Integer.toString(game.playerManager.getHighScore()), whiteLabelStyle);
        highScoreCountLabel.setFontScale(FONT_SIZE_3);

        Label scoreCountLabel = new Label(Integer.toString(game.playerManager.getScore()), whiteLabelStyle);
        scoreCountLabel.setFontScale(FONT_SIZE_3);

        rightGroup.addActor(highScoreCountLabel);
        rightGroup.addActor(scoreCountLabel);
    }

    private void initUpHorizontalGroup() {
        upVG = new VerticalGroup();
        upVG.space(30);

        String playerName = game.gameServiceClient.getPlayerDisplayName();

        Label textLabel = new Label(playerName, orangeLabelStyle);
        textLabel.setFontScale(FONT_SIZE_4);

        Label countLabel = new Label("LVL " + Integer.toString(game.playerManager.getLevel()), greenLabelStyle);
        countLabel.setFontScale(FONT_SIZE_6);

        upVG.addActor(textLabel);
        upVG.addActor(countLabel);

    }

    private void initMenuButton() {
        menuButton = new MyTextButton(StringHelper.GO_TO_MENU, FONT_SIZE_3);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }
}

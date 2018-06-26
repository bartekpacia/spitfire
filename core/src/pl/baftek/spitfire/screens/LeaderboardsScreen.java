package pl.baftek.spitfire.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.golfgl.gdxgamesvcs.GameServiceException;
import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.ui.MyTextButton;

import static pl.baftek.spitfire.game.StringHelper.GO_TO_MENU;
import static pl.baftek.spitfire.game.StringHelper.HIGH_SCORE;
import static pl.baftek.spitfire.game.StringHelper.LEADERBOARD_HIGH_SCORE;
import static pl.baftek.spitfire.game.StringHelper.LEADERBOARD_SCORE;
import static pl.baftek.spitfire.game.StringHelper.SCORE;

public class LeaderboardsScreen extends AbstractScreen
{
    LeaderboardsScreen(SpitfireGame game)
    {
        super(game);
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);
        stage.draw();
    }

    @Override
    protected void init()
    {

    }

    @Override
    protected void buildUI()
    {
        //screen label
        Label leaderboardsLabel = new Label("Which \nleaderboard?", whiteLabelStyle);
        leaderboardsLabel.setFontScale(0.7f);

        //buttons
        MyTextButton highScoreButton = new MyTextButton(HIGH_SCORE, normalFontSize);
        highScoreButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                try
                {
                    SpitfireGame.gameServiceClient.showLeaderboards(LEADERBOARD_HIGH_SCORE);
                } catch (GameServiceException e)
                {
                    e.printStackTrace();
                }
                super.clicked(event, x, y);
            }
        });

        MyTextButton scoreButton = new MyTextButton(SCORE, normalFontSize);
        scoreButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                try
                {
                    SpitfireGame.gameServiceClient.showLeaderboards(LEADERBOARD_SCORE);
                } catch (GameServiceException e)
                {
                    e.printStackTrace();
                }
                super.clicked(event, x, y);
            }
        });

        MyTextButton menuButton = new MyTextButton(GO_TO_MENU, normalFontSize);
        menuButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new MenuScreen(game));
                super.clicked(event, x, y);
            }
        });

        //adding to table
        Table table = new Table();

        table.add(leaderboardsLabel).padBottom(70).row();
        table.add(highScoreButton).padBottom(40).row();
        table.add(scoreButton).padBottom(200).row();
        table.add(menuButton).row();
        table.setFillParent(true);
        stage.addActor(table);
    }
}
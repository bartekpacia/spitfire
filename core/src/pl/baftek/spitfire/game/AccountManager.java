package pl.baftek.spitfire.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import pl.baftek.spitfire.entities.Player;
import pl.baftek.spitfire.enums.BoostType;

import static pl.baftek.spitfire.game.StringHelper.LEADERBOARD_HIGH_SCORE;
import static pl.baftek.spitfire.game.StringHelper.LEADERBOARD_SCORE;

public class AccountManager
{
    private static final String TAG = "AccountManager";

    private SpitfireGame game;
    private Preferences preferences;
    private LevelHelper levelHelper;

    //preferences
    private static final String GAME_PREFS = "pl.baftek.spitfire.preferences";
    private static final String MONEY_PREFS = "pl.baftek.spitfire.preferences.money";
    private static final String XP_PREFS = "pl.baftek.spitfire.preferences.xp";
    private static final String SCORE_PREFS = "pl.baftek.spitfire.preferences.alltimescore";
    private static final String HIGH_SCORE_PREFS = "pl.baftek.spitfire.preferences.score";

    //statistics
    private int money;
    private int xp;
    private int score;
    private int highScore;
    private int level;

    //boosts on gameplay
    private int mgBoosts;
    private int nukes;
    private int engineBoosts;

    //other
    private int xpForNextLevel;

    private BoostType NEXT_LEVEL_REWARD;

    AccountManager(SpitfireGame game)
    {
        this.game = game;
        preferences = Gdx.app.getPreferences(GAME_PREFS);
        levelHelper = new LevelHelper();

        reloadStats();
    }

    private void reloadStats()
    {
        money = preferences.getInteger(MONEY_PREFS);
        xp = preferences.getInteger(XP_PREFS);
        score = preferences.getInteger(SCORE_PREFS);
        highScore = preferences.getInteger(HIGH_SCORE_PREFS);

        level = levelHelper.getLevel();
    }

    public int getMoney()
    {
        return money;
    }

    public void addMoney(int addedMoney)
    {
        money = money + addedMoney;
        preferences.putInteger(MONEY_PREFS, money);
        preferences.flush();
        reloadStats();
    }

    public void subtractMoney(int subtractedMoney)
    {
        money = money - subtractedMoney;
        preferences.putInteger(MONEY_PREFS, money);
        preferences.flush();
        reloadStats();
    }

    public int getXp()
    {
        return xp;
    }

    public void addXp(int xpToAdd)
    {
        xp = xp + xpToAdd;
        preferences.putInteger(XP_PREFS, xp);
        preferences.flush();
        reloadStats();

        Gdx.app.log(TAG, "Added " + xpToAdd + " XP. Now XP = " + xp);
    }

    public int getScore()
    {
        return score;
    }

    public void addScore(int scoreToAdd)
    {
        score = score + scoreToAdd;
        preferences.putInteger(SCORE_PREFS, score);
        preferences.flush();
        reloadStats();

        game.gameServiceClient.submitToLeaderboard(LEADERBOARD_SCORE, score, null);
    }

    public int getHighScore()
    {
        return highScore;
    }

    public void setHighScore(int newHighScore)
    {
        if (newHighScore > highScore)
        {
            highScore = newHighScore;
            preferences.putInteger(HIGH_SCORE_PREFS, highScore);
            preferences.flush();
            reloadStats();

            game.gameServiceClient.submitToLeaderboard(LEADERBOARD_HIGH_SCORE, highScore, null);
        }
    }

    public int getLevel()
    {
        return level;
    }

    public int getMgBoosts()
    {
        return mgBoosts;
    }

    public int getNukes()
    {
        return nukes;
    }

    public int getEngineBoosts()
    {
        return engineBoosts;
    }

    public int getXpForNextLevel()
    {
        return xpForNextLevel;
    }

    public BoostType getNextLevelReward()
    {
        return NEXT_LEVEL_REWARD;
    }

    private class LevelHelper
    {
        private static final int XP_LEVEL_2 = 20;
        private static final int XP_LEVEL_3 = 50;
        private static final int XP_LEVEL_4 = 200;
        private static final int XP_LEVEL_5 = 300;
        private static final int XP_LEVEL_6 = 700;
        private static final int XP_LEVEL_7 = 900;
        private static final int XP_LEVEL_8 = 1200;
        private static final int XP_LEVEL_9 = 2400;
        private static final int XP_LEVEL_10 = 4000;
        private static final int XP_LEVEL_11 = 6000;

        private int getLevel()
        {
            if (xp < XP_LEVEL_2)
            {
                xpForNextLevel = XP_LEVEL_2;
                mgBoosts = 1;
                nukes = 0;
                engineBoosts = 1;
                NEXT_LEVEL_REWARD = BoostType.NUKE;

                return 1;
            }
            else if (xp < XP_LEVEL_3)
            {
                xpForNextLevel = XP_LEVEL_3;
                mgBoosts = 1;
                nukes = 1;
                engineBoosts = 1;
                NEXT_LEVEL_REWARD = BoostType.MG_BOOST;

                return 2;
            }
            else if (xp < XP_LEVEL_4)
            {
                xpForNextLevel = XP_LEVEL_4;
                mgBoosts = 2;
                nukes = 1;
                engineBoosts = 1;
                NEXT_LEVEL_REWARD = BoostType.ENGINE_BOOST;

                return 3;
            }
            else if (xp < XP_LEVEL_5)
            {
                xpForNextLevel = XP_LEVEL_5;
                mgBoosts = 2;
                nukes = 1;
                engineBoosts = 2;
                NEXT_LEVEL_REWARD = BoostType.MG_BOOST;

                return 4;
            }
            else if (xp < XP_LEVEL_6)
            {
                xpForNextLevel = XP_LEVEL_6;
                mgBoosts = 3;
                nukes = 1;
                engineBoosts = 2;
                NEXT_LEVEL_REWARD = BoostType.ENGINE_BOOST;

                return 5;
            }
            else if (xp < XP_LEVEL_7)
            {
                xpForNextLevel = XP_LEVEL_7;
                mgBoosts = 3;
                nukes = 1;
                engineBoosts = 3;
                NEXT_LEVEL_REWARD = BoostType.NUKE;

                return 6;
            }
            else if (xp < XP_LEVEL_8)
            {
                level = 7;
                xpForNextLevel = XP_LEVEL_8;
                mgBoosts = 3;
                nukes = 2;
                engineBoosts = 3;
                NEXT_LEVEL_REWARD = BoostType.NUKE;

                return 7;
            }
            else if (xp < XP_LEVEL_9)
            {
                xpForNextLevel = XP_LEVEL_9;
                mgBoosts = 3;
                nukes = 3;
                engineBoosts = 3;
                NEXT_LEVEL_REWARD = BoostType.NOTHING;

                return 8;
            }
            else if (xp < XP_LEVEL_10)
            {
                level = 9;
                xpForNextLevel = XP_LEVEL_10;
                mgBoosts = 3;
                nukes = 3;
                engineBoosts = 3;

                NEXT_LEVEL_REWARD = BoostType.NOTHING;

                return 9;
            }
            else if (xp < XP_LEVEL_11)
            {
                level = 10;
                xpForNextLevel = XP_LEVEL_11;
                mgBoosts = 3;
                nukes = 3;
                engineBoosts = 3;
                NEXT_LEVEL_REWARD = BoostType.NOTHING;

                return 10;
            }

            else return 999;
        }
    }
}
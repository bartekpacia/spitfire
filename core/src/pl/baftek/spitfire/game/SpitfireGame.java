package pl.baftek.spitfire.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.kotcrab.vis.ui.VisUI;

import java.util.Calendar;

import de.golfgl.gdxgamesvcs.IGameServiceClient;
import pl.baftek.spitfire.enums.Achievement;
import pl.baftek.spitfire.enums.BoostType;
import pl.baftek.spitfire.enums.GameState;
import pl.baftek.spitfire.enums.PlayerType;
import pl.baftek.spitfire.screens.MenuScreen;
import pl.baftek.spitfire.ui.PopupLabel;

import static pl.baftek.spitfire.enums.Achievement.ACE;
import static pl.baftek.spitfire.enums.Achievement.BIG_BOY;
import static pl.baftek.spitfire.enums.Achievement.CROESUS;
import static pl.baftek.spitfire.enums.Achievement.FIRST_BLOOD;
import static pl.baftek.spitfire.enums.Achievement.RATATA;
import static pl.baftek.spitfire.enums.Achievement.TOP_GEAR;
import static pl.baftek.spitfire.game.StringHelper.ACHIEVEMENT_ACE;
import static pl.baftek.spitfire.game.StringHelper.ACHIEVEMENT_BIGBOY;
import static pl.baftek.spitfire.game.StringHelper.ACHIEVEMENT_CROESUS;
import static pl.baftek.spitfire.game.StringHelper.ACHIEVEMENT_FIRST_BLOOD;
import static pl.baftek.spitfire.game.StringHelper.ACHIEVEMENT_RATATA;
import static pl.baftek.spitfire.game.StringHelper.ACHIEVEMENT_TOP_GEAR;

public class SpitfireGame extends Game
{
    private static final String TAG = "SpitfireGame";
    public static final String TITLE = "Spitfire Game";
    public static final int WIDTH = 720;
    public static final int HEIGHT = 1280;

    public static IGameServiceClient gameServiceClient;
    private static Preferences preferences;
    public static int LEVEL_FULLY_EQUIPPED = 8;

    private Texture spitfireTexture;
    private Texture mustangTexture;
    private Texture IL2texture;

    private int mustangPrice = 750;
    private int IL2price = 1500;

    private static final int UPGRADE_LEVEL_2 = 15;
    private static final int UPGRADE_LEVEL_3 = 30;
    private static final int UPGRADE_LEVEL_4 = 50;
    private static final int UPGRADE_LEVEL_5 = 100;
    private static final int UPGRADE_LEVEL_6 = 150;
    private static final int UPGRADE_LEVEL_7 = 200;
    private static final int UPGRADE_LEVEL_8 = 300;
    private static final int UPGRADE_LEVEL_9 = 500;
    private static final int UPGRADE_LEVEL_10 = 700;

    private static final int XP_LEVEL_2 = 20;
    private static final int XP_LEVEL_3 = 50;
    private static final int XP_LEVEL_4 = 200;
    private static final int XP_LEVEL_5 = 450;
    private static final int XP_LEVEL_6 = 900;
    private static final int XP_LEVEL_7 = 1400;
    private static final int XP_LEVEL_8 = 2200;
    private static final int XP_LEVEL_9 = 3500;
    private static final int XP_LEVEL_10 = 6000;
    private static final int XP_LEVEL_11 = 10000;
    public static final int BONUS_MULTIPLIER = 2;

    private static int xp;
    private static int xpForNextLevel;
    private static int playerLevel;
    private static int destroyedEnemies;
    private static int highScore;
    private static int score;
    private static int money;
    private static int earnedMoney; //all time money!

    private static int defaultNukeCount = 0;
    private static int defaultMGCount = 0;
    private static int defaultEngineCount = 0;
    private static BoostType NEXT_LEVEL_REWARD;

    private static final String GAME_PREFS = "pl.baftek.spitfire.preferences";
    private static final String HIGH_SCORE_PREFS = "pl.baftek.spitfire.preferences.score";
    private static final String XP_PREFS = "pl.baftek.spitfire.preferences.xp";
    private static final String SCORE_PREFS = "pl.baftek.spitfire.preferences.alltimescore";
    private static final String DESTROYED_ENEMIES = "pl.baftek.spitfire.enemiesdestroyed";
    private static final String MONEY_PREFS = "pl.baftek.spitfire.preferences.money";
    private static final String EARNED_MONEY_PREFS = "pl.baftek.spitfire.earnedmoney";

    private static final String SPITFIRE_MG_LEVEL_PREFS = "pl.baftek.spitfire.preferences.spitfire.mglevel";
    private static final String SPITFIRE_ENGINE_LEVEL_PREFS = "pl.baftek.spitfire.preferences.spitfire.enginelevel";

    private static final String MUSTANG_MG_LEVEL_PREFS = "pl.baftek.spitfire.preferences.mustang.mglevel";
    private static final String MUSTANG_ENGINE_LEVEL_PREFS = "pl.baftek.spitfire.preferences.mustang.enginelevel";

    private static final String IL2_MG_LEVEL_PREFS = "pl.baftek.spitfire.preferences.il2.mglevel";
    private static final String IL2_ENGINE_LEVEL_PREFS = "pl.baftek.spitfire.preferences.il2.enginelevel";
    private static final String IL2_MISSILE_LEVEL_PREFS = "pl.baftek.spitfire.preferences.il2.missilelevel";

    private static final String SPITFIRE_BOUGHT_PREFS = "pl.baftek.spitfire.preferences.spitfirebought";
    private static final String MUSTANG_BOUGHT_PREFS = "pl.baftek.spitfire.preferences.mustangbought";
    private static final String IL2_BOUGHT_PREFS = "pl.baftek.spitfire.preferences.il2bought";

    private static final String CURRENT_PLANE = "pl.baftek.spitfire.preferences.currentplane";

    private static final String SOUND_PREFS = "pl.baftek.spitfire.preferences.sound";
    private static final String FIRST_BLOOD_UNLOCKED_PREFS = "pl.baftek.spitfire.preferences.achievements.firstblood";
    private static final String ACE_UNLOCKED_PREFS = "pl.baftek.spitfire.preferences.achievements.ace";
    private static final String CROESUS_UNLOCKED_PREFS = "pl.baftek.spitfire.preferences.achievements.croesus";
    private static final String RATATA_UNLOCKED_PREFS = "pl.baftek.spitfire.preferences.achievements.ratata";
    private static final String BIG_BOY_UNLOCKED_PREFS = "pl.baftek.spitfire.preferences.achievements.bigboy";
    private static final String TOP_GEAR_UNLOCKED_PREFS = "pl.baftek.spitfire.preferences.achievements.topgear";

    private static final String DATE_PREFS = "pl.baftek.spitfire.preferences.lastday";
    private static final String DAILY_BONUS_PREFS = "pl.baftek.spitfire.preferences.dailybonus";
    private static boolean dailyBonusAvailability;

    private boolean spitfireBought;
    private boolean mustangBought;
    private boolean il2bought;

    private static float spitfireMGShootCooldown;
    private static int spitfireMGUpgradeCost;
    private static int spitfireEngineSpeed;
    private static int spitfireEngineUpgradeCost;

    private static float mustangMGShootCooldown;
    private static int mustangMGUpgradeCost;
    private static int mustangEngineSpeed;
    private static int mustangEngineUpgradeCost;

    private static float IL2MGshootCooldown;
    private static int IL2MGupgradeCost;
    private static int IL2engineSpeed;
    private static int IL2engineUpgradeCost;
    private static float IL2missileShootCooldown;
    private static int IL2missileUpgradeCost;

    private static GameState gameState;
    private static boolean soundEnabled;

    public PlayerType currentPlayerType;
    public String currentPlayerTypeString;
    public BitmapFont font;

    private static final int SPITFIRE_AVAILABILITY_LEVEL = 1;
    private static final int MUSTANG_AVAILABILITY_LEVEL = 3;
    private static final int IL2_AVAILABILITY_LEVEL = 6;

    public SpitfireGame(IGameServiceClient gameServiceClient)
    {
        SpitfireGame.gameServiceClient = gameServiceClient;
    }

    @Override
    public void create()
    {
        init();

        this.setScreen(new MenuScreen(this));
    }

    private void init()
    {
        preferences = Gdx.app.getPreferences(GAME_PREFS);

        load();

        spitfireTexture = new Texture("spitfire.png");
        mustangTexture = new Texture("mustang.png");
        IL2texture = new Texture("il2.png");
    }

    private void load()
    {
        loadHighScore();
        loadScore();
        loadMoney();
        loadAllTimeMoney();
        loadDestroyedEnemies();
        loadSoundEnabled();
        loadXP();
        loadPlayerLevel();
        loadBoughtPlanes();
        loadDate();
        loadDailyBonus();
        refreshCurrentPlayerType();

        loadSpitfireStats();
        loadMustangStats();

        loadIL2Stats();

        VisUI.load();
        createFont();

        if (gameServiceClient != null)
        {
            Timer timer = new Timer();
            timer.scheduleTask(new Timer.Task()
            {
                @Override
                public void run()
                {
                    Gdx.app.log(TAG, "Player: " + gameServiceClient.getPlayerDisplayName());
                }
            }, 0.5f);
        }
    }

    private static void loadDailyBonus()
    {
        dailyBonusAvailability = preferences.getBoolean(DAILY_BONUS_PREFS);
    }

    private void loadDate()
    {
        Calendar calendar = Calendar.getInstance();

        int last = preferences.getInteger(DATE_PREFS);
        int today = calendar.get(Calendar.DAY_OF_YEAR);

        //Gdx.app.log(TAG, "PRE-CHANGES Last day: " + last + " Today: " + today + " Bonus available: " + dailyBonusAvailability);

        //only on startup
        if (last == 0)
        {
            last = calendar.get(Calendar.DAY_OF_YEAR);
            preferences.putInteger(DATE_PREFS, last);
            preferences.flush();
        }

        //new day
        if (today != last)
        {
            dailyBonusAvailability = true;
            preferences.putBoolean(DAILY_BONUS_PREFS, dailyBonusAvailability);
            last = today;
            preferences.putInteger(DATE_PREFS, last);
            preferences.flush();
        }

        loadDailyBonus();

        //Gdx.app.log(TAG, "AFTER-CHANGES Last day: " + last + " Today: " + today + " Bonus available: " + dailyBonusAvailability);
    }

    public static boolean isDailyBonusAvailable()
    {
        return dailyBonusAvailability;
    }

    public static void setDailyBonusAvailability(boolean dailyBonusAvailability)
    {
        preferences.putBoolean(DAILY_BONUS_PREFS, dailyBonusAvailability);
        preferences.flush();
        loadDailyBonus();
    }

    private void loadPlayerLevel()
    {
        if (xp < XP_LEVEL_2)
        {
            playerLevel = 1;
            xpForNextLevel = XP_LEVEL_2;

            defaultMGCount = 1;
            defaultNukeCount = 0;
            defaultEngineCount = 1;

            NEXT_LEVEL_REWARD = BoostType.NUKE;
        } else if (xp < XP_LEVEL_3)
        {
            playerLevel = 2;
            xpForNextLevel = XP_LEVEL_3;

            defaultMGCount = 1;
            defaultNukeCount = 1;
            defaultEngineCount = 1;

            NEXT_LEVEL_REWARD = BoostType.MG_BOOST;
        } else if (xp < XP_LEVEL_4)
        {
            playerLevel = 3;
            xpForNextLevel = XP_LEVEL_4;

            defaultMGCount = 2;
            defaultNukeCount = 1;
            defaultEngineCount = 1;

            NEXT_LEVEL_REWARD = BoostType.ENGINE_BOOST;
        } else if (xp < XP_LEVEL_5)
        {
            playerLevel = 4;
            xpForNextLevel = XP_LEVEL_5;

            defaultMGCount = 2;
            defaultNukeCount = 1;
            defaultEngineCount = 2;

            NEXT_LEVEL_REWARD = BoostType.MG_BOOST;
        } else if (xp < XP_LEVEL_6)
        {
            playerLevel = 5;
            xpForNextLevel = XP_LEVEL_6;

            defaultMGCount = 3;
            defaultNukeCount = 1;
            defaultEngineCount = 2;

            NEXT_LEVEL_REWARD = BoostType.ENGINE_BOOST;
        } else if (xp < XP_LEVEL_7)
        {
            playerLevel = 6;
            xpForNextLevel = XP_LEVEL_7;

            defaultMGCount = 3;
            defaultNukeCount = 1;
            defaultEngineCount = 3;

            NEXT_LEVEL_REWARD = BoostType.NUKE;
        } else if (xp < XP_LEVEL_8)
        {
            playerLevel = 7;
            xpForNextLevel = XP_LEVEL_8;

            defaultMGCount = 3;
            defaultNukeCount = 2;
            defaultEngineCount = 3;

            NEXT_LEVEL_REWARD = BoostType.NUKE;
        } else if (xp < XP_LEVEL_9)
        {
            playerLevel = 8;
            xpForNextLevel = XP_LEVEL_9;

            defaultMGCount = 3;
            defaultNukeCount = 3;
            defaultEngineCount = 3;

            NEXT_LEVEL_REWARD = BoostType.NOTHING;
        } else if (xp < XP_LEVEL_10)
        {
            playerLevel = 9;
            xpForNextLevel = XP_LEVEL_10;

            defaultMGCount = 3;
            defaultNukeCount = 3;
            defaultEngineCount = 3;

            NEXT_LEVEL_REWARD = BoostType.NOTHING;
        } else if (xp < XP_LEVEL_11)
        {
            playerLevel = 10;
            xpForNextLevel = XP_LEVEL_11;

            defaultMGCount = 3;
            defaultNukeCount = 3;
            defaultEngineCount = 3;

            NEXT_LEVEL_REWARD = BoostType.NOTHING;
        }
    }

    private void loadXP()
    {
        xp = preferences.getInteger(XP_PREFS);
        Gdx.app.log("XP", Integer.toString(xp));
    }

    public void buyPlane(PlayerType playerType, Stage stage)
    {
        Gdx.app.log(TAG, "buyPlane() called!");

        switch (playerType)
        {
            case MUSTANG:
            {
                Gdx.app.log(TAG, "case mustang");

                if (getPlayerLevel() >= getCurrentPlaneAvailabilityLevel(PlayerType.MUSTANG))
                {
                    Gdx.app.log(TAG, "player level is ok");

                    if (getMoney() >= mustangPrice)
                    {
                        mustangBought = true;
                        preferences.putBoolean(MUSTANG_BOUGHT_PREFS, mustangBought);
                        money = money - mustangPrice;
                        preferences.putInteger(MONEY_PREFS, money);
                        preferences.flush();
                        Gdx.app.log(TAG, "money is ok, mustang should be bought");
                        new PopupLabel(StringHelper.BOUGHT_SUCCESSFULLY, Color.GREEN, stage);
                    } else
                    {
                        new PopupLabel(StringHelper.NO_MONEY, Color.RED, stage);
                    }
                } else
                {
                    new PopupLabel(StringHelper.TOO_LOW_LEVEL, Color.RED, stage);
                }

                break;
            }

            case IL2:
            {
                Gdx.app.log(TAG, "case il2");

                if (getPlayerLevel() >= getCurrentPlaneAvailabilityLevel(PlayerType.IL2))
                {
                    Gdx.app.log(TAG, "player level is ok");

                    if (getMoney() >= IL2price)
                    {
                        il2bought = true;
                        preferences.putBoolean(IL2_BOUGHT_PREFS, il2bought);
                        money = money - IL2price;
                        preferences.putInteger(MONEY_PREFS, money);
                        preferences.flush();
                        Gdx.app.log(TAG, "money is ok, il2m should be bought");

                        new PopupLabel(StringHelper.BOUGHT_SUCCESSFULLY, Color.GREEN, stage);
                    } else
                    {
                        new PopupLabel(StringHelper.NO_MONEY, Color.RED, stage);
                    }
                } else
                {
                    new PopupLabel(StringHelper.TOO_LOW_LEVEL, Color.RED, stage);
                }

                break;
            }
        }
    }

    private void loadBoughtPlanes()
    {
        preferences.putBoolean(SPITFIRE_BOUGHT_PREFS, true);
        preferences.flush();

        spitfireBought = preferences.getBoolean(SPITFIRE_BOUGHT_PREFS);
        mustangBought = preferences.getBoolean(MUSTANG_BOUGHT_PREFS);
        il2bought = preferences.getBoolean(IL2_BOUGHT_PREFS);

        Gdx.app.log(TAG, "Spitfire bought " + spitfireBought + "; Mustang bought " + mustangBought + "; IL2 bought " + il2bought);
    }

    public static void playSound(Sound sound, float volume)
    {
        if (soundEnabled)
        {
            //Gdx.app.log("playSound()", "playing " + sound.toString());
            sound.play(volume);
        }
    }

    private void refreshCurrentPlayerType()
    {
        if (preferences.getString(CURRENT_PLANE).isEmpty() || preferences.getString(CURRENT_PLANE).equals(StringHelper.SPITIFRE))
        {
            currentPlayerType = PlayerType.SPITFIRE;
            currentPlayerTypeString = StringHelper.SPITIFRE;
            preferences.putString(CURRENT_PLANE, StringHelper.SPITIFRE);
            preferences.flush();
        } else if (preferences.getString(CURRENT_PLANE).equals(StringHelper.MUSTANG))
        {
            currentPlayerType = PlayerType.MUSTANG;
            currentPlayerTypeString = StringHelper.MUSTANG;
        } else if (preferences.getString(CURRENT_PLANE).equals(StringHelper.IL2))
        {
            currentPlayerType = PlayerType.IL2;
            currentPlayerTypeString = StringHelper.IL2;
        }

        Gdx.app.log(TAG, "refreshCurrentPlayerType(): current plane enum: " + currentPlayerType);
        Gdx.app.log(TAG, "refreshCurrentPlayerType(): current plane string: " + preferences.getString(CURRENT_PLANE));
    }

    private void loadMustangStats()
    {
        /*
         *MG
         */
        if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 0)
        {
            preferences.putInteger(MUSTANG_MG_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 1)
        {
            mustangMGShootCooldown = 0.6f;
            mustangMGUpgradeCost = 40;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 2)
        {
            mustangMGShootCooldown = 0.55f;
            mustangMGUpgradeCost = 100;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 3)
        {
            mustangMGShootCooldown = 0.52f;
            mustangMGUpgradeCost = 200;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 4)
        {
            mustangMGShootCooldown = 0.5f;
            mustangMGUpgradeCost = 300;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 5)
        {
            mustangMGShootCooldown = 0.48f;
            mustangMGUpgradeCost = 400;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 6)
        {
            mustangMGShootCooldown = 0.46f;
            mustangMGUpgradeCost = 450;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 7)
        {
            mustangMGShootCooldown = 0.45f;
            mustangMGUpgradeCost = 500;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 8)
        {
            mustangMGShootCooldown = 0.44f;
            mustangMGUpgradeCost = 650;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 9)
        {
            mustangMGShootCooldown = 0.42f;
            mustangMGUpgradeCost = 750;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 10)
        {
            mustangMGShootCooldown = 0.4f;
            mustangMGUpgradeCost = 900;
        }

        /*
         *ENGINE
         */

        if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 0)
        {
            preferences.putInteger(MUSTANG_ENGINE_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 1)
        {
            mustangEngineSpeed = 460;
            mustangEngineUpgradeCost = 40;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 2)
        {
            mustangEngineSpeed = 480;
            mustangEngineUpgradeCost = 80;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 3)
        {
            mustangEngineSpeed = 500;
            mustangEngineUpgradeCost = 80;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 4)
        {
            mustangEngineSpeed = 520;
            mustangEngineUpgradeCost = 180;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 5)
        {
            mustangEngineSpeed = 545;
            mustangEngineUpgradeCost = 300;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 6)
        {
            mustangEngineSpeed = 580;
            mustangEngineUpgradeCost = 400;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 7)
        {
            mustangEngineSpeed = 610;
            mustangEngineUpgradeCost = 500;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 8)
        {
            mustangEngineSpeed = 640;
            mustangEngineUpgradeCost = 600;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 9)
        {
            mustangEngineSpeed = 670;
            mustangEngineUpgradeCost = 700;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 10)
        {
            mustangEngineSpeed = 700;
            mustangEngineUpgradeCost = 800;
        }
    }

    private void loadSpitfireStats()
    {
        /*
         *MG
         */
        if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 0)
        {
            preferences.putInteger(SPITFIRE_MG_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 1)
        {
            spitfireMGShootCooldown = 0.48f;
            spitfireMGUpgradeCost = 10;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 2)
        {
            spitfireMGShootCooldown = 0.44f;
            spitfireMGUpgradeCost = 20;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 3)
        {
            spitfireMGShootCooldown = 0.41f;
            spitfireMGUpgradeCost = 50;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 4)
        {
            spitfireMGShootCooldown = 0.38f;
            spitfireMGUpgradeCost = 100;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 5)
        {
            spitfireMGShootCooldown = 0.36f;
            spitfireMGUpgradeCost = 150;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 6)
        {
            spitfireMGShootCooldown = 0.34f;
            spitfireMGUpgradeCost = 200;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 7)
        {
            spitfireMGShootCooldown = 0.32f;
            spitfireMGUpgradeCost = 265;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 8)
        {
            spitfireMGShootCooldown = 0.3f;
            spitfireMGUpgradeCost = 400;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 9)
        {
            spitfireMGShootCooldown = 0.28f;
            spitfireMGUpgradeCost = 600;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 10)
        {
            spitfireMGShootCooldown = 0.26f;
            spitfireMGUpgradeCost = 750;
        }

        /*
         * ENGINE
         */

        if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 0)
        {
            preferences.putInteger(SPITFIRE_ENGINE_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 1)
        {
            spitfireEngineSpeed = 420;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_2;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 2)
        {
            spitfireEngineSpeed = 440;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_3;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 3)
        {
            spitfireEngineSpeed = 460;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_4;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 4)
        {
            spitfireEngineSpeed = 480;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_5;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 5)
        {
            spitfireEngineSpeed = 500;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_6;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 6)
        {
            spitfireEngineSpeed = 530;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_7;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 7)
        {
            spitfireEngineSpeed = 550;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_8;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 8)
        {
            spitfireEngineSpeed = 570;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_9;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 9)
        {
            spitfireEngineSpeed = 590;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_10;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 10)
        {
            spitfireEngineSpeed = 610;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_10;
        }
    }

    private void loadIL2Stats()
    {
        /*
         *MG
         */
        if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 0)
        {
            preferences.putInteger(IL2_MG_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 1)
        {
            IL2MGshootCooldown = 0.48f;
            IL2MGupgradeCost = 50;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 2)
        {
            IL2MGshootCooldown = 0.46f;
            IL2MGupgradeCost = 100;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 3)
        {
            IL2MGshootCooldown = 0.44f;
            IL2MGupgradeCost = 200;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 4)
        {
            IL2MGshootCooldown = 0.42f;
            IL2MGupgradeCost = 300;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 5)
        {
            IL2MGshootCooldown = 0.41f;
            IL2MGupgradeCost = 350;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 6)
        {
            IL2MGshootCooldown = 0.39f;
            IL2MGupgradeCost = 400;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 7)
        {
            IL2MGshootCooldown = 0.38f;
            IL2MGupgradeCost = 450;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 8)
        {
            IL2MGshootCooldown = 0.36f;
            IL2MGupgradeCost = 600;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 9)
        {
            IL2MGshootCooldown = 0.35f;
            IL2MGupgradeCost = 650;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 10)
        {
            IL2MGshootCooldown = 0.33f;
            IL2MGupgradeCost = 800;
        }

        /*
         *ENGINE
         */
        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 0)
        {
            preferences.putInteger(IL2_ENGINE_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 1)
        {
            IL2engineSpeed = 380;
            IL2engineUpgradeCost = 50;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 2)
        {
            IL2engineSpeed = 400;
            IL2engineUpgradeCost = 100;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 3)
        {
            IL2engineSpeed = 420;
            IL2engineUpgradeCost = 200;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 4)
        {
            IL2engineSpeed = 440;
            IL2engineUpgradeCost = 400;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 5)
        {
            IL2engineSpeed = 460;
            IL2engineUpgradeCost = 600;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 6)
        {
            IL2engineSpeed = 480;
            IL2engineUpgradeCost = 730;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 7)
        {
            IL2engineSpeed = 495;
            IL2engineUpgradeCost = 950;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 8)
        {
            IL2engineSpeed = 510;
            IL2engineUpgradeCost = 1000;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 9)
        {
            IL2engineSpeed = 520;
            IL2engineUpgradeCost = 1100;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 10)
        {
            IL2engineSpeed = 540;
            IL2engineUpgradeCost = 1200;
        }

        /*
         *MISSILES
         */

        if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 0)
        {
            preferences.putInteger(IL2_MISSILE_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 1)
        {
            IL2missileShootCooldown = 5f;
            IL2missileUpgradeCost = 50;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 2)
        {
            IL2missileShootCooldown = 4.5f;
            IL2missileUpgradeCost = 100;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 3)
        {
            IL2missileShootCooldown = 4.3f;
            IL2missileUpgradeCost = 150;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 4)
        {
            IL2missileShootCooldown = 4f;
            IL2missileUpgradeCost = 200;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 5)
        {
            IL2missileShootCooldown = 3.5f;
            IL2missileUpgradeCost = 300;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 6)
        {
            IL2missileShootCooldown = 3f;
            IL2missileUpgradeCost = 400;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 7)
        {
            IL2missileShootCooldown = 2.6f;
            IL2missileUpgradeCost = 400;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 8)
        {
            IL2missileShootCooldown = 2.2f;
            IL2missileUpgradeCost = 500;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 9)
        {
            IL2missileShootCooldown = 1.8f;
            IL2missileUpgradeCost = 800;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 10)
        {
            IL2missileShootCooldown = 1.4f;
            IL2missileUpgradeCost = 1000;
        }
    }

    /**
     * -------------------
     * tons of getters and setters
     */

    public static GameState getGameState()
    {
        return gameState;
    }

    public static void setGameState(GameState gameState)
    {
        SpitfireGame.gameState = gameState;
    }

    public int getMGLevel(PlayerType playerType)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                return preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS);
            }

            case MUSTANG:
            {
                return preferences.getInteger(MUSTANG_MG_LEVEL_PREFS);
            }

            case IL2:
            {
                return preferences.getInteger(IL2_MG_LEVEL_PREFS);
            }

            default:
            {
                return 0;
            }
        }
    }

    public void setMGLevel(PlayerType playerType, int level)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                preferences.putInteger(SPITFIRE_MG_LEVEL_PREFS, level);
                preferences.flush();
                loadSpitfireStats();
                break;
            }

            case MUSTANG:
            {
                preferences.putInteger(MUSTANG_MG_LEVEL_PREFS, level);
                preferences.flush();
                loadMustangStats();
                break;
            }

            case IL2:
            {
                preferences.putInteger(IL2_MG_LEVEL_PREFS, level);
                preferences.flush();
                loadIL2Stats();
                break;
            }
        }
    }

    public int getEngineLevel(PlayerType playerType)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                return preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS);
            }

            case MUSTANG:
            {
                return preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS);
            }

            case IL2:
            {
                return preferences.getInteger(IL2_ENGINE_LEVEL_PREFS);
            }

            default:
            {
                return 0;
            }
        }
    }

    public void setEngineLevel(PlayerType playerType, int level)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                preferences.putInteger(SPITFIRE_ENGINE_LEVEL_PREFS, level);
                preferences.flush();
                loadSpitfireStats();
                break;
            }

            case MUSTANG:
            {
                preferences.putInteger(MUSTANG_ENGINE_LEVEL_PREFS, level);
                preferences.flush();
                loadMustangStats();
                break;
            }

            case IL2:
            {
                preferences.putInteger(IL2_ENGINE_LEVEL_PREFS, level);
                preferences.flush();
                loadIL2Stats();
                break;
            }
        }
    }

    public int getEngineUpgradeCost(PlayerType playerType)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                return spitfireEngineUpgradeCost;
            }

            case MUSTANG:
            {
                return mustangEngineUpgradeCost;
            }

            case IL2:
            {
                return IL2engineUpgradeCost;
            }

            default:
            {
                return 0;
            }
        }
    }

    public int getMGUpgradeCost(PlayerType playerType)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                return spitfireMGUpgradeCost;
            }

            case MUSTANG:
            {
                return mustangMGUpgradeCost;
            }

            case IL2:
            {
                return IL2engineUpgradeCost;
            }

            default:
            {
                return 0;
            }
        }
    }

    public int getMissileLevel(PlayerType playerType)
    {
        if (playerType == PlayerType.IL2)
        {
            return preferences.getInteger(IL2_MISSILE_LEVEL_PREFS);
        } else
        {
            return 0;
        }
    }

    public void setMissileLevel(PlayerType playerType, int level)
    {
        if (playerType == PlayerType.IL2)
        {
            preferences.putInteger(IL2_MISSILE_LEVEL_PREFS, level);
            preferences.flush();
            loadIL2Stats();
        }
    }

    public int getMissileUpgradeCost(PlayerType playerType)
    {
        if (playerType == PlayerType.IL2)
        {
            return IL2missileUpgradeCost;
        } else
        {
            return 0;
        }
    }

    private void loadSoundEnabled()
    {
        soundEnabled = preferences.getBoolean(SOUND_PREFS, true);
    }

    private void createFont()
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120;

        font = generator.generateFont(parameter);
        generator.dispose();
    }

    private void loadScore()
    {
        score = preferences.getInteger(SCORE_PREFS);
    }

    private void loadAllTimeMoney()
    {
        earnedMoney = preferences.getInteger(EARNED_MONEY_PREFS);
    }

    private void loadDestroyedEnemies()
    {
        destroyedEnemies = preferences.getInteger(DESTROYED_ENEMIES);
    }

    private void loadHighScore()
    {
        highScore = preferences.getInteger(HIGH_SCORE_PREFS);
    }

    private void loadMoney()
    {
        money = preferences.getInteger(MONEY_PREFS);
    }

    public int getMoney()
    {
        return money;
    }

    public void subtractMoney(int subtractedMoney)
    {
        if (subtractedMoney > 0)
        {
            money = money - subtractedMoney;
            preferences.putInteger(MONEY_PREFS, money);
            preferences.flush();

            loadMoney();
        }
    }

    public static void addMoney(int moneyToAdd)
    {
        money += moneyToAdd;
        preferences.putInteger(MONEY_PREFS, money);
        preferences.flush();
    }

    public int getHighScore()
    {
        return highScore;
    }

    public void setHighScore(int newHighScore)
    {
        if (newHighScore >= 0)
        {
            if (newHighScore > getHighScore())
            {
                highScore = newHighScore;
                preferences.putInteger(HIGH_SCORE_PREFS, highScore);
                preferences.flush();
            }
        }
    }

    public void addDestroyedEnemy()
    {
        destroyedEnemies++;
        preferences.putInteger(DESTROYED_ENEMIES, destroyedEnemies);
        preferences.flush();
    }

    public int getDestroyedEnemies()
    {
        return destroyedEnemies;
    }

    public void addEarnedMoney(int moneyToAdd)
    {
        earnedMoney += moneyToAdd;
        preferences.putInteger(EARNED_MONEY_PREFS, earnedMoney);
        preferences.flush();
    }

    public int getEarnedMoney()
    {
        return earnedMoney;
    }

    public void addScore(int scoreToAdd)
    {
        score += scoreToAdd;
        preferences.putInteger(SCORE_PREFS, score);
    }

    public int getScore()
    {
        return score;
    }

    public void addXp(int xpToAdd)
    {
        if (xpToAdd > 0)
        {
            xp += xpToAdd;
            Gdx.app.log("SpitfireGame-addXp()", "xp = " + Integer.toString(xp));
            preferences.putInteger(XP_PREFS, xp);
            preferences.flush();
        }
    }

    public int getXpForNextLevel()
    {
        loadPlayerLevel();
        return xpForNextLevel;
    }

    public int getXp()
    {
        return xp;
    }

    public int getPlayerLevel()
    {
        loadPlayerLevel();
        return playerLevel;
    }

    public void setSoundEnabled(boolean soundEnabled)
    {
        SpitfireGame.soundEnabled = soundEnabled;
        preferences.putBoolean(SOUND_PREFS, SpitfireGame.soundEnabled);
        preferences.flush();
    }

    public boolean isSoundEnabled()
    {
        return soundEnabled;
    }

    public PlayerType getCurrentPlayerType()
    {
        return currentPlayerType;
    }

    public void setCurrentPlayerType(PlayerType playerType)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                preferences.putString(CURRENT_PLANE, StringHelper.SPITIFRE);
                preferences.flush();
                Gdx.app.log(TAG, "setCurrentPlayerType(): player type string " + StringHelper.SPITIFRE);
                break;
            }

            case MUSTANG:
            {
                if (isBought(PlayerType.MUSTANG))
                {
                    preferences.putString(CURRENT_PLANE, StringHelper.MUSTANG);
                    preferences.flush();
                    Gdx.app.log(TAG, "setCurrentPlayerType(): player type string " + StringHelper.MUSTANG);
                }

                break;
            }

            case IL2:
            {
                if (isBought(PlayerType.IL2))
                {
                    preferences.putString(CURRENT_PLANE, StringHelper.IL2);
                    preferences.flush();
                    Gdx.app.log(TAG, "setCurrentPlayerType(): player type string " + StringHelper.IL2);
                }

                break;
            }
        }

        refreshCurrentPlayerType();
    }

    public String getCurrentPlayerTypeString()
    {
        refreshCurrentPlayerType();

        return preferences.getString(CURRENT_PLANE);
    }

    public Texture getCurrentPlayerTypeTexture()
    {
        refreshCurrentPlayerType();

        if (getCurrentPlayerTypeString().equals(StringHelper.SPITIFRE))
        {
            return spitfireTexture;
        } else if (getCurrentPlayerTypeString().equals(StringHelper.MUSTANG))
        {
            return mustangTexture;
        } else if (getCurrentPlayerTypeString().equals(StringHelper.IL2))
        {
            return IL2texture;
        } else
        {
            return null;
        }
    }

    public static int getSpeed(PlayerType playerType)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                return spitfireEngineSpeed;
            }

            case MUSTANG:
            {
                return mustangEngineSpeed;
            }

            case IL2:
            {
                return IL2engineSpeed;
            }

            default:
            {
                return 0;
            }


        }
    }

    public static float getShootCooldown(PlayerType playerType)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                return spitfireMGShootCooldown;
            }

            case MUSTANG:
            {
                return mustangMGShootCooldown;
            }

            case IL2:
            {
                return IL2MGshootCooldown;
            }

            default:
            {
                return 0;
            }
        }
    }

    public static float getMissileShootCooldown(PlayerType playerType)
    {
        if (playerType == PlayerType.IL2)
        {
            return IL2missileShootCooldown;
        } else
        {
            return 0;
        }
    }

    public boolean isBought(PlayerType playerType)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                return preferences.getBoolean(SPITFIRE_BOUGHT_PREFS);
            }

            case MUSTANG:
            {
                return preferences.getBoolean(MUSTANG_BOUGHT_PREFS);
            }

            case IL2:
            {
                return preferences.getBoolean(IL2_BOUGHT_PREFS);
            }

            default:
            {
                return false;
            }
        }
    }

    public int getPlanePrice(PlayerType playerType)
    {
        switch (playerType)
        {
            case MUSTANG:
            {
                return mustangPrice;
            }

            case IL2:
            {
                return IL2price;
            }

            default:
            {
                return 0;
            }
        }
    }

    public int getCurrentPlaneAvailabilityLevel(PlayerType playerType)
    {
        switch (playerType)
        {
            case SPITFIRE:
            {
                return SPITFIRE_AVAILABILITY_LEVEL;
            }

            case MUSTANG:
            {
                return MUSTANG_AVAILABILITY_LEVEL;
            }

            case IL2:
            {
                return IL2_AVAILABILITY_LEVEL;
            }

            default:
            {
                return 0;
            }
        }
    }

    public static int getDefaultNukeCount()
    {
        return defaultNukeCount;
    }

    public static int getDefaultMGBoostsCount()
    {
        return defaultMGCount;
    }

    public static int getDefaultEngineBoostsCount()
    {
        return defaultEngineCount;
    }

    private Boolean isAchievementUnlocked(Achievement achievement)
    {
        switch (achievement)
        {
            case FIRST_BLOOD:
            {
                return preferences.getBoolean(FIRST_BLOOD_UNLOCKED_PREFS);
            }

            case ACE:
            {
                return preferences.getBoolean(ACE_UNLOCKED_PREFS);
            }

            case CROESUS:
            {
                return preferences.getBoolean(CROESUS_UNLOCKED_PREFS);
            }

            case RATATA:
            {
                return preferences.getBoolean(RATATA_UNLOCKED_PREFS);
            }

            case BIG_BOY:
            {
                return preferences.getBoolean(BIG_BOY_UNLOCKED_PREFS);
            }

            case TOP_GEAR:
            {
                return preferences.getBoolean(TOP_GEAR_UNLOCKED_PREFS);
            }

            default:
            {
                return false;
            }
        }
    }

    public void unlockAchievement(Achievement achievement)
    {
        if (gameServiceClient != null)
        {
            switch (achievement)
            {
                case FIRST_BLOOD:
                {
                    Gdx.app.log(TAG, "UNLOCKING ACHIEVEMENT: CASE FIRST BLOOD");

                    if (!isAchievementUnlocked(FIRST_BLOOD))
                    {
                        Gdx.app.log(TAG, "UNLOCKING ACHIEVEMENT: CASE FIRST BLOOD - UNLOCKED NOW!");
                        gameServiceClient.unlockAchievement(ACHIEVEMENT_FIRST_BLOOD);
                        preferences.putBoolean(FIRST_BLOOD_UNLOCKED_PREFS, true);
                        preferences.flush();
                        break;
                    }
                }

                case ACE:
                {
                    if (!isAchievementUnlocked(ACE))
                    {
                        gameServiceClient.unlockAchievement(ACHIEVEMENT_ACE);
                        preferences.putBoolean(ACE_UNLOCKED_PREFS, true);
                        preferences.flush();
                        break;
                    }
                }

                case CROESUS:
                {
                    if (!isAchievementUnlocked(CROESUS))
                    {
                        gameServiceClient.unlockAchievement(ACHIEVEMENT_CROESUS);
                        preferences.putBoolean(CROESUS_UNLOCKED_PREFS, true);
                        preferences.flush();
                        break;
                    }
                }

                case RATATA:
                {
                    if (!isAchievementUnlocked(RATATA))
                    {
                        gameServiceClient.unlockAchievement(ACHIEVEMENT_RATATA);
                        preferences.putBoolean(RATATA_UNLOCKED_PREFS, true);
                        preferences.flush();
                        break;
                    }
                }

                case BIG_BOY:
                {
                    if (!isAchievementUnlocked(BIG_BOY))
                    {
                        gameServiceClient.unlockAchievement(ACHIEVEMENT_BIGBOY);
                        preferences.putBoolean(BIG_BOY_UNLOCKED_PREFS, true);
                        preferences.flush();
                        break;
                    }
                }

                case TOP_GEAR:
                {
                    if (!isAchievementUnlocked(TOP_GEAR))
                    {
                        gameServiceClient.unlockAchievement(ACHIEVEMENT_TOP_GEAR);
                        preferences.putBoolean(TOP_GEAR_UNLOCKED_PREFS, true);
                        preferences.flush();
                        break;
                    }
                }
            }
        }

        Gdx.app.log("unlockAchievement()", "Unlocking achievement " + achievement.toString());
    }

    public static BoostType getNextLevelReward()
    {
        return NEXT_LEVEL_REWARD;
    }

    @Override
    public void dispose()
    {
        VisUI.dispose();
        font.dispose();

        spitfireTexture.dispose();
        mustangTexture.dispose();
        IL2texture.dispose();

        super.dispose();
    }
}
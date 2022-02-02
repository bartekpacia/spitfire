package pl.baftek.spitfire.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Timer;
import com.kotcrab.vis.ui.VisUI;
import de.golfgl.gdxgamesvcs.IGameServiceClient;
import pl.baftek.spitfire.enums.Achievement;
import pl.baftek.spitfire.enums.GameState;
import pl.baftek.spitfire.enums.PlayerType;
import pl.baftek.spitfire.screens.MenuScreen;
import pl.baftek.spitfire.ui.PopupLabel;

import java.util.Calendar;

import static pl.baftek.spitfire.enums.Achievement.*;
import static pl.baftek.spitfire.game.StringHelper.*;

public class SpitfireGame extends Game {
    private static final String TAG = "SpitfireGame";
    public static final String TITLE = "Spitfire Game";
    public static final int WIDTH = 720;
    public static final int HEIGHT = 1280;

    public IGameServiceClient gameServiceClient;

    public AccountManager playerManager;

    private static Preferences preferences;
    public static int LEVEL_FULLY_EQUIPPED = 8;

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

    public static final int BONUS_MULTIPLIER = 2;

    @Deprecated
    private static final String GAME_PREFS = "pl.baftek.spitfire.preferences";

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

    private static final int SPITFIRE_AVAILABILITY_LEVEL = 1;
    private static final int MUSTANG_AVAILABILITY_LEVEL = 3;
    private static final int IL2_AVAILABILITY_LEVEL = 6;

    public SpitfireGame(IGameServiceClient gameServiceClient) {
        this.gameServiceClient = gameServiceClient;
    }

    @Override
    public void create() {
        init();

        this.setScreen(new MenuScreen(this));
    }

    private void init() {
        preferences = Gdx.app.getPreferences(GAME_PREFS);
        playerManager = new AccountManager(this);

        load();
    }

    private void load() {
        loadBoughtPlanes();
        refreshCurrentPlayerType();

        loadSoundEnabled();

        loadSpitfireStats();
        loadMustangStats();
        loadIL2Stats();

        loadDate();
        loadDailyBonus();

        VisUI.load();

        if (gameServiceClient != null) {
            Timer timer = new Timer();
            timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    Gdx.app.log(TAG, "Player: " + gameServiceClient.getPlayerDisplayName());
                }
            }, 0.5f);
        }
    }

    public static class ResHelper {
        public static final String TAG_AM = "ResHelper";

        public static final Skin SKIN = new Skin(Gdx.files.internal(StringHelper.UI_SKIN_PATH));

        public static final Texture spitfire = new Texture("spitfire.png");
        public static final Texture smallSpitfire = new Texture("spitfire_small.png");
        public static final Texture mustang = new Texture("mustang.png");
        public static final Texture smallMustang = new Texture("mustang_small.png");
        public static final Texture szturmovik = new Texture("szturmovik.png");
        public static final Texture smallSzturmovik = new Texture("szturmovik_small.png");
        public static final Texture bf109 = new Texture("bf109.png");
        public static final Texture me262 = new Texture("me262.png");
        public static final Texture he111 = new Texture("he111.png");
        public static final Texture japan155 = new Texture("japan155.png");

        public static final Texture leaderboard = new Texture("leaderboards.png");
        public static final Texture achievements = new Texture("achievement.png");
        public static final Texture playGames = new Texture("play_games.png");
        public static final Texture money = new Texture("money.png");
        public static final Texture smallMoney = new Texture("money_tiny.png");
        public static final Texture engine = new Texture("engine.png");
        public static final Texture machineGun = new Texture("machine_gun.png");
        public static final Texture missile = new Texture("missile_icon.png");
        public static final Texture mgBoost = new Texture("mg_boost.png");
        public static final Texture engineBoost = new Texture("engine_boost.png");
        public static final Texture nuke = new Texture("nuke.png");
        public static final Texture play = new Texture("play.png");
        public static final Texture pause = new Texture("pause.png");

        public static final Texture sky = new Texture("bgsky.jpg");
        public static final Texture hangar = new Texture("hangar.jpg");

        public static final Sound normalShootSound = Gdx.audio.newSound(Gdx.files.internal("audio/shoot.mp3"));
        public static final Sound strongShootSound = Gdx.audio.newSound(Gdx.files.internal("audio/hmg_shoot.mp3"));
        public static final Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("audio/explosion.mp3"));
        public static final Sound maydaySound = Gdx.audio.newSound(Gdx.files.internal("audio/mayday.mp3"));
        public static final Sound bonusSound = Gdx.audio.newSound(Gdx.files.internal("audio/bonus.mp3"));
        public static final Sound bigExplosionSound = Gdx.audio.newSound(Gdx.files.internal("audio/big_explosion.mp3"));
        public static final Sound missileLaunch = Gdx.audio.newSound(Gdx.files.internal("audio/missile_launch.mp3"));

        public static final ParticleEffect explosion = loadExplosion();
        public static final ParticleEffect bigExplosion = loadBigExplosion();
        public static final ParticleEffect bonusExplosion = loadBonusExplosion();
        public static final ParticleEffect ultraExplosion = loadUltraExplosion();

        private static ParticleEffect loadExplosion() {
            ParticleEffect effect = new ParticleEffect();
            effect.load(Gdx.files.internal("particles/explosion.p"), Gdx.files.internal(""));
            effect.start();

            return effect;
        }

        private static ParticleEffect loadBigExplosion() {
            ParticleEffect effect = new ParticleEffect();
            effect.load(Gdx.files.internal("particles/big_explosion.p"), Gdx.files.internal(""));
            effect.start();

            return effect;
        }

        private static ParticleEffect loadBonusExplosion() {
            ParticleEffect effect = new ParticleEffect();
            effect.load(Gdx.files.internal("particles/bonus_explosion.p"), Gdx.files.internal(""));
            effect.start();

            return effect;
        }

        private static ParticleEffect loadUltraExplosion() {
            ParticleEffect effect = new ParticleEffect();
            effect.load(Gdx.files.internal("particles/ultra_explosion.p"), Gdx.files.internal(""));
            effect.start();

            return effect;
        }

        //font
        public static final BitmapFont font = createFont();

        private static BitmapFont createFont() {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 120;

            return generator.generateFont(parameter);
        }

        public static final TextButtonStyle whiteButtonStyle = createWhiteButtonStyle();

        private static TextButtonStyle createWhiteButtonStyle() {
            TextButtonStyle style = new TextButtonStyle();
            style.font = font;
            Gdx.app.log(TAG_AM, "Creating whiteButtonStyle!");

            return style;
        }

        public static final TextButtonStyle redButtonStyle = createButtonStyle();

        private static TextButtonStyle createButtonStyle() {
            TextButtonStyle style = new TextButtonStyle();
            style.font = font;
            style.fontColor = Color.RED;

            return style;
        }

        public static final TextButtonStyle orangeButtonStyle = createOrangeButtonStyle();

        private static TextButtonStyle createOrangeButtonStyle() {
            TextButtonStyle style = new TextButtonStyle();
            style.font = font;
            style.fontColor = Color.ORANGE;

            return style;
        }

        public static final TextButtonStyle greenButtonStyle = createGreenButtonStyle();

        private static TextButtonStyle createGreenButtonStyle() {
            TextButtonStyle style = new TextButtonStyle();
            style.font = font;
            style.fontColor = Color.GREEN;

            return style;
        }

        public static final LabelStyle whiteLabelStyle = createWhiteLabelStyle();

        private static LabelStyle createWhiteLabelStyle() {
            LabelStyle style = new LabelStyle();
            style.font = font;
            style.fontColor = Color.WHITE;

            return style;
        }

        public static final LabelStyle redLabelStyle = createRedLabelStyle();

        private static LabelStyle createRedLabelStyle() {
            LabelStyle style = new LabelStyle();
            style.font = font;
            style.fontColor = Color.RED;

            return style;
        }

        public static final LabelStyle blueLabelStyle = createBlueLabelStyle();

        private static LabelStyle createBlueLabelStyle() {
            LabelStyle style = new LabelStyle();
            style.font = font;
            style.fontColor = Color.BLUE;

            return style;
        }

        public static final LabelStyle cyanLabelStyle = createCyanLabelStyle();

        private static LabelStyle createCyanLabelStyle() {
            LabelStyle style = new LabelStyle();
            style.font = font;
            style.fontColor = Color.CYAN;

            return style;
        }

        public static final LabelStyle orangeLabelStyle = createOrangeLabelStyle();

        private static LabelStyle createOrangeLabelStyle() {
            LabelStyle style = new LabelStyle();
            style.font = font;
            style.fontColor = Color.ORANGE;

            return style;
        }

        public static final LabelStyle yellowLabelStyle = createYellowLabelStyle();

        private static LabelStyle createYellowLabelStyle() {
            LabelStyle style = new LabelStyle();
            style.font = font;
            style.fontColor = Color.ORANGE;

            return style;
        }

        public static final LabelStyle greenLabelStyle = createGreenLabelStyle();

        private static LabelStyle createGreenLabelStyle() {
            LabelStyle style = new LabelStyle();
            style.font = font;
            style.fontColor = Color.GREEN;

            return style;
        }
    }

    private static void loadDailyBonus() {
        dailyBonusAvailability = preferences.getBoolean(DAILY_BONUS_PREFS);
    }

    private void loadDate() {
        Calendar calendar = Calendar.getInstance();

        int last = preferences.getInteger(DATE_PREFS);
        int today = calendar.get(Calendar.DAY_OF_YEAR);

        //Gdx.app.log(TAG, "PRE-CHANGES Last day: " + last + " Today: " + today + " Bonus available: " + dailyBonusAvailability);

        //only on startup
        if (last == 0) {
            last = calendar.get(Calendar.DAY_OF_YEAR);
            preferences.putInteger(DATE_PREFS, last);
            preferences.flush();
        }

        //new day
        if (today != last) {
            dailyBonusAvailability = true;
            preferences.putBoolean(DAILY_BONUS_PREFS, dailyBonusAvailability);
            last = today;
            preferences.putInteger(DATE_PREFS, last);
            preferences.flush();
        }

        loadDailyBonus();

        //Gdx.app.log(TAG, "AFTER-CHANGES Last day: " + last + " Today: " + today + " Bonus available: " + dailyBonusAvailability);
    }

    public static boolean isDailyBonusAvailable() {
        return dailyBonusAvailability;
    }

    public static void setDailyBonusAvailability(boolean dailyBonusAvailability) {
        preferences.putBoolean(DAILY_BONUS_PREFS, dailyBonusAvailability);
        preferences.flush();
        loadDailyBonus();
    }

    public void buyPlane(PlayerType playerType, Stage stage) {
        Gdx.app.log(TAG, "buyPlane() called!");

        switch (playerType) {
            case MUSTANG: {
                Gdx.app.log(TAG, "case mustang");

                if (playerManager.getLevel() >= getCurrentPlaneAvailabilityLevel(PlayerType.MUSTANG)) {
                    Gdx.app.log(TAG, "player level is ok");

                    if (playerManager.getMoney() >= mustangPrice) {
                        mustangBought = true;
                        preferences.putBoolean(MUSTANG_BOUGHT_PREFS, mustangBought);
                        playerManager.subtractMoney(mustangPrice);
                        Gdx.app.log(TAG, "money is ok, Mustang should be bought");
                        new PopupLabel(StringHelper.BOUGHT_SUCCESSFULLY, Color.GREEN, stage);
                    } else {
                        new PopupLabel(StringHelper.NO_MONEY, Color.RED, stage);
                    }
                } else {
                    new PopupLabel(StringHelper.TOO_LOW_LEVEL, Color.RED, stage);
                }

                break;
            }

            case SZTURMOVIK: {
                Gdx.app.log(TAG, "case il2");

                if (playerManager.getLevel() >= getCurrentPlaneAvailabilityLevel(PlayerType.SZTURMOVIK)) {
                    Gdx.app.log(TAG, "player level is ok");

                    if (playerManager.getMoney() >= IL2price) {
                        il2bought = true;
                        preferences.putBoolean(IL2_BOUGHT_PREFS, il2bought);
                        playerManager.subtractMoney(IL2price);
                        Gdx.app.log(TAG, "money is ok, il2m should be bought");

                        new PopupLabel(StringHelper.BOUGHT_SUCCESSFULLY, Color.GREEN, stage);
                    } else {
                        new PopupLabel(StringHelper.NO_MONEY, Color.RED, stage);
                    }
                } else {
                    new PopupLabel(StringHelper.TOO_LOW_LEVEL, Color.RED, stage);
                }

                break;
            }
        }
    }

    private void loadBoughtPlanes() {
        preferences.putBoolean(SPITFIRE_BOUGHT_PREFS, true);
        preferences.flush();

        spitfireBought = preferences.getBoolean(SPITFIRE_BOUGHT_PREFS);
        mustangBought = preferences.getBoolean(MUSTANG_BOUGHT_PREFS);
        il2bought = preferences.getBoolean(IL2_BOUGHT_PREFS);

        Gdx.app.log(TAG, "Spitfire bought " + spitfireBought + "; Mustang bought " + mustangBought + "; SZTURMOVIK bought " + il2bought);
    }

    public void playSound(Sound sound, float volume) {
        if (soundEnabled) {
            //Gdx.app.log("playSound()", "playing " + sound.toString());
            sound.play(volume);
        }
    }

    private void refreshCurrentPlayerType() {
        if (preferences.getString(CURRENT_PLANE).isEmpty() || preferences.getString(CURRENT_PLANE).equals(StringHelper.SPITIFRE)) {
            currentPlayerType = PlayerType.SPITFIRE;
            currentPlayerTypeString = StringHelper.SPITIFRE;
            preferences.putString(CURRENT_PLANE, StringHelper.SPITIFRE);
            preferences.flush();
        } else if (preferences.getString(CURRENT_PLANE).equals(StringHelper.MUSTANG)) {
            currentPlayerType = PlayerType.MUSTANG;
            currentPlayerTypeString = StringHelper.MUSTANG;
        } else if (preferences.getString(CURRENT_PLANE).equals(StringHelper.IL2)) {
            currentPlayerType = PlayerType.SZTURMOVIK;
            currentPlayerTypeString = StringHelper.IL2;
        }

        Gdx.app.log(TAG, "refreshCurrentPlayerType(): current plane enum: " + currentPlayerType);
        Gdx.app.log(TAG, "refreshCurrentPlayerType(): current plane string: " + preferences.getString(CURRENT_PLANE));
    }

    private void loadMustangStats() {
        /*
         *MG
         */
        if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 0) {
            preferences.putInteger(MUSTANG_MG_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 1) {
            mustangMGShootCooldown = 0.6f;
            mustangMGUpgradeCost = 40;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 2) {
            mustangMGShootCooldown = 0.55f;
            mustangMGUpgradeCost = 100;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 3) {
            mustangMGShootCooldown = 0.52f;
            mustangMGUpgradeCost = 200;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 4) {
            mustangMGShootCooldown = 0.5f;
            mustangMGUpgradeCost = 300;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 5) {
            mustangMGShootCooldown = 0.48f;
            mustangMGUpgradeCost = 400;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 6) {
            mustangMGShootCooldown = 0.46f;
            mustangMGUpgradeCost = 450;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 7) {
            mustangMGShootCooldown = 0.45f;
            mustangMGUpgradeCost = 500;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 8) {
            mustangMGShootCooldown = 0.44f;
            mustangMGUpgradeCost = 650;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 9) {
            mustangMGShootCooldown = 0.42f;
            mustangMGUpgradeCost = 750;
        } else if (preferences.getInteger(MUSTANG_MG_LEVEL_PREFS) == 10) {
            mustangMGShootCooldown = 0.4f;
            mustangMGUpgradeCost = 900;
        }

        /*
         *ENGINE
         */

        if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 0) {
            preferences.putInteger(MUSTANG_ENGINE_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 1) {
            mustangEngineSpeed = 460;
            mustangEngineUpgradeCost = 40;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 2) {
            mustangEngineSpeed = 480;
            mustangEngineUpgradeCost = 80;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 3) {
            mustangEngineSpeed = 500;
            mustangEngineUpgradeCost = 80;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 4) {
            mustangEngineSpeed = 520;
            mustangEngineUpgradeCost = 180;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 5) {
            mustangEngineSpeed = 545;
            mustangEngineUpgradeCost = 300;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 6) {
            mustangEngineSpeed = 580;
            mustangEngineUpgradeCost = 400;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 7) {
            mustangEngineSpeed = 610;
            mustangEngineUpgradeCost = 500;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 8) {
            mustangEngineSpeed = 640;
            mustangEngineUpgradeCost = 600;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 9) {
            mustangEngineSpeed = 670;
            mustangEngineUpgradeCost = 700;
        } else if (preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS) == 10) {
            mustangEngineSpeed = 700;
            mustangEngineUpgradeCost = 800;
        }
    }

    private void loadSpitfireStats() {
        /*
         *MG
         */
        if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 0) {
            preferences.putInteger(SPITFIRE_MG_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 1) {
            spitfireMGShootCooldown = 0.48f;
            spitfireMGUpgradeCost = 10;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 2) {
            spitfireMGShootCooldown = 0.44f;
            spitfireMGUpgradeCost = 20;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 3) {
            spitfireMGShootCooldown = 0.41f;
            spitfireMGUpgradeCost = 50;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 4) {
            spitfireMGShootCooldown = 0.38f;
            spitfireMGUpgradeCost = 100;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 5) {
            spitfireMGShootCooldown = 0.36f;
            spitfireMGUpgradeCost = 150;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 6) {
            spitfireMGShootCooldown = 0.34f;
            spitfireMGUpgradeCost = 200;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 7) {
            spitfireMGShootCooldown = 0.32f;
            spitfireMGUpgradeCost = 265;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 8) {
            spitfireMGShootCooldown = 0.3f;
            spitfireMGUpgradeCost = 400;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 9) {
            spitfireMGShootCooldown = 0.28f;
            spitfireMGUpgradeCost = 600;
        } else if (preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS) == 10) {
            spitfireMGShootCooldown = 0.26f;
            spitfireMGUpgradeCost = 750;
        }

        /*
         * ENGINE
         */

        if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 0) {
            preferences.putInteger(SPITFIRE_ENGINE_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 1) {
            spitfireEngineSpeed = 420;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_2;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 2) {
            spitfireEngineSpeed = 440;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_3;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 3) {
            spitfireEngineSpeed = 460;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_4;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 4) {
            spitfireEngineSpeed = 480;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_5;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 5) {
            spitfireEngineSpeed = 500;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_6;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 6) {
            spitfireEngineSpeed = 530;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_7;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 7) {
            spitfireEngineSpeed = 550;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_8;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 8) {
            spitfireEngineSpeed = 570;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_9;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 9) {
            spitfireEngineSpeed = 590;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_10;
        } else if (preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS) == 10) {
            spitfireEngineSpeed = 610;
            spitfireEngineUpgradeCost = UPGRADE_LEVEL_10;
        }
    }

    private void loadIL2Stats() {
        /*
         *MG
         */
        if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 0) {
            preferences.putInteger(IL2_MG_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 1) {
            IL2MGshootCooldown = 0.48f;
            IL2MGupgradeCost = 50;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 2) {
            IL2MGshootCooldown = 0.46f;
            IL2MGupgradeCost = 100;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 3) {
            IL2MGshootCooldown = 0.44f;
            IL2MGupgradeCost = 200;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 4) {
            IL2MGshootCooldown = 0.42f;
            IL2MGupgradeCost = 300;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 5) {
            IL2MGshootCooldown = 0.41f;
            IL2MGupgradeCost = 350;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 6) {
            IL2MGshootCooldown = 0.39f;
            IL2MGupgradeCost = 400;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 7) {
            IL2MGshootCooldown = 0.38f;
            IL2MGupgradeCost = 450;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 8) {
            IL2MGshootCooldown = 0.36f;
            IL2MGupgradeCost = 600;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 9) {
            IL2MGshootCooldown = 0.35f;
            IL2MGupgradeCost = 650;
        } else if (preferences.getInteger(IL2_MG_LEVEL_PREFS) == 10) {
            IL2MGshootCooldown = 0.33f;
            IL2MGupgradeCost = 800;
        }

        /*
         *ENGINE
         */
        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 0) {
            preferences.putInteger(IL2_ENGINE_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 1) {
            IL2engineSpeed = 380;
            IL2engineUpgradeCost = 50;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 2) {
            IL2engineSpeed = 400;
            IL2engineUpgradeCost = 100;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 3) {
            IL2engineSpeed = 420;
            IL2engineUpgradeCost = 200;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 4) {
            IL2engineSpeed = 440;
            IL2engineUpgradeCost = 400;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 5) {
            IL2engineSpeed = 460;
            IL2engineUpgradeCost = 600;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 6) {
            IL2engineSpeed = 480;
            IL2engineUpgradeCost = 730;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 7) {
            IL2engineSpeed = 495;
            IL2engineUpgradeCost = 950;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 8) {
            IL2engineSpeed = 510;
            IL2engineUpgradeCost = 1000;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 9) {
            IL2engineSpeed = 520;
            IL2engineUpgradeCost = 1100;
        }

        if (preferences.getInteger(IL2_ENGINE_LEVEL_PREFS) == 10) {
            IL2engineSpeed = 540;
            IL2engineUpgradeCost = 1200;
        }

        /*
         *MISSILES
         */

        if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 0) {
            preferences.putInteger(IL2_MISSILE_LEVEL_PREFS, 1);
            preferences.flush();
        }

        if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 1) {
            IL2missileShootCooldown = 5f;
            IL2missileUpgradeCost = 50;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 2) {
            IL2missileShootCooldown = 4.5f;
            IL2missileUpgradeCost = 100;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 3) {
            IL2missileShootCooldown = 4.3f;
            IL2missileUpgradeCost = 150;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 4) {
            IL2missileShootCooldown = 4f;
            IL2missileUpgradeCost = 200;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 5) {
            IL2missileShootCooldown = 3.5f;
            IL2missileUpgradeCost = 300;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 6) {
            IL2missileShootCooldown = 3f;
            IL2missileUpgradeCost = 400;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 7) {
            IL2missileShootCooldown = 2.6f;
            IL2missileUpgradeCost = 400;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 8) {
            IL2missileShootCooldown = 2.2f;
            IL2missileUpgradeCost = 500;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 9) {
            IL2missileShootCooldown = 1.8f;
            IL2missileUpgradeCost = 800;
        } else if (preferences.getInteger(IL2_MISSILE_LEVEL_PREFS) == 10) {
            IL2missileShootCooldown = 1.4f;
            IL2missileUpgradeCost = 1000;
        }
    }

    /**
     * -------------------
     * tons of getters and setters
     */

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        SpitfireGame.gameState = gameState;
    }

    public int getMGLevel(PlayerType playerType) {
        switch (playerType) {
            case SPITFIRE: {
                return preferences.getInteger(SPITFIRE_MG_LEVEL_PREFS);
            }

            case MUSTANG: {
                return preferences.getInteger(MUSTANG_MG_LEVEL_PREFS);
            }

            case SZTURMOVIK: {
                return preferences.getInteger(IL2_MG_LEVEL_PREFS);
            }

            default: {
                return 0;
            }
        }
    }

    public void setMGLevel(PlayerType playerType, int level) {
        switch (playerType) {
            case SPITFIRE: {
                preferences.putInteger(SPITFIRE_MG_LEVEL_PREFS, level);
                preferences.flush();
                loadSpitfireStats();
                break;
            }

            case MUSTANG: {
                preferences.putInteger(MUSTANG_MG_LEVEL_PREFS, level);
                preferences.flush();
                loadMustangStats();
                break;
            }

            case SZTURMOVIK: {
                preferences.putInteger(IL2_MG_LEVEL_PREFS, level);
                preferences.flush();
                loadIL2Stats();
                break;
            }
        }
    }

    public int getEngineLevel(PlayerType playerType) {
        switch (playerType) {
            case SPITFIRE: {
                return preferences.getInteger(SPITFIRE_ENGINE_LEVEL_PREFS);
            }

            case MUSTANG: {
                return preferences.getInteger(MUSTANG_ENGINE_LEVEL_PREFS);
            }

            case SZTURMOVIK: {
                return preferences.getInteger(IL2_ENGINE_LEVEL_PREFS);
            }

            default: {
                return 0;
            }
        }
    }

    public void setEngineLevel(PlayerType playerType, int level) {
        switch (playerType) {
            case SPITFIRE: {
                preferences.putInteger(SPITFIRE_ENGINE_LEVEL_PREFS, level);
                preferences.flush();
                loadSpitfireStats();
                break;
            }

            case MUSTANG: {
                preferences.putInteger(MUSTANG_ENGINE_LEVEL_PREFS, level);
                preferences.flush();
                loadMustangStats();
                break;
            }

            case SZTURMOVIK: {
                preferences.putInteger(IL2_ENGINE_LEVEL_PREFS, level);
                preferences.flush();
                loadIL2Stats();
                break;
            }
        }
    }

    public int getEngineUpgradeCost(PlayerType playerType) {
        switch (playerType) {
            case SPITFIRE: {
                return spitfireEngineUpgradeCost;
            }

            case MUSTANG: {
                return mustangEngineUpgradeCost;
            }

            case SZTURMOVIK: {
                return IL2engineUpgradeCost;
            }

            default: {
                return 0;
            }
        }
    }

    public int getMGUpgradeCost(PlayerType playerType) {
        switch (playerType) {
            case SPITFIRE: {
                return spitfireMGUpgradeCost;
            }

            case MUSTANG: {
                return mustangMGUpgradeCost;
            }

            case SZTURMOVIK: {
                return IL2engineUpgradeCost;
            }

            default: {
                return 0;
            }
        }
    }

    public int getMissileLevel(PlayerType playerType) {
        if (playerType == PlayerType.SZTURMOVIK) {
            return preferences.getInteger(IL2_MISSILE_LEVEL_PREFS);
        } else {
            return 0;
        }
    }

    public void setMissileLevel(PlayerType playerType, int level) {
        if (playerType == PlayerType.SZTURMOVIK) {
            preferences.putInteger(IL2_MISSILE_LEVEL_PREFS, level);
            preferences.flush();
            loadIL2Stats();
        }
    }

    public int getMissileUpgradeCost(PlayerType playerType) {
        if (playerType == PlayerType.SZTURMOVIK) {
            return IL2missileUpgradeCost;
        } else {
            return 0;
        }
    }

    private void loadSoundEnabled() {
        soundEnabled = preferences.getBoolean(SOUND_PREFS, true);
    }

    public void setSoundEnabled(boolean soundEnabled) {
        SpitfireGame.soundEnabled = soundEnabled;
        preferences.putBoolean(SOUND_PREFS, SpitfireGame.soundEnabled);
        preferences.flush();
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public PlayerType getCurrentPlayerType() {
        return currentPlayerType;
    }

    public void setCurrentPlayerType(PlayerType playerType) {
        switch (playerType) {
            case SPITFIRE: {
                preferences.putString(CURRENT_PLANE, StringHelper.SPITIFRE);
                preferences.flush();
                Gdx.app.log(TAG, "setCurrentPlayerType(): player type string " + StringHelper.SPITIFRE);
                break;
            }

            case MUSTANG: {
                if (isBought(PlayerType.MUSTANG)) {
                    preferences.putString(CURRENT_PLANE, StringHelper.MUSTANG);
                    preferences.flush();
                    Gdx.app.log(TAG, "setCurrentPlayerType(): player type string " + StringHelper.MUSTANG);
                }

                break;
            }

            case SZTURMOVIK: {
                if (isBought(PlayerType.SZTURMOVIK)) {
                    preferences.putString(CURRENT_PLANE, StringHelper.IL2);
                    preferences.flush();
                    Gdx.app.log(TAG, "setCurrentPlayerType(): player type string " + StringHelper.IL2);
                }

                break;
            }
        }

        refreshCurrentPlayerType();
    }

    public String getCurrentPlayerTypeString() {
        refreshCurrentPlayerType();

        return preferences.getString(CURRENT_PLANE);
    }

    public static int getSpeed(PlayerType playerType) {
        switch (playerType) {
            case SPITFIRE: {
                return spitfireEngineSpeed;
            }

            case MUSTANG: {
                return mustangEngineSpeed;
            }

            case SZTURMOVIK: {
                return IL2engineSpeed;
            }

            default: {
                return 0;
            }


        }
    }

    public static float getShootCooldown(PlayerType playerType) {
        switch (playerType) {
            case SPITFIRE: {
                return spitfireMGShootCooldown;
            }

            case MUSTANG: {
                return mustangMGShootCooldown;
            }

            case SZTURMOVIK: {
                return IL2MGshootCooldown;
            }

            default: {
                return 0;
            }
        }
    }

    public static float getMissileShootCooldown(PlayerType playerType) {
        if (playerType == PlayerType.SZTURMOVIK) {
            return IL2missileShootCooldown;
        } else {
            return 0;
        }
    }

    public boolean isBought(PlayerType playerType) {
        switch (playerType) {
            case SPITFIRE: {
                return preferences.getBoolean(SPITFIRE_BOUGHT_PREFS);
            }

            case MUSTANG: {
                return preferences.getBoolean(MUSTANG_BOUGHT_PREFS);
            }

            case SZTURMOVIK: {
                return preferences.getBoolean(IL2_BOUGHT_PREFS);
            }

            default: {
                return false;
            }
        }
    }

    public int getPlanePrice(PlayerType playerType) {
        switch (playerType) {
            case MUSTANG: {
                return mustangPrice;
            }

            case SZTURMOVIK: {
                return IL2price;
            }

            default: {
                return 0;
            }
        }
    }

    public int getCurrentPlaneAvailabilityLevel(PlayerType playerType) {
        switch (playerType) {
            case SPITFIRE: {
                return SPITFIRE_AVAILABILITY_LEVEL;
            }

            case MUSTANG: {
                return MUSTANG_AVAILABILITY_LEVEL;
            }

            case SZTURMOVIK: {
                return IL2_AVAILABILITY_LEVEL;
            }

            default: {
                return 0;
            }
        }
    }

    private Boolean isAchievementUnlocked(Achievement achievement) {
        switch (achievement) {
            case FIRST_BLOOD: {
                return preferences.getBoolean(FIRST_BLOOD_UNLOCKED_PREFS);
            }

            case RATATA: {
                return preferences.getBoolean(RATATA_UNLOCKED_PREFS);
            }

            case BIG_BOY: {
                return preferences.getBoolean(BIG_BOY_UNLOCKED_PREFS);
            }

            case TOP_GEAR: {
                return preferences.getBoolean(TOP_GEAR_UNLOCKED_PREFS);
            }

            default: {
                return false;
            }
        }
    }

    public void unlockAchievement(Achievement achievement) {
        switch (achievement) {
            case FIRST_BLOOD: {
                if (!isAchievementUnlocked(FIRST_BLOOD)) {
                    gameServiceClient.unlockAchievement(ACHIEVEMENT_FIRST_BLOOD);
                    preferences.putBoolean(FIRST_BLOOD_UNLOCKED_PREFS, true);
                    preferences.flush();
                    break;
                }
            }

            case RATATA: {
                if (!isAchievementUnlocked(RATATA)) {
                    gameServiceClient.unlockAchievement(ACHIEVEMENT_RATATA);
                    preferences.putBoolean(RATATA_UNLOCKED_PREFS, true);
                    preferences.flush();
                    break;
                }
            }

            case BIG_BOY: {
                if (!isAchievementUnlocked(BIG_BOY)) {
                    gameServiceClient.unlockAchievement(ACHIEVEMENT_BIGBOY);
                    preferences.putBoolean(BIG_BOY_UNLOCKED_PREFS, true);
                    preferences.flush();
                    break;
                }
            }

            case TOP_GEAR: {
                if (!isAchievementUnlocked(TOP_GEAR)) {
                    gameServiceClient.unlockAchievement(ACHIEVEMENT_TOP_GEAR);
                    preferences.putBoolean(TOP_GEAR_UNLOCKED_PREFS, true);
                    preferences.flush();
                    break;
                }
            }
        }


        Gdx.app.log(TAG, "Unlocking achievement " + achievement.toString());
    }

    @Override
    public void dispose() {
        VisUI.dispose();
        ResHelper.font.dispose();

        ResHelper.spitfire.dispose();
        ResHelper.smallSpitfire.dispose();
        ResHelper.mustang.dispose();
        ResHelper.smallMustang.dispose();
        ResHelper.szturmovik.dispose();
        ResHelper.smallSzturmovik.dispose();
        ResHelper.bf109.dispose();
        ResHelper.me262.dispose();
        ResHelper.he111.dispose();
        ResHelper.japan155.dispose();
        ResHelper.achievements.dispose();
        ResHelper.leaderboard.dispose();
        ResHelper.playGames.dispose();
        ResHelper.money.dispose();
        ResHelper.smallMoney.dispose();
        ResHelper.hangar.dispose();
        ResHelper.engine.dispose();
        ResHelper.machineGun.dispose();
        ResHelper.missile.dispose();
        ResHelper.explosion.dispose();
        ResHelper.bigExplosion.dispose();
        ResHelper.bonusExplosion.dispose();
        ResHelper.ultraExplosion.dispose();
        ResHelper.maydaySound.dispose();
        ResHelper.bigExplosionSound.dispose();
        ResHelper.bonusSound.dispose();
        ResHelper.explosionSound.dispose();
        ResHelper.missileLaunch.dispose();

        super.dispose();
    }
}
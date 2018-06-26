package pl.baftek.spitfire.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.Iterator;

import pl.baftek.spitfire.entities.BonusDrop;
import pl.baftek.spitfire.entities.Bullet;
import pl.baftek.spitfire.entities.Enemy;
import pl.baftek.spitfire.entities.Missile;
import pl.baftek.spitfire.entities.Player;
import pl.baftek.spitfire.entities.Projectile;
import pl.baftek.spitfire.enums.Achievement;
import pl.baftek.spitfire.enums.BonusDropType;
import pl.baftek.spitfire.enums.EnemyType;
import pl.baftek.spitfire.enums.GameState;
import pl.baftek.spitfire.enums.PlayerType;
import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.game.StringHelper;
import pl.baftek.spitfire.ui.MyTextButton;
import pl.baftek.spitfire.ui.PopupLabel;

import static com.badlogic.gdx.math.MathUtils.random;
import static pl.baftek.spitfire.game.StringHelper.SCORE;

public class GameplayScreen extends AbstractScreen
{
    private static final String TAG = "GameplayScreen";
    private Timer.Task playServicesRefreshTask;
    //private Timer.Task spawnBulletTask;

    private Player player;
    private Enemy enemy;
    private BonusDrop bonusDrop;

    private Label scoreLabel;
    private Label highScoreLabel;
    private Label bonusScoreLabel;
    private Label mgLabel;
    private Label engineBoostLabel;
    private Label nukeLabel;

    private Image mgImage;
    private Image engineBoostImage;
    private Image nukeImage;

    private Button buttonLeft;
    private Button buttonRight;
    private Image buttonPause;

    private Sound explosionSound;
    private Sound maydaySound;
    private Sound bonusSound;
    private Sound bigExplosionSound;
    private Sound missileLaunch;

    private ParticleEffect explosion;
    private ParticleEffect bigExplosion;
    private ParticleEffect bonusExplosion;
    private ParticleEffect ultraExplosion;

    private Iterator<Projectile> projectiletIter;
    private Iterator<Enemy> enemyIter;
    private Iterator<BonusDrop> bonusDropIter;

    private Array<Projectile> projectiles;
    private Array<Enemy> enemies;
    private Array<BonusDrop> bonusDrops;

    private long lastEnemySpawnTime;
    private int enemySpawnCooldownTime = 650000000;

    private final int BUTTON_HEIGHT = 1030;
    private final int EXPLOSION_OFFSET = 15;
    private int currentScore = 0;
    private int currentMoney = 0;
    private int currentXp = 0;
    private int mgBoosts = SpitfireGame.getDefaultMGBoostsCount();
    private int nukes = SpitfireGame.getDefaultNukeCount();
    private int engineBoosts = SpitfireGame.getDefaultEngineBoostsCount();
    private int counterHelper;

    private boolean spawnEnemies = true;
    private boolean enemyBrokeThrough = false;
    private boolean playerDead = false;

    private boolean leftButtonPressed;
    private boolean rightButtonPressed;
    private boolean gameEnded = false;
    private boolean nukeUsed = false;
    private boolean mgBoostUsed = false;
    private boolean engineBoostUsed = false;
    private boolean nukeActive = false;

    private Table table;

    private HorizontalGroup upHG;
    private HorizontalGroup midHG;
    private HorizontalGroup downHG;
    private VerticalGroup deadVG;
    private VerticalGroup pauseVG;

    private Timer timer;
    private Timer shootTimer;

    private boolean bomberSpawned;

    private Texture bgTexture;
    private Texture moneyTexture;
    private Texture mgBoostTexture;
    private Texture nukeTexture;
    private Texture engineBoostTexture;
    private Texture playTextureTemp;
    private Texture pauseTextureTemp;

    private TextureRegionDrawable playDrawable;
    private TextureRegionDrawable pauseDrawable;

    private float bgY = 0;

    GameplayScreen(SpitfireGame game)
    {
        super(game);
    }

    @Override
    protected void init()
    {
        timer = new Timer();
        shootTimer = new Timer();

        player = new Player(game.getCurrentPlayerType());
        projectiles = new Array<Projectile>();
        enemies = new Array<Enemy>();
        bonusDrops = new Array<BonusDrop>();

        initAssets();

        initPlayServicesRefreshTask();
        initShootTask();

        refreshAchievementsAndLeaderboards();

        SpitfireGame.setGameState(GameState.RUN);
    }

    @Override
    protected void buildUI()
    {
        table = new Table();

        upHG = new HorizontalGroup();
        upHG.space(100);
        upHG.padTop(15);

        midHG = new HorizontalGroup();

        downHG = new HorizontalGroup();
        downHG.space(40);

        deadVG = new VerticalGroup();
        deadVG.space(30);
        deadVG.center();

        MyTextButton buttonMenu = new MyTextButton(StringHelper.GO_TO_MENU, mediumFontSize);
        buttonMenu.setPosition(SpitfireGame.WIDTH / 2, SpitfireGame.HEIGHT / 2 - 50);
        buttonMenu.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                counterHelper++;
                new PopupLabel("Click again to confirm!", Color.RED, stage);

                if (counterHelper >= 2)
                {
                    addMoneyAndXp();
                    game.setScreen(new MenuScreen(game));
                }

                super.clicked(event, x, y);
            }
        });

        pauseVG = new VerticalGroup();
        pauseVG.center();

        pauseVG.addActor(buttonMenu);
        pauseVG.setFillParent(true);

        initUpGroup();
        initButtons();
        initBoostsGroup();

        upHG.addActor(scoreLabel);
        upHG.addActor(highScoreLabel);
        upHG.addActor(buttonPause);

        midHG.addActor(buttonLeft);
        midHG.addActor(buttonRight);

        downHG.addActor(mgImage);
        downHG.addActor(mgLabel);
        downHG.addActor(nukeImage);
        downHG.addActor(nukeLabel);
        downHG.addActor(engineBoostImage);
        downHG.addActor(engineBoostLabel);

        table.add(upHG).row();
        table.add(midHG).row();
        table.add(downHG).row();
        table.top();
        table.setFillParent(true);

        stage.addActor(table);
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);

        spriteBatch.begin();

        if (SpitfireGame.getGameState() == GameState.RUN)
        {
            drawAndMoveBackground(true);
            update();
            updatePlayer();
        } else
        {
            drawAndMoveBackground(false);
        }

        updateUI();
        drawGameObjects();
        updateGameState();

        String text = SCORE + ": " + currentScore;
        scoreLabel.setText(text);

        explosion.draw(spriteBatch, delta);
        bigExplosion.draw(spriteBatch, delta);
        bonusExplosion.draw(spriteBatch, delta);
        ultraExplosion.draw(spriteBatch, delta);

        spriteBatch.end();

        stage.draw();
    }

    private void updateGameState()
    {
        if (SpitfireGame.getGameState() == GameState.GAMEOVER)
        {
            gameOver();
        }
    }

    private void initPlayServicesRefreshTask()
    {
        playServicesRefreshTask = new Timer.Task()
        {
            @Override
            public void run()
            {
                Gdx.app.log(TAG, "refreshing achievements and leaderboard");

                if (game.getDestroyedEnemies() > 0)
                {
                    game.unlockAchievement(Achievement.FIRST_BLOOD);
                }

                if (game.getDestroyedEnemies() == 100)
                {
                    game.unlockAchievement(Achievement.ACE);
                }

                if (game.getEarnedMoney() >= 1000)
                {
                    game.unlockAchievement(Achievement.CROESUS);
                }

                if (mgBoostUsed)
                {
                    game.unlockAchievement(Achievement.RATATA);
                }

                if (nukeUsed)
                {
                    game.unlockAchievement(Achievement.BIG_BOY);
                }

                if (engineBoostUsed)
                {
                    game.unlockAchievement(Achievement.TOP_GEAR);
                }
            }
        };

        timer.scheduleTask(playServicesRefreshTask, 4f, 10f);
    }

    private void initShootTask()
    {
        if (game.getCurrentPlayerType() == PlayerType.IL2)
        {
            shootTimer.scheduleTask(new Timer.Task()
            {
                @Override
                public void run()
                {
                    spawnMissile();
                }
            }, player.getMissileShootCooldown(), player.getMissileShootCooldown());
        }

        shootTimer.scheduleTask(new Timer.Task()
        {
            @Override
            public void run()
            {
                spawnBullet();
            }

        }, player.getShootCooldown(), player.getShootCooldown());
    }

    /**
     * This method should be initialized as first in main init() method.
     */

    private void initAssets()
    {
        //explosions
        //don't forget to draw explosions in render()

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("particles/explosion.p"), Gdx.files.internal(""));
        explosion.start();

        bigExplosion = new ParticleEffect();
        bigExplosion.load(Gdx.files.internal("particles/big_explosion.p"), Gdx.files.internal(""));
        bigExplosion.start();

        bonusExplosion = new ParticleEffect();
        bonusExplosion.load(Gdx.files.internal("particles/bonus_explosion.p"), Gdx.files.internal(""));
        bonusExplosion.start();

        ultraExplosion = new ParticleEffect();
        ultraExplosion.load(Gdx.files.internal("particles/ultra_explosion.p"), Gdx.files.internal(""));
        ultraExplosion.start();

        //sound

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("audio/explosion.mp3"));
        maydaySound = Gdx.audio.newSound(Gdx.files.internal("audio/mayday.mp3"));
        bonusSound = Gdx.audio.newSound(Gdx.files.internal("audio/bonus.mp3"));
        bigExplosionSound = Gdx.audio.newSound(Gdx.files.internal("audio/big_explosion.mp3"));
        missileLaunch = Gdx.audio.newSound(Gdx.files.internal("audio/missile_launch.mp3"));

        //textures

        bgTexture = new Texture("bgsky.jpg");
        moneyTexture = new Texture("money_tiny.png");

        mgBoostTexture = new Texture("mg_boost.png");
        nukeTexture = new Texture("nuke.png");
        engineBoostTexture = new Texture("engine_boost.png");

        playTextureTemp = new Texture(Gdx.files.internal("play.png"));
        pauseTextureTemp = new Texture(Gdx.files.internal("pause.png"));

        playDrawable = new TextureRegionDrawable(new TextureRegion(playTextureTemp));
        pauseDrawable = new TextureRegionDrawable(new TextureRegion(pauseTextureTemp));
    }

    private void drawGameObjects()
    {
        for (Enemy enemy : enemies)
        {
            enemy.move();
            spriteBatch.draw(enemy.getTexture(), enemy.x, enemy.y);
        }

        for (Projectile projectile : projectiles)
        {
            projectile.move();
            spriteBatch.draw(projectile.getTexture(), projectile.getX(), projectile.getY());
        }

        for (BonusDrop bonusDrop : bonusDrops)
        {
            bonusDrop.move();
            spriteBatch.draw(bonusDrop.getTexture(), bonusDrop.x, bonusDrop.y);
        }

        if (SpitfireGame.getGameState() == GameState.RUN || SpitfireGame.getGameState() == GameState.PAUSE)
        {
            spriteBatch.draw(player.getTexture(), player.x, player.y);
        }
    }

    private void initUpGroup()
    {
        scoreLabel = new Label(StringHelper.SCORE + ": " + currentScore, whiteLabelStyle);
        scoreLabel.setFontScale(0.4f);

        highScoreLabel = new Label(StringHelper.HIGH + ": " + game.getHighScore(), whiteLabelStyle);
        highScoreLabel.setFontScale(0.4f);

        buttonPause = new Image(pauseDrawable);
        buttonPause.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (SpitfireGame.getGameState() == GameState.PAUSE)
                {
                    SpitfireGame.setGameState(GameState.RUN);
                    new PopupLabel(StringHelper.GAME_RESUMED, Color.CYAN, stage);
                    counterHelper = 0;
                    pauseVG.remove();

                } else if (SpitfireGame.getGameState() == GameState.RUN)
                {
                    SpitfireGame.setGameState(GameState.PAUSE);
                    new PopupLabel(StringHelper.GAME_PAUSED, Color.CHARTREUSE, stage);
                    stage.addActor(pauseVG);
                }
                super.clicked(event, x, y);
            }
        });
    }

    private void updateUI()
    {
        if (SpitfireGame.getGameState() == GameState.PAUSE)
        {
            buttonPause.setDrawable(playDrawable);
        } else if (SpitfireGame.getGameState() == GameState.RUN)
        {
            buttonPause.setDrawable(pauseDrawable);
        }

    }

    private void drawAndMoveBackground(boolean moveBackground)
    {
        spriteBatch.draw(bgTexture, 0, bgY);

        if (moveBackground)
        {
            bgY -= 2;

            if (bgY <= -1280)
            {
                bgY = 0;
                Gdx.app.log("Background", "refreshing background img");
            }
        }
    }

    private void refreshAchievementsAndLeaderboards()
    {
        if (SpitfireGame.gameServiceClient != null)
        {
            playServicesRefreshTask.run();
        }
    }

    private void update()
    {
        if (!playerDead && !enemyBrokeThrough)
        {
            if (spawnEnemies)
            {
                //check if bomber was spawned
                if (bomberSpawned)
                {
                    //try to get 1st enemy in array - if it works, then at least 1 enemy exists, else IllegalStateException is thrown
                    try
                    {
                        //throws IllegalStateException when empty
                        enemies.first();
                    } catch (IllegalStateException exception)
                    {
                        //there are no enemies, so spawn them
                        System.out.println("GameplayScreen: No enemies left - illegal state exception was thrown");
                        bomberSpawned = false;
                    }
                }

                //spawn enemies normally
                else if (!bomberSpawned)
                {
                    if (TimeUtils.nanoTime() - lastEnemySpawnTime > enemySpawnCooldownTime)
                    {
                        spawnEnemy(true);
                        decraseEnemySpawnTime();
                    }
                }
            }

            enemyIter = enemies.iterator();
            while (enemyIter.hasNext())
            {
                Enemy enemy = enemyIter.next();

                if (enemy.y < -64)
                {
                    enemyBrokeThrough = true;
                    enemyIter.remove();
                    continue;
                } else if (enemy.overlaps(player))
                {
                    SpitfireGame.playSound(maydaySound, 0.75f);
                    bigExplosion.reset();
                    bigExplosion.getEmitters().first().setPosition(player.x + player.getTexture().getWidth() / 2, player.y + player.getTexture().getWidth() / 2);

                    SpitfireGame.setGameState(GameState.GAMEOVER);

                    enemyIter.remove();
                    continue;
                }

                projectiletIter = projectiles.iterator();
                while (projectiletIter.hasNext())
                {
                    Projectile projectile = projectiletIter.next();

                    if (projectile.getY() > SpitfireGame.HEIGHT)
                    {
                        projectiletIter.remove();
                        continue;
                    }

                    if (projectile.overlaps(enemy))
                    {
                        SpitfireGame.playSound(explosionSound, 0.6f);
                        explosion.reset();
                        explosion.getEmitters().first().setPosition(projectile.getX(), projectile.getY() + EXPLOSION_OFFSET);

                        projectiletIter.remove();
                        enemy.setHp(enemy.getHp() - projectile.getDamage());

                        //enemy destroyed
                        if (enemy.getHp() <= 0)
                        {
                            destroyEnemy(enemy);
                        }
                        break;
                    }
                }
            }
        }

        bonusDropIter = bonusDrops.iterator();
        while (bonusDropIter.hasNext())
        {
            BonusDrop bonusDrop = bonusDropIter.next();

            if (player.overlaps(bonusDrop))
            {
                System.out.println("BONUS DROP CATCHED");
                //number just for setting heigth of the bonusScoreLabel
                int random = random(200, 400);

                if (bonusDrop.getBonusDropType().equals(BonusDropType.POINTS_DROP))
                {
                    SpitfireGame.playSound(bonusSound, 1f);
                    bonusExplosion.reset();
                    bonusExplosion.getEmitters().first().setPosition(bonusDrop.x + bonusDrop.getTexture().getWidth() / 2, bonusDrop.y + bonusDrop.getTexture().getWidth() / 2);

                    //score that will be added to currentScore
                    int bonusScore = random(20, 50);
                    currentScore += bonusScore;

                    bonusScoreLabel.setStyle(greenLabelStyle);
                    bonusScoreLabel.setText("+ " + Integer.toString(bonusScore));
                    bonusScoreLabel.setPosition(player.x, player.y + random);
                    stage.addActor(bonusScoreLabel);

                    //remove after momento
                    timer.scheduleTask(new Timer.Task()
                    {
                        @Override
                        public void run()
                        {
                            bonusScoreLabel.remove();
                        }
                    }, 1.5f);
                } else if (bonusDrop.getBonusDropType() == BonusDropType.FIRERATE_DROP)
                {
                    SpitfireGame.playSound(bonusSound, 1f);
                    bonusExplosion.reset();
                    bonusExplosion.getEmitters().first().setPosition(bonusDrop.x + bonusDrop.getTexture().getWidth() / 2, bonusDrop.y + bonusDrop.getTexture().getWidth() / 2);

                    mgBoosts++;
                    mgLabel.setText(Integer.toString(mgBoosts));
                    bonusScoreLabel.setStyle(yellowLabelStyle);
                    bonusScoreLabel.setText("+ MG");
                    bonusScoreLabel.setPosition(player.x - random, player.y + random);
                    stage.addActor(bonusScoreLabel);

                    //remove after momento
                    timer.scheduleTask(new Timer.Task()
                    {
                        @Override
                        public void run()
                        {
                            bonusScoreLabel.remove();
                        }
                    }, 1.5f);
                } else if (bonusDrop.getBonusDropType() == BonusDropType.SPEED_DROP)
                {
                    SpitfireGame.playSound(bonusSound, 1f);
                    bonusExplosion.reset();
                    bonusExplosion.getEmitters().first().setPosition(bonusDrop.x + bonusDrop.getTexture().getWidth() / 2, bonusDrop.y + bonusDrop.getTexture().getWidth() / 2);

                    engineBoosts++;
                    engineBoostLabel.setText(Integer.toString(engineBoosts));

                    bonusScoreLabel.setStyle(blueLabelStyle);
                    bonusScoreLabel.setText("+ ENG");
                    bonusScoreLabel.setPosition(player.x - random, player.y + random);
                    stage.addActor(bonusScoreLabel);

                    //remove after momento
                    timer.scheduleTask(new Timer.Task()
                    {
                        @Override
                        public void run()
                        {
                            bonusScoreLabel.remove();
                        }
                    }, 1.5f);
                } else if (bonusDrop.getBonusDropType() == BonusDropType.NUKE_DROP)
                {
                    SpitfireGame.playSound(bonusSound, 0.7f);
                    bonusExplosion.reset();
                    bonusExplosion.getEmitters().first().setPosition(bonusDrop.x + bonusDrop.getTexture().getWidth() / 2, bonusDrop.y + bonusDrop.getTexture().getWidth() / 2);

                    nukes++;
                    nukeLabel.setText(Integer.toString(nukes));

                    bonusScoreLabel.setStyle(redLabelStyle);
                    bonusScoreLabel.setText("+ NUKE");
                    bonusScoreLabel.setPosition(player.x - random, player.y + random);
                    stage.addActor(bonusScoreLabel);

                    //remove after momento
                    timer.scheduleTask(new Timer.Task()
                    {
                        @Override
                        public void run()
                        {
                            bonusScoreLabel.remove();
                        }
                    }, 1.5f);
                } else if (bonusDrop.getBonusDropType() == BonusDropType.FAKE)
                {
                    SpitfireGame.playSound(explosionSound, 1f);
                    playerDead = true;
                }

                bonusDropIter.remove();
            }

            if (bonusDrop.y < -100)
            {
                bonusDropIter.remove();
                System.out.println("deleted bonus drop");
            }

        }
    }

    private void spawnBonusDrop(float x, float y)
    {
        bonusDrop = new BonusDrop(x, y);
        bonusDrops.add(bonusDrop);
    }

    private void addCurrentScore(float scoreToAdd)
    {
        currentScore += scoreToAdd;
        game.addScore((int) scoreToAdd);

        if (currentScore > game.getHighScore())
        {
            game.setHighScore(currentScore);
            //this way, it produces less garbage
            String scoreString = Integer.toString(game.getHighScore());
            String bestScoreString = StringHelper.HIGH + scoreString;
            highScoreLabel.setText(bestScoreString);
        }
    }

    private void updatePlayer()
    {
        if (enemyBrokeThrough || playerDead)
        {
            SpitfireGame.setGameState(GameState.GAMEOVER);
        } else
        {
            if (leftButtonPressed)
            {
                player.moveLeft();
            }

            if (rightButtonPressed)
            {
                player.moveRight();
            }

            if (player.x < 0)
            {
                player.setX(0);
            }

            if (player.x > SpitfireGame.WIDTH - player.getTexture().getWidth())
            {
                player.setX(SpitfireGame.WIDTH - player.getTexture().getWidth());
            }
        }
    }

    private void spawnBullet()
    {
        if (SpitfireGame.getGameState() == GameState.RUN)
        {
            if (game.getCurrentPlayerType() == PlayerType.SPITFIRE)
            {
                int height = 75;

                Projectile bullet1 = new Bullet(player.x + player.getTexture().getWidth() / 2 - player.getTexture().getWidth() / 6, player.y + height, game.getCurrentPlayerType());
                Projectile bullet2 = new Bullet(player.x + player.getTexture().getWidth() / 2 + player.getTexture().getWidth() / 6, player.y + height, game.getCurrentPlayerType());

                projectiles.add(bullet1);
                projectiles.add(bullet2);
            } else if (game.getCurrentPlayerType() == PlayerType.MUSTANG)
            {
                int height = 70;

                Projectile bullet1 = new Bullet(player.x + player.getTexture().getWidth() / 2 - player.getTexture().getWidth() / 6, player.y + height, game.getCurrentPlayerType());
                Projectile bullet2 = new Bullet(player.x + player.getTexture().getWidth() / 2 + player.getTexture().getWidth() / 6, player.y + height, game.getCurrentPlayerType());

                projectiles.add(bullet1);
                projectiles.add(bullet2);
            } else if (game.getCurrentPlayerType() == PlayerType.IL2)
            {
                int height = 50;

                Projectile bullet1 = new Bullet(player.x + player.getTexture().getWidth() / 2 - player.getTexture().getWidth() / 6, player.y + height, game.getCurrentPlayerType());
                Projectile bullet2 = new Bullet(player.x + player.getTexture().getWidth() / 2 + player.getTexture().getWidth() / 6, player.y + height, game.getCurrentPlayerType());

                projectiles.add(bullet1);
                projectiles.add(bullet2);
            }
        }
    }

    private void spawnMissile()
    {
        if (SpitfireGame.getGameState() == GameState.RUN)
        {
            if (game.getCurrentPlayerType() == PlayerType.IL2)
            {
                Projectile missile;

                int height = 50;
                int random = MathUtils.random(0, 1);

                if (random == 0)
                {
                    missile = new Missile(player.x + player.getTexture().getWidth() / 2 - 50, player.y + height);
                } else
                {
                    missile = new Missile(player.x + player.getTexture().getWidth() / 2 + 50, player.y + height);
                }

                SpitfireGame.playSound(missileLaunch, 1f);

                projectiles.add(missile);
            }
        }
    }

    private void spawnEnemy(boolean bomberAllowed)
    {
        EnemyType enemyType;
        int i = MathUtils.random(1, 100);
        int random1 = 60;
        int random2 = 610;

        if (i > 0 && i <= 80)
        {
            enemyType = EnemyType.BF109;
        } else if (i > 80 && i <= 90)
        {
            enemyType = EnemyType.ME262;
        } else if (i > 50 && i < 94)
        {
            if (bomberAllowed)
            {
                random2 = 450;
                System.out.println("Bomber spawned!");
                enemyType = EnemyType.HE111;

                bomberSpawned = true;

                timer.scheduleTask(new Timer.Task()
                {
                    @Override
                    public void run()
                    {
                        spawnEnemy(false);

                        timer.scheduleTask(new Timer.Task()
                        {
                            @Override
                            public void run()
                            {
                                spawnEnemy(false);

                                timer.scheduleTask(new Timer.Task()
                                {
                                    @Override
                                    public void run()
                                    {
                                        spawnEnemy(true);
                                    }
                                }, 1f);
                            }
                        }, 0.85f);
                    }
                }, 0.5f);

            } else
            {
                enemyType = EnemyType.ME262;
            }
        } else
        {
            enemyType = EnemyType.JAPAN155;
        }

        enemy = new Enemy(enemyType, random(random1, random2), SpitfireGame.HEIGHT + 300);
        enemies.add(enemy);
        lastEnemySpawnTime = TimeUtils.nanoTime();
    }

    private void decraseEnemySpawnTime()
    {
        if (currentScore >= 0)
        {
            enemySpawnCooldownTime = 820000000;
        }

        if (currentScore > 50)
        {
            enemySpawnCooldownTime = 580000000;
        }

        if (currentScore > 100)
        {
            enemySpawnCooldownTime = 540000000;
        }

        if (currentScore > 200)
        {
            enemySpawnCooldownTime = 570000000;
        }

        if (currentScore > 300)
        {
            enemySpawnCooldownTime = 530000000;
        }

        if (currentScore > 400)
        {
            enemySpawnCooldownTime = 480000000;
        }

        if (currentScore > 500)
        {
            enemySpawnCooldownTime = 470000000;
        }

        if (currentScore > 600)
        {
            enemySpawnCooldownTime = 460000000;
        }

        if (currentScore > 800)
        {
            enemySpawnCooldownTime = 440000000;
        }

        if (currentScore > 1000)
        {
            enemySpawnCooldownTime = 420000000;
        }

        if (currentScore > 1150)
        {
            enemySpawnCooldownTime = 400000000;
        }

        if (currentScore > 1300)
        {
            enemySpawnCooldownTime = 380000000;
        }

        if (currentScore > 1400)
        {
            enemySpawnCooldownTime = 350000000;
        }

        if (currentScore > 1500)
        {
            enemySpawnCooldownTime = 320000000;
        }

        if (currentScore > 1600)
        {
            enemySpawnCooldownTime = 310000000;
        }

        if (currentScore > 1800)
        {
            enemySpawnCooldownTime = 300000000;
        }

        if (currentScore > 2000)
        {
            enemySpawnCooldownTime = 290000000;
        }
    }

    private void initButtonLeft()
    {
        buttonLeft = new Button(new ButtonStyle());

        buttonLeft.padTop(BUTTON_HEIGHT);
        buttonLeft.padLeft(SpitfireGame.WIDTH / 2);

        buttonLeft.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                leftButtonPressed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                leftButtonPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    private void initButtonRight()
    {
        buttonRight = new Button(new ButtonStyle());

        buttonRight.padTop(BUTTON_HEIGHT);
        buttonRight.padRight(SpitfireGame.WIDTH / 2);

        buttonRight.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                rightButtonPressed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                rightButtonPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    private void initBoostsGroup()
    {
        mgLabel = new Label("MG", yellowLabelStyle);
        mgLabel.setFontScale(0.5f);
        mgLabel.setText(Integer.toString(mgBoosts));

        nukeLabel = new Label("NUKE", redLabelStyle);
        nukeLabel.setFontScale(0.5f);
        nukeLabel.setText(Integer.toString(nukes));

        engineBoostLabel = new Label("ENG", blueLabelStyle);
        engineBoostLabel.setFontScale(0.5f);
        engineBoostLabel.setText(Integer.toString(engineBoosts));

        mgImage = new Image(mgBoostTexture);
        mgImage.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                activateMGBoost();

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        nukeImage = new Image(nukeTexture);
        nukeImage.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                activateNuke();

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        engineBoostImage = new Image(engineBoostTexture);
        engineBoostImage.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                activateEngineBoost();

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        bonusScoreLabel = new Label("", whiteLabelStyle);
        bonusScoreLabel.setFontScale(0.45f);
    }

    private void activateEngineBoost()
    {
        if (SpitfireGame.getGameState() == GameState.RUN)
        {
            if (engineBoosts > 0)
            {
                if (!player.isEngineBoostActive())
                {
                    engineBoosts--;
                    engineBoostUsed = true;
                    engineBoostLabel.setText(Integer.toString(engineBoosts));

                    player.setEngineBoostActive(true);

                    timer.scheduleTask(new Timer.Task()
                    {
                        @Override
                        public void run()
                        {
                            player.setEngineBoostActive(false);
                        }
                    }, 4f);
                }
            }
        }
    }

    private void activateMGBoost()
    {
        if (SpitfireGame.getGameState() == GameState.RUN)
        {
            if (mgBoosts > 0)
            {
                if (!player.isMGBoostActive())
                {
                    mgBoosts--;
                    mgBoostUsed = true;
                    mgLabel.setText(Integer.toString(mgBoosts));

                    player.setMGBoostActive(true);

                    shootTimer.clear();
                    initShootTask();

                    shootTimer.scheduleTask(new Timer.Task()
                    {
                        @Override
                        public void run()
                        {
                            player.setMGBoostActive(false);
                            shootTimer.clear();
                            initShootTask();
                        }
                    }, 4f);
                }
            }
        }
    }

    private void activateNuke()
    {
        if (SpitfireGame.getGameState() == GameState.RUN)
        {
            if (nukes > 0)
            {
                if (!nukeActive)
                {
                    nukeActive = true;
                    SpitfireGame.playSound(bigExplosionSound, 1f);
                    ultraExplosion.getEmitters().first().setPosition(SpitfireGame.WIDTH / 2, SpitfireGame.HEIGHT / 2);
                    ultraExplosion.reset();

                    //kills all enemies! wohoo!
                    nukes--;
                    nukeLabel.setText(Integer.toString(nukes));

                    for (Enemy enemy : enemies)
                    {
                        int pointsToAdd = (int) enemy.getHp();
                        addCurrentScore(pointsToAdd);

                        bigExplosion.getEmitters().first().setPosition(enemy.x + enemy.getTexture().getWidth() / 2, enemy.y + enemy.getTexture().getHeight() / 2);
                        explosion.reset();

                        destroyEnemy(enemy);
                    }


                    enemies.clear();
                    spawnEnemies = false;
                    nukeUsed = true;
                    timer.scheduleTask(new Timer.Task()
                    {
                        @Override
                        public void run()
                        {
                            spawnEnemies = true;
                            nukeActive = false;
                        }
                    }, 2f);
                }
            }
        }
    }

    private void gameOver()
    {
        if (!gameEnded)
        {
            timer.stop();
            shootTimer.stop();
            gameEnded = true;
            spawnEnemies = false;

            SpitfireGame.setGameState(GameState.GAMEOVER);
            addMoneyAndXp();

            refreshAchievementsAndLeaderboards();
            System.out.println("Submitting to leaderboard; make it work (GamePlayScreen.playerDead())");

            drawGameOverUI();
        }
    }

    private void drawGameOverUI()
    {
        Label orLabel;
        Label labelYouGet;
        Label labelMoney;
        Label labelXp;
        Image moneyImage;

        Table table = new Table();
        HorizontalGroup buttonsGroup = new HorizontalGroup();
        HorizontalGroup moneyGroup = new HorizontalGroup();
        VerticalGroup lootGroup = new VerticalGroup();

        Label gameOverLabel = new Label("GAME OVER", redLabelStyle);
        gameOverLabel.setFontScale(normalFontSize);

        orLabel = new Label("OR", whiteLabelStyle);
        orLabel.setFontScale(ultraSmallFontSize);

        MyTextButton retryButton = new MyTextButton("RETRY", mediumFontSize);
        retryButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new GameplayScreen(game));
                super.clicked(event, x, y);
            }
        });

        MyTextButton menuButton = new MyTextButton(StringHelper.GO_TO_MENU, mediumFontSize);
        menuButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new MenuScreen(game));
                super.clicked(event, x, y);
            }
        });


        labelYouGet = new Label("You get", whiteLabelStyle);
        labelYouGet.setFontScale(mediumFontSize);

        labelMoney = new Label("+ " + currentMoney, greenLabelStyle);
        labelMoney.setFontScale(smallFontSize);

        labelXp = new Label("+ " + currentXp + " XP", greenLabelStyle);
        labelXp.setFontScale(smallFontSize);

        moneyImage = new Image(moneyTexture);

        moneyGroup.addActor(labelMoney);
        moneyGroup.addActor(moneyImage);
        moneyGroup.space(20);

        buttonsGroup.addActor(retryButton);
        buttonsGroup.addActor(orLabel);
        buttonsGroup.addActor(menuButton);
        buttonsGroup.space(20);

        lootGroup.addActor(labelYouGet);
        lootGroup.addActor(moneyGroup);
        lootGroup.addActor(labelXp);
        lootGroup.space(20);

        table.add(gameOverLabel).padBottom(100).row();
        table.add(buttonsGroup).padBottom(90).row();
        table.add(lootGroup).padBottom(30).row();

        if (SpitfireGame.isDailyBonusAvailable())
        {
            HorizontalGroup bonusHG = new HorizontalGroup();

            Image image2 = new Image(moneyTexture);
            Label dailyBonusInfo = new Label("Daily bonus 2x", orangeLabelStyle);
            dailyBonusInfo.setFontScale(smallFontSize);

            bonusHG.addActor(dailyBonusInfo);
            bonusHG.addActor(image2);
            bonusHG.space(10);
            table.add(bonusHG).row();

            SpitfireGame.setDailyBonusAvailability(false);
        }

        table.setFillParent(true);
        stage.addActor(table);
    }

    private void addMoneyAndXp()
    {
        currentMoney = calculateMoney();
        SpitfireGame.addMoney(currentMoney);
        game.addEarnedMoney(currentMoney);
        game.addXp(currentXp);
    }

    private void destroyEnemy(Enemy enemy)
    {
        //if destroyed enemy is bomber, playDrawable additional explosion and spawn BonusDrop
        if (enemy.getType() == EnemyType.HE111)
        {
            bigExplosion.getEmitters().first().setPosition(enemy.x, enemy.y);
            bigExplosion.reset();
            spawnBonusDrop(enemy.x + enemy.getTexture().getWidth() / 2, enemy.y);
            addCurrentXp(5);
            addCurrentScore(Enemy.hpHE111);
        } else if (enemy.getType() == EnemyType.JAPAN155)
        {
            addCurrentXp(2);
            addCurrentScore(Enemy.hpJapan155);
        } else if (enemy.getType() == EnemyType.ME262)
        {
            addCurrentXp(1);
            addCurrentScore(Enemy.hpME262);
        } else if (enemy.getType() == EnemyType.BF109)
        {
            addCurrentScore(Enemy.hpBF109);
        }

        bigExplosion.getEmitters().first().setPosition(enemy.x + enemy.getTexture().getWidth() / 2, enemy.y + enemy.getTexture().getHeight() / 2);
        bigExplosion.reset();
        game.addDestroyedEnemy();
        enemyIter.remove();
    }

    private int calculateMoney()
    {
        float randomvar = random(0.75f, 1.25f);
        currentMoney = (int) (currentScore / 3f * randomvar);

        if (SpitfireGame.isDailyBonusAvailable())
        {
            currentMoney = currentMoney * SpitfireGame.BONUS_MULTIPLIER;
            System.out.println("DAILY BONUS WAS AVAILABLE");
            new PopupLabel("Bonus " + Boolean.toString(SpitfireGame.isDailyBonusAvailable()), Color.GREEN, stage);
        }

        return currentMoney;
    }

    private void initButtons()
    {
        initButtonLeft();
        initButtonRight();
    }

    private void addCurrentXp(int xp)
    {
        currentXp += xp;
    }

    @Override
    public void pause()
    {
        timer.stop();
        shootTimer.stop();
        refreshAchievementsAndLeaderboards();
        super.pause();
    }

    @Override
    public void resume()
    {
        timer.start();
        shootTimer.start();
        refreshAchievementsAndLeaderboards();
        super.resume();
    }

    @Override
    public void dispose()
    {
        explosion.dispose();
        bigExplosion.dispose();
        ultraExplosion.dispose();
        bonusExplosion.dispose();

        explosionSound.dispose();
        bigExplosionSound.dispose();
        bonusSound.dispose();
        maydaySound.dispose();

        bgTexture.dispose();
        moneyTexture.dispose();
        mgBoostTexture.dispose();
        nukeTexture.dispose();
        engineBoostTexture.dispose();
        playTextureTemp.dispose();
        pauseTextureTemp.dispose();

        timer = null;
        shootTimer = null;

        super.dispose();
    }
}
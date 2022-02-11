package pl.baftek.spitfire.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import pl.baftek.spitfire.enums.PlayerType;
import pl.baftek.spitfire.game.SpitfireGame;

public class Player extends GameObject {
    private final int STARTING_X = SpitfireGame.WIDTH / 2;
    private final int STARTING_Y = 140;

    private final int BOOSTED_SPEED = 950;
    private final float BOOSTED_SHOOT_COOLDOWN = 0.13f;
    private final PlayerType playerType;
    private float shootCooldown;
    private int speed;
    private float missileShootCooldown;
    private boolean MGBoostActive;
    private boolean engineBoostActive;
    private Texture texture;

    public Player(PlayerType playerType) {
        this.playerType = playerType;

        if (playerType == PlayerType.SPITFIRE) {
            texture = SpitfireGame.ResHelper.smallSpitfire;
        } else if (playerType == PlayerType.MUSTANG) {
            texture = SpitfireGame.ResHelper.smallMustang;
        } else if (playerType == PlayerType.SZTURMOVIK) {
            texture = SpitfireGame.ResHelper.smallSzturmovik;
        }

        loadStats(playerType);

        this.x = STARTING_X - (texture.getWidth() >> 1);
        this.y = STARTING_Y;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        System.out.println("Player alive. Shoot cooldown is " + shootCooldown + ", speed is " + speed + ", IL-2M missile shoot cooldown is " + missileShootCooldown);
    }

    private void loadStats(PlayerType playerType) {
        speed = SpitfireGame.getSpeed(playerType);

        shootCooldown = SpitfireGame.getShootCooldown(playerType);

        missileShootCooldown = SpitfireGame.getMissileShootCooldown(playerType);
    }

    public void moveLeft() {
        super.move();

        if (gameRun) {
            if (engineBoostActive) {
                this.x = this.x - BOOSTED_SPEED * Gdx.graphics.getDeltaTime();
            } else {
                this.x = this.x - speed * Gdx.graphics.getDeltaTime();
            }
        }
    }

    public void moveRight() {
        super.move();

        if (gameRun) {
            if (engineBoostActive) {
                this.x = this.x + BOOSTED_SPEED * Gdx.graphics.getDeltaTime();
            } else {
                this.x = this.x + speed * Gdx.graphics.getDeltaTime();
            }
        }
    }

    /*
     * getters and setters
     */

    public boolean isEngineBoostActive() {
        return engineBoostActive;
    }

    public void setEngineBoostActive(boolean engineBoostActive) {
        if (engineBoostActive) {
            this.speed = BOOSTED_SPEED;
            this.engineBoostActive = true;
        } else {
            this.speed = SpitfireGame.getSpeed(playerType);
            this.engineBoostActive = false;
        }
    }

    public float getShootCooldown() {
        return shootCooldown;
    }

    public void setShootCooldown(float shootCooldown) {
        this.shootCooldown = shootCooldown;
    }

    public float getMissileShootCooldown() {
        return missileShootCooldown;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isMGBoostActive() {
        return MGBoostActive;
    }

    public void setMGBoostActive(boolean MGBoostActive) {
        if (MGBoostActive) {
            this.shootCooldown = BOOSTED_SHOOT_COOLDOWN;
            this.MGBoostActive = true;
        } else {
            this.shootCooldown = SpitfireGame.getShootCooldown(playerType);
            this.MGBoostActive = false;
        }
    }
}

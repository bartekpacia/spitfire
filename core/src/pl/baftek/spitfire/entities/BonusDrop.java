package pl.baftek.spitfire.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import pl.baftek.spitfire.enums.BonusDropType;

public class BonusDrop extends GameObject {
    private Texture texture;

    private BonusDropType bonusDropType;

    public BonusDrop(float x, float y) {
        this.x = x;
        this.y = y;
        int random = MathUtils.random(1, 100);

        if (random > 0 && random <= 60) {
            bonusDropType = BonusDropType.SCORE_DROP;
            texture = new Texture("bonus_green.png");
        } else if (random > 60 && random <= 75) {
            bonusDropType = BonusDropType.MG_DROP;
            texture = new Texture("bonus_yellow.png");
        } else if (random > 75 && random <= 90) {
            bonusDropType = BonusDropType.ENGINE_DROP;
            texture = new Texture("bonus_blue.png");
        } else if (random > 90 && random <= 95) {
            bonusDropType = BonusDropType.NUKE_DROP;
            texture = new Texture("bonus_red.png");
        } else {
            bonusDropType = BonusDropType.FAKE;
            texture = new Texture("bomb.png");
        }

        this.width = getTexture().getWidth() + 10;
        this.height = getTexture().getHeight() + 10;
    }

    public void move() {
        super.move();

        if (gameRun) {
            this.y = this.y - 350 * Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * -------------------
     * getters and setters
     */

    public BonusDropType getBonusDropType() {
        return bonusDropType;
    }

    public Texture getTexture() {
        return texture;
    }
}
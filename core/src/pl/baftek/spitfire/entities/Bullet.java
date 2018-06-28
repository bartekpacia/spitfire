package pl.baftek.spitfire.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.enums.PlayerType;

public class Bullet extends GameObject implements Projectile
{
    private static Texture texture;
    private float soundVolume;
    private Sound sound;

    private static final float DAMAGE_SPITFIRE = 0.5f;
    private static final float DAMAGE_IL2 = 0.75f;
    private static final float DAMAGE_MUSTANG = 1.5f;

    private static int bulletSpeed;
    private float damage;

    public Bullet(float x, float y, PlayerType playerType, SpitfireGame game)
    {
        if (playerType == PlayerType.SPITFIRE)
        {
            damage = DAMAGE_SPITFIRE;
            sound = SpitfireGame.ResHelper.normalShootSound;
            soundVolume = 0.5f;
            texture = new Texture("bullet1.png");
            bulletSpeed = 1700;
        }

        else if (playerType == PlayerType.MUSTANG)
        {
            damage = DAMAGE_MUSTANG;
            sound = SpitfireGame.ResHelper.strongShootSound;
            soundVolume = 0.25f;
            texture = new Texture("bullet2.png");
            bulletSpeed = 2200;
        }

        else if (playerType == PlayerType.SZTURMOVIK)
        {
            damage = DAMAGE_IL2;
            sound = SpitfireGame.ResHelper.normalShootSound;
            soundVolume = 0.5f;
            texture = new Texture("bullet1.png");
            bulletSpeed = 1600;
        }

        this.x = x;
        this.y = y;

        this.width = texture.getWidth();
        this.height = texture.getHeight();

        game.playSound(sound, soundVolume);
    }

    public void move()
    {
        super.move();

        if (gameRun)
        {
            this.y = this.y + bulletSpeed * Gdx.graphics.getDeltaTime();
        }
    }

    public Texture getTexture()
    {
        return texture;
    }

    public float getDamage()
    {
        return damage;
    }
}

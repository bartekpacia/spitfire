package pl.baftek.spitfire.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import pl.baftek.spitfire.enums.EnemyType;
import pl.baftek.spitfire.game.SpitfireGame;

public class Enemy extends GameObject
{
    private EnemyType enemyType;

    private static int speedBF109 = 380;
    private static int speedME262 = 530;
    private static int speedJapan155 = 240;
    private static int speedHE111 = 150;

    public static float hpBF109 = 1;
    public static float hpME262 = 1;
    public static float hpHE111 = 15;
    public static float hpJapan155 = 2.5f;

    private float hp;

    private Texture texture;

    public Enemy(EnemyType enemyType, int x, int y)
    {
        this.x = x;
        this.y = y;

        if (enemyType == EnemyType.BF109)
        {
            this.enemyType = enemyType;
            this.speed = speedBF109;
            this.hp = hpBF109;
            texture = SpitfireGame.ResHelper.bf109;
        }

        else if (enemyType == EnemyType.ME262)
        {
            this.enemyType = enemyType;
            this.speed = speedME262;
            this.hp = hpME262;
            texture = SpitfireGame.ResHelper.me262;
        }

        else if (enemyType == EnemyType.HE111)
        {
            this.enemyType = enemyType;
            this.speed = speedHE111;
            this.hp = hpHE111;
            texture = SpitfireGame.ResHelper.he111;
        }

        else if (enemyType == EnemyType.JAPAN155)
        {
            this.enemyType = enemyType;
            this.speed = speedJapan155;
            this.hp = hpJapan155;
            texture = SpitfireGame.ResHelper.japan155;
        }

        else
        {
            System.out.println("Invalid enemy plane model!");
        }


        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    public void move()
    {
        super.move();

        if (gameRun)
        {
            this.y = this.y - speed * Gdx.graphics.getDeltaTime();
        }
    }

    public Texture getTexture()
    {
        return texture;
    }

    public float getHp()
    {
        return hp;
    }

    public void setHp(float hp)
    {
        this.hp = hp;
    }

    public EnemyType getType()
    {
        return enemyType;
    }
}

package pl.baftek.spitfire.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Missile extends GameObject implements Projectile {
    private static final int SPEED = 1200;

    private static final float damage = 10;

    private static final Texture texture = new Texture(Gdx.files.internal("missile.png"));

    public Missile(float x, float y) {
        this.x = x;
        this.y = y;

        this.width = texture.getWidth();
        this.height = texture.getHeight();

    }

    @Override
    public void move() {
        super.move();

        if (gameRun) {
            this.y = this.y + SPEED * Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public float getDamage() {
        return damage;
    }
}

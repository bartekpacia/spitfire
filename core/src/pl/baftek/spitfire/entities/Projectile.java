package pl.baftek.spitfire.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public interface Projectile {
    void move();

    Texture getTexture();

    float getDamage();

    float getX();

    float getY();

    boolean overlaps(Rectangle rectangle);
}

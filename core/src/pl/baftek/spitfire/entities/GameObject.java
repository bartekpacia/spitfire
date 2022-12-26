package pl.baftek.spitfire.entities;


import com.badlogic.gdx.math.Rectangle;
import pl.baftek.spitfire.enums.GameState;
import pl.baftek.spitfire.game.SpitfireGame;

public abstract class GameObject extends Rectangle {
    protected int speed;
    boolean gameRun;

    /**
     * General method for moving. Must always call super(). Before moving must always check if(gameRun){}
     */
    protected void move() {
        gameRun = SpitfireGame.getGameState() == GameState.RUN;
    }
}

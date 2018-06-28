package pl.baftek.spitfire.entities;


import com.badlogic.gdx.math.Rectangle;

import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.enums.GameState;

public abstract class GameObject extends Rectangle
{
    boolean gameRun;
    protected int speed;

    /**
     * General method for moving. Must always call super(). Before moving must always check if(gameRun){}
     */
    protected void move()
    {
        if(SpitfireGame.getGameState() == GameState.RUN)
        {
            gameRun = true;
        }

        else
        {
            gameRun = false;
        }
    };

}

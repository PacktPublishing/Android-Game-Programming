package com.plattysoft.yass.engine;

/**
 * Created by Raul Portales on 03/03/15.
 */
public interface GameObject {

    void startGame();

    void onUpdate(long elapsedMillis, GameEngine gameEngine);

    void onDraw();
}

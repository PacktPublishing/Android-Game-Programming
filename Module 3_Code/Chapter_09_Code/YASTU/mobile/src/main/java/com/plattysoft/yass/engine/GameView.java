package com.plattysoft.yass.engine;

import android.content.Context;

import java.util.List;

/**
 * Created by Raul Portales on 26/03/15.
 */
public interface GameView {

    void draw();

    void setGameObjects(List<List<GameObject>> gameObjects);

    int getWidth();

    int getHeight();

    int getPaddingLeft();

    int getPaddingRight();

    int getPaddingTop();

    int getPaddingBottom();

    Context getContext();

    void postInvalidate();

}

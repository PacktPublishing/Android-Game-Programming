package com.gamecodeschool.c9asteroids;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class AsteroidsView extends GLSurfaceView{

    GameManager gm;

    public AsteroidsView(Context context, int screenX, int screenY) {
        super(context);

        gm = new GameManager(screenX, screenY);

        // Which version of OpenGl we are using
        setEGLContextClientVersion(2);

        // Attach our renderer to the GLSurfaceView
        setRenderer(new AsteroidsRenderer(gm));

    }

}

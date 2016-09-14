package com.gamecodeschool.c10asteroids;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class AsteroidsView extends GLSurfaceView{

    private GameManager gm;
    private SoundManager sm;
    private InputController ic;

    public AsteroidsView(Context context, int screenX, int screenY) {
        super(context);

        sm = new SoundManager();
        sm.loadSound(context);
        gm = new GameManager(screenX, screenY);
        ic = new InputController(screenX, screenY);

        // Which version of OpenGl we are using
        setEGLContextClientVersion(2);

        // Attach our renderer to the GLSurfaceView
        setRenderer(new AsteroidsRenderer(gm, sm, ic));

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ic.handleInput(motionEvent, gm, sm);
        return true;
    }

}

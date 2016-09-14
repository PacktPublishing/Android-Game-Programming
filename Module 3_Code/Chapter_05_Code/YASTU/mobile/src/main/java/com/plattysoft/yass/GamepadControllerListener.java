package com.plattysoft.yass;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by Raul Portales on 16/03/15.
 */
public interface GamepadControllerListener {

    boolean dispatchGenericMotionEvent(MotionEvent event);

    boolean dispatchKeyEvent(KeyEvent event);
}

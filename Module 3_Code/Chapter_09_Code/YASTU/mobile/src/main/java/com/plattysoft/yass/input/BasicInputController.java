package com.plattysoft.yass.input;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.plattysoft.yass.R;

/**
 * Created by Raul Portales on 11/03/15.
 */
public class BasicInputController extends InputController implements View.OnTouchListener {

    public BasicInputController(View view) {
        view.findViewById(R.id.keypad_up).setOnTouchListener(this);
        view.findViewById(R.id.keypad_down).setOnTouchListener(this);
        view.findViewById(R.id.keypad_left).setOnTouchListener(this);
        view.findViewById(R.id.keypad_right).setOnTouchListener(this);
        view.findViewById(R.id.keypad_fire).setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();
        int id = v.getId();
        if (action == MotionEvent.ACTION_DOWN) {
            // User started pressing a key
            if (id == R.id.keypad_up) {
                mVerticalFactor -= 1;
            }
            else if (id == R.id.keypad_down) {
                mVerticalFactor += 1;
            }
            else if (id == R.id.keypad_left) {
                mHorizontalFactor -= 1;
            }
            else if (id == R.id.keypad_right) {
                mHorizontalFactor += 1;
            }
            else if (id == R.id.keypad_fire) {
                mIsFiring = true;
            }
        }
        else if (action == MotionEvent.ACTION_UP) {
            if (id == R.id.keypad_up) {
                mVerticalFactor += 1;
            }
            else if (id == R.id.keypad_down) {
                mVerticalFactor -= 1;
            }
            else if (id == R.id.keypad_left) {
                mHorizontalFactor += 1;
            }
            else if (id == R.id.keypad_right) {
                mHorizontalFactor -= 1;
            }
            else if (id == R.id.keypad_fire) {
                mIsFiring = false;
            }
        }
        return false;
    }
}

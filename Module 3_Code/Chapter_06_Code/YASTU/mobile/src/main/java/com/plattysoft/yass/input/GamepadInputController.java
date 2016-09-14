package com.plattysoft.yass.input;

import android.view.InputDevice;
import android.view.InputDevice.MotionRange;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.plattysoft.yass.GamepadControllerListener;
import com.plattysoft.yass.R;
import com.plattysoft.yass.YassActivity;

/**
 * Created by Raul Portales on 10/03/15.
 */
public class GamepadInputController extends InputController implements GamepadControllerListener {

    private final YassActivity mActivity;

    public GamepadInputController(YassActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onStart() {
        mActivity.setGamepadControllerListener(this);
    }

    @Override
    public void onStop() {
        mActivity.setGamepadControllerListener(null);
    }

    @Override
    public void onResume() {
        mHorizontalFactor = 0;
        mVerticalFactor = 0;
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        int source = event.getSource();

        if ((source & InputDevice.SOURCE_JOYSTICK) != InputDevice.SOURCE_JOYSTICK) {
            return false;
        }
        mHorizontalFactor = event.getAxisValue(MotionEvent.AXIS_X);
        mVerticalFactor = event.getAxisValue(MotionEvent.AXIS_Y);

        InputDevice device = event.getDevice();
        MotionRange rangeX = device.getMotionRange(MotionEvent.AXIS_X, source);
        if (Math.abs(mHorizontalFactor) <= rangeX.getFlat()) {
            mHorizontalFactor = event.getAxisValue(MotionEvent.AXIS_HAT_X);
            MotionRange rangeHatX = device.getMotionRange(MotionEvent.AXIS_HAT_X, source);
            if (Math.abs(mHorizontalFactor) <= rangeHatX.getFlat()) {
                mHorizontalFactor = 0;
            }
        }
        MotionRange rangeY = device.getMotionRange(MotionEvent.AXIS_Y, source);
        if (Math.abs(mVerticalFactor) <= rangeY.getFlat()) {
            mVerticalFactor = event.getAxisValue(MotionEvent.AXIS_HAT_Y);
            MotionRange rangeHatY = device.getMotionRange(MotionEvent.AXIS_HAT_Y, source);
            if (Math.abs(mVerticalFactor) <= rangeHatY.getFlat()) {
                mVerticalFactor = 0;
            }
        }
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        // The keys we want to listen to
        if (action == MotionEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                mVerticalFactor -= 1;
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                mVerticalFactor += 1;
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                mHorizontalFactor -= 1;
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                mHorizontalFactor += 1;
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_BUTTON_A) {
                mIsFiring = true;
                return true;
            }
        }
        else if (action == MotionEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                mVerticalFactor += 1;
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                mVerticalFactor -= 1;
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                mHorizontalFactor += 1;
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                mHorizontalFactor -= 1;
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_BUTTON_A) {
                mIsFiring = false;
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_BUTTON_B) {
                mActivity.onBackPressed();
                return true;
            }
        }
        return false;
    }
}

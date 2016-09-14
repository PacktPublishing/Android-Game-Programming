package com.plattysoft.yass;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.plattysoft.yass.counter.GameFragment;
import com.plattysoft.yass.counter.MainMenuFragment;


public class YassActivity extends Activity {

    private static final String TAG_FRAGMENT = "content";
    private GamepadControllerListener mGamepadControllerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yass);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainMenuFragment(), TAG_FRAGMENT)
                    .commit();
        }
    }

    public void startGame() {
        // Navigate the the game fragment, which makes the start automatically
        navigateToFragment( new GameFragment());
    }

    private void navigateToFragment(YassBaseFragment dst) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, dst, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    public void setGamepadControllerListener(GamepadControllerListener listener) {
        mGamepadControllerListener = listener;
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (mGamepadControllerListener != null) {
            if (mGamepadControllerListener.dispatchGenericMotionEvent(event)) {
                return true;
            }
        }
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent (KeyEvent event) {
        if (mGamepadControllerListener != null) {
            if (mGamepadControllerListener.dispatchKeyEvent(event)) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

//    public void navigateToFragment(Fragment newFragment) {
//        // Set it and do the transaction
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
////        ft.setCustomAnimations(R.animator.fragment_return_enter, R.animator.fragment_return_exit, R.animator.fragment_enter, R.animator.fragment_exit);
//        ft.replace(R.id.container, newFragment, TAG_FRAGMENT);
//        ft.addToBackStack(null);
//        ft.commit();
//    }

    @Override
    public void onBackPressed() {
        final YassBaseFragment fragment = (YassBaseFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void navigateBack() {
        // Do a push on the navigation history
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

}

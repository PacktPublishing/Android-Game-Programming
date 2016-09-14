package com.plattysoft.yass.counter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.plattysoft.yass.YassActivity;
import com.plattysoft.yass.YassBaseFragment;
import com.plattysoft.yass.engine.FPSCounter;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.GameView;
import com.plattysoft.yass.engine.SurfaceGameView;
import com.plattysoft.yass.input.CompositeInputController;
import com.plattysoft.yass.movement.GameController;
import com.plattysoft.yass.movement.ParallaxBackground;
import com.plattysoft.yass.movement.Player;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class GameFragment extends YassBaseFragment implements View.OnClickListener, InputManager.InputDeviceListener {

    private GameEngine mGameEngine;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_play_pause).setOnClickListener(this);
        final ViewTreeObserver obs = view.getViewTreeObserver();
        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeGlobalOnLayoutListener(this);
                }
                else {
                    obs.removeOnGlobalLayoutListener(this);
                }
                GameView gameView = (GameView) getView().findViewById(R.id.gameView);
                mGameEngine = new GameEngine(getActivity(), gameView, 4);
                mGameEngine.setInputController(new CompositeInputController(getView(), getYassActivity()));
                mGameEngine.setSoundManager(getYassActivity().getSoundManager());
                new ParallaxBackground(mGameEngine, 20, R.drawable.seamless_space_0).addToGameEngine(mGameEngine, 0);
//                mGameEngine.addGameObject(new ParallaxBackground(mGameEngine, 30, R.drawable.parallax60), 0);
                new GameController(mGameEngine).addToGameEngine(mGameEngine, 2);
                new Player(mGameEngine).addToGameEngine(mGameEngine, 3);
                new FPSCounter(mGameEngine).addToGameEngine(mGameEngine, 2);
                mGameEngine.startGame();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    InputManager inputManager = (InputManager) getActivity().getSystemService(Context.INPUT_SERVICE);
                    inputManager.registerInputDeviceListener(GameFragment.this, null);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play_pause) {
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGameEngine.isRunning()){
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGameEngine.stopGame();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            InputManager inputManager = (InputManager) getActivity().getSystemService(Context.INPUT_SERVICE);
            inputManager.unregisterInputDeviceListener(this);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mGameEngine.isRunning()){
            pauseGameAndShowPauseDialog();
            return true;
        }
        return false;
    }

    private void pauseGameAndShowPauseDialog() {
        if (mGameEngine.isPaused()) {
            return;
        }
        mGameEngine.pauseGame();
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pause_dialog_title)
                .setMessage(R.string.pause_dialog_message)
                .setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mGameEngine.resumeGame();
                    }
                })
                .setNegativeButton(R.string.stop, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mGameEngine.stopGame();
                        ((YassActivity)getActivity()).navigateBack();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mGameEngine.resumeGame();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {

    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {
        if (mGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onInputDeviceChanged(int deviceId) {

    }
}

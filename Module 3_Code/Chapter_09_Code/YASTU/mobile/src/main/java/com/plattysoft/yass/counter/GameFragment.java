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

import com.plattysoft.yass.GameOverDialog;
import com.plattysoft.yass.PauseDialog;
import com.plattysoft.yass.YassActivity;
import com.plattysoft.yass.YassBaseFragment;
import com.plattysoft.yass.engine.BaseCustomDialog;
import com.plattysoft.yass.engine.FPSCounter;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.R;
import com.plattysoft.yass.engine.GameView;
import com.plattysoft.yass.input.CompositeInputController;
import com.plattysoft.yass.movement.GameController;
import com.plattysoft.yass.movement.ParallaxBackground;
import com.plattysoft.yass.movement.Player;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class GameFragment extends YassBaseFragment implements View.OnClickListener, InputManager.InputDeviceListener, PauseDialog.PauseDialogListener, GameOverDialog.GameOverDialogListener {

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
    }

    @Override
    protected void onLayoutCompleted() {
        prepareAndStartGame();
    }

    private void prepareAndStartGame() {
        GameView gameView = (GameView) getView().findViewById(R.id.gameView);
        mGameEngine = new GameEngine(getActivity(), gameView, 4);
        mGameEngine.setInputController(new CompositeInputController(getView(), getYassActivity()));
        mGameEngine.setSoundManager(getYassActivity().getSoundManager());
        new ParallaxBackground(mGameEngine, 20, R.drawable.seamless_space_0).addToGameEngine(mGameEngine, 0);
        new GameController(mGameEngine, GameFragment.this).addToGameEngine(mGameEngine, 2);
        new FPSCounter(mGameEngine).addToGameEngine(mGameEngine, 2);
        new ScoreGameObject(this, getView(), R.id.score_value).addToGameEngine(mGameEngine, 0);
        new LivesCounter(getView(), R.id.lives_value).addToGameEngine(mGameEngine, 0);
        mGameEngine.startGame();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            InputManager inputManager = (InputManager) getActivity().getSystemService(Context.INPUT_SERVICE);
            inputManager.registerInputDeviceListener(GameFragment.this, null);
        }
        gameView.postInvalidate();
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
        if (mGameEngine.isRunning() && !mGameEngine.isPaused()){
            pauseGameAndShowPauseDialog();
            return true;
        }
        return super.onBackPressed();
    }

    private void pauseGameAndShowPauseDialog() {
        if (mGameEngine.isPaused()) {
            return;
        }
        mGameEngine.pauseGame();
        PauseDialog dialog = new PauseDialog(getYassActivity());
        dialog.setListener(this);
        showDialog(dialog);
    }

    public void resumeGame() {
        mGameEngine.resumeGame();
    }

    @Override
    public void exitGame() {
        mGameEngine.stopGame();
        getYassActivity().navigateBack();
    }

    @Override
    public void startNewGame() {
        // Exit the current game
        mGameEngine.stopGame();
        // Start a new one
        prepareAndStartGame();
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

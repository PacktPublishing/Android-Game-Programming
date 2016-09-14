package com.plattysoft.yass.movement;

import android.graphics.Canvas;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.plattysoft.yass.GameOverDialog;
import com.plattysoft.yass.PauseDialog;
import com.plattysoft.yass.QuitDialog;
import com.plattysoft.yass.R;
import com.plattysoft.yass.YassBaseFragment;
import com.plattysoft.yass.counter.GameFragment;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.GameObject;
import com.plattysoft.yass.sound.GameEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul Portales on 23/03/15.
 */
public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ENEMIES = 500;
    private static final int ASTEROID_POOL_SIZE = 10;
    private static final int INITIAL_LIFES = 3;
    private final GameFragment mParent;

    private long mCurrentMillis;
    private List<Asteroid> mAsteroidPool = new ArrayList<Asteroid>();

    private int mEnemiesSpawned;

    private GameControllerState mState;
    private int mWaitingTime;

    private static final int STOPPING_WAVE_WAITING_TIME = 2000;
    private int NEXT_WAVE_WAITING_TIME = 3000; // Time since the player is placed and Asteroids start to go down.

    private int mNumLives;

    public GameController(GameEngine gameEngine, GameFragment parent) {
        double pixelFactor = gameEngine.mPixelFactor;
        mParent = parent;
        // We initialize the pool of items now
        for (int i=0; i<ASTEROID_POOL_SIZE; i++) {
            mAsteroidPool.add(new Asteroid(this, gameEngine));
        }
    }

    @Override
    public void startGame(GameEngine gameEngine) {
        mCurrentMillis = 0;
        mEnemiesSpawned = 0;
        mWaitingTime = 0;
        for (int i=0; i<INITIAL_LIFES; i++) {
            gameEngine.onGameEvent(GameEvent.LifeAdded);
        }
        mState = GameControllerState.PlacingSpaceship;
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.SpaceshipHit) {
            mState = GameControllerState.StoppingWave;
            mWaitingTime = 0;
        }
        else if (gameEvent == GameEvent.GameOver) {
            mState = GameControllerState.GameOver;
            showGameOverDialog();
        }
        else if (gameEvent == GameEvent.LifeAdded) {
            mNumLives++;
        }
    }

    private void showGameOverDialog() {
        mParent.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GameOverDialog quitDialog = new GameOverDialog(mParent);
                quitDialog.setListener(mParent);
                mParent.showDialog(quitDialog);
            }
        });
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if (mState == GameControllerState.SpawningEnemies) {
            mCurrentMillis += elapsedMillis;
            long waveTimestamp = mEnemiesSpawned * TIME_BETWEEN_ENEMIES; // - mWaveStartingTimestamp[i];
            if (mCurrentMillis > waveTimestamp) {
                // Spawn a new enemy
                Asteroid a = mAsteroidPool.remove(0);
                a.init(gameEngine);
                a.addToGameEngine(gameEngine, mLayer);
                mEnemiesSpawned++;
                return;
            }
        }
        else if (mState == GameControllerState.StoppingWave) {
            mWaitingTime += elapsedMillis;
            if (mWaitingTime > STOPPING_WAVE_WAITING_TIME) {
                mState = GameControllerState.PlacingSpaceship;
            }
        }
        else if (mState == GameControllerState.PlacingSpaceship) {
            if (mNumLives == 0){
                gameEngine.onGameEvent(GameEvent.GameOver);
            }
            else {
                mNumLives--;
                gameEngine.onGameEvent(GameEvent.LifeLost);
                Player newLife = new Player(gameEngine);
                newLife.addToGameEngine(gameEngine, 3);
                newLife.startGame(gameEngine);
                // We wait to start spawning more enemies
                mState = GameControllerState.Waiting;
                mWaitingTime = 0;
            }
        }
        else if (mState == GameControllerState.Waiting) {
            mWaitingTime += elapsedMillis;
            if (mWaitingTime > NEXT_WAVE_WAITING_TIME) {
                mState = GameControllerState.SpawningEnemies;
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    public void returnToPool(Asteroid asteroid) {
        mAsteroidPool.add(asteroid);
    }
}

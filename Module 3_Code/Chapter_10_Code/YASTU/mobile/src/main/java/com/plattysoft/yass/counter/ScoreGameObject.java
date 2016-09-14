package com.plattysoft.yass.counter;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.Games;
import com.plattysoft.yass.R;
import com.plattysoft.yass.YassBaseFragment;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.engine.GameObject;
import com.plattysoft.yass.movement.GameControllerState;
import com.plattysoft.yass.sound.GameEvent;

/**
 * Created by Raul Portales on 03/03/15.
 */
public class ScoreGameObject extends GameObject {

    private static final int POINTS_LOSS_PER_ASTEROID_MISSED = 1;
    private static final int POINTS_GAINED_PER_ASTEROID_HIT = 50;

    private final YassBaseFragment mParent;

    private final TextView mText;
    private final GoogleApiClient mApiClient;
    private int mPoints;
    private boolean mPointsHaveChanged;

    private long mTimeWithoutDie;
    private int mConsecutiveMisses;
    private int mConsecutiveHits;

    public ScoreGameObject(YassBaseFragment parent, View view, int viewResId) {
        mText = (TextView) view.findViewById(viewResId);
        mParent = parent;
        mApiClient = mParent.getYassActivity().getGameHelper().getApiClient();
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mTimeWithoutDie += elapsedMillis;
        if (mTimeWithoutDie > 60000) {
            unlockSafe(R.string.achievement_survivor);
        }
    }

    private void unlockSafe(int resId) {
        if (mApiClient.isConnecting() || mApiClient.isConnected()) {
            try {
                Games.Achievements.unlock(mApiClient, getString(resId));
            } catch (Exception e) {
                mApiClient.disconnect();
            }
        }
    }

    @Override
    public void startGame(GameEngine gameEngine) {
        mTimeWithoutDie = 0;
        mPoints = 0;
        mConsecutiveHits = 0;
        mConsecutiveMisses = 0;
        mText.post(mUpdateTextRunnable);
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.AsteroidHit) {
            mPoints += POINTS_GAINED_PER_ASTEROID_HIT;
            mPointsHaveChanged = true;
            mConsecutiveMisses = 0;
            mConsecutiveHits++;
            checkAsteroidHitRelatedAchievements();
        }
        else if (gameEvent == GameEvent.BulletMissed) {
            mConsecutiveMisses++;
            mConsecutiveHits = 0;
            if (mConsecutiveMisses >= 20) {
                unlockSafe(R.string.achievement_target_lost);
            }
        }
        else if (gameEvent == GameEvent.SpaceshipHit) {
            mTimeWithoutDie = 0;
        }
        else if (gameEvent == GameEvent.AsteroidMissed) {
            if (mPoints > 0) {
                mPoints -= POINTS_LOSS_PER_ASTEROID_MISSED;
            }
            mPointsHaveChanged = true;
        }
        else if (gameEvent == GameEvent.GameFinished) {
            // Submit the score
            if (mApiClient.isConnecting() || mApiClient.isConnected()) {
                try {
                    Games.Leaderboards.submitScore(mApiClient, getLeaderboardId(), mPoints);
                }
                catch (Exception e){
                    // If the user signed out in this execution of the game we end up here.
                    // It's fine, we just disconnect the client to avoid more problems later
                    mApiClient.disconnect();
                }
            }
        }
    }

    private void checkAsteroidHitRelatedAchievements() {
        if (mPoints > 100000) {
           // Unlock achievement
           unlockSafe(R.string.achievement_big_score);
        }
        if (mConsecutiveHits >= 20) {
            unlockSafe(R.string.achievement_target_aquired);
        }
        // Increment achievement of asteroids hit
        if (mApiClient.isConnecting() || mApiClient.isConnected()) {
            try {
                Games.Achievements.increment(mApiClient, getString(R.string.achievement_asteroid_killer), 1);
            } catch (Exception e) {
                mApiClient.disconnect();
            }
        }
    }

    private String getString(int resId) {
        return mParent.getString(resId);
    }

    private String getLeaderboardId() {
        return mParent.getString(R.string.leaderboard_high_scores);
    }

    private Runnable mUpdateTextRunnable = new Runnable() {
        @Override
        public void run() {
            String text = String.format("%06d", mPoints);
            mText.setText(text);
        }
    };

    @Override
    public void onDraw(Canvas canvas) {
        if (mPointsHaveChanged) {
            mText.post(mUpdateTextRunnable);
            mPointsHaveChanged = false;
        }
    }
}

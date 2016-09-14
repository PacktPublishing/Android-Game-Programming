package com.plattysoft.yass;

import android.view.View;

import com.plattysoft.yass.counter.GameFragment;
import com.plattysoft.yass.engine.BaseCustomDialog;

/**
 * Created by Raul Portales on 16/04/15.
 */
public class GameOverDialog extends BaseCustomDialog implements View.OnClickListener {

    private GameOverDialogListener mListener;

    public GameOverDialog(GameFragment parent) {
        super(parent.getYassActivity());
        setContentView(R.layout.dialog_game_over);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_resume).setOnClickListener(this);
     }

    public void setListener (GameOverDialogListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_exit) {
            dismiss();
            mListener.exitGame();
        }
        else if (v.getId() == R.id.btn_resume) {
            dismiss();
            mListener.startNewGame();
        }
    }

    public interface GameOverDialogListener {
        void exitGame();

        void startNewGame();
    }
}

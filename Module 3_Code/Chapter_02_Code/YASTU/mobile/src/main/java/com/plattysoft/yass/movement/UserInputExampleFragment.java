package com.plattysoft.yass.movement;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.plattysoft.yass.R;
import com.plattysoft.yass.YassBaseFragment;
import com.plattysoft.yass.counter.ScoreGameObject;
import com.plattysoft.yass.engine.GameEngine;
import com.plattysoft.yass.input.BasicInputController;

/**
 * Created by Raul Portales on 10/03/15.
 */
public class UserInputExampleFragment extends YassBaseFragment {

    private GameEngine mGameEngine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_imput_example, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewTreeObserver obs = view.getViewTreeObserver();
        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeGlobalOnLayoutListener(this);
                } else {
                    obs.removeOnGlobalLayoutListener(this);
                }
                mGameEngine = new GameEngine(getActivity());
//        mGameEngine.addGameObject(new ScoreGameObject(view, R.id.txt_score));
//        view.findViewById(R.id.btn_play_pause).setOnClickListener(this);
                mGameEngine.setInputController(new BasicInputController(getView()));
                mGameEngine.addGameObject(new Player(getView()));
                mGameEngine.startGame();
            }
        });
    }
}

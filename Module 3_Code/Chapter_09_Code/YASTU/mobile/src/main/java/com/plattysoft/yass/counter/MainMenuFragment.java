package com.plattysoft.yass.counter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.plattysoft.yass.QuitDialog;
import com.plattysoft.yass.R;
import com.plattysoft.yass.YassBaseFragment;
import com.plattysoft.yass.sound.SoundManager;

import java.util.Random;

/**
 * Created by Raul Portales on 03/03/15.
 */
public class MainMenuFragment extends YassBaseFragment implements View.OnClickListener, QuitDialog.QuitDialogListener {

    private static final String PREF_SHOULD_DISPLAY_GAMEPAD_HELP = "com.example.yass.display.gamepad.help.boolean";
    public static final int TITLE_ANIMATION_DURATION = 1600;
    private static final long TITLE_START_DELAY = 400;

    private static final int REQUEST_ACHIEVEMENTS = 1;
    private static final int REQUEST_LEADERBOARDS = 2;

    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(this);
        view.findViewById(R.id.btn_sound).setOnClickListener(this);
        view.findViewById(R.id.btn_music).setOnClickListener(this);

        view.findViewById(R.id.btn_achievements).setOnClickListener(this);
        view.findViewById(R.id.btn_leaderboards).setOnClickListener(this);
        view.findViewById(R.id.btn_sign_in).setOnClickListener(this);

        Animation pulseAnimation = AnimationUtils.loadAnimation(getActivity(), R.animator.button_pulse);
        view.findViewById(R.id.btn_start).startAnimation(pulseAnimation);

        updateSoundAndMusicButtons();
    }

    @Override
    protected void onLayoutCompleted() {
        animateTitles();
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 42f);
        animation.setDuration(1000);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float currentValue = (Float) animation.getAnimatedValue();
                // Do something with the value
            }
        });
        animation.start();
        updatePlayButtons();
//        ImageView iv = (ImageView) getView().findViewById(R.id.ship_animated);
//        ((AnimationDrawable)iv.getDrawable()).start();

//        animateShip();
    }

    @Override
    public void onSignInFailed() {
        updatePlayButtons();
    }

    @Override
    public void onSignInSucceeded() {
        updatePlayButtons();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // when we sign out
        updatePlayButtons();
    }

    private void updatePlayButtons() {
        GameHelper gameHelper = getYassActivity().getGameHelper();
        if (gameHelper.isConnecting() || gameHelper.isSignedIn()) {
            getView().findViewById(R.id.btn_achievements).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.btn_leaderboards).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.btn_sign_in).setVisibility(View.GONE);
        }
        else {
            getView().findViewById(R.id.btn_achievements).setVisibility(View.GONE);
            getView().findViewById(R.id.btn_leaderboards).setVisibility(View.GONE);
            getView().findViewById(R.id.btn_sign_in).setVisibility(View.VISIBLE);
        }
    }

    private void animateShip() {
        try {
            View iv = getView().findViewById(R.id.ship_animated);
            // Get a random position on the screen
            Random r = new Random();
            int targetX = r.nextInt(getView().getWidth());
            int targetY = r.nextInt(getView().getHeight());
            // Animate
            iv.animate()
                    .x(targetX)
                    .y(targetY)
                    .setDuration(500)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animateShip();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
        }
        catch (NullPointerException e) {
            // Teh view has been detached, just don't crash
        }
    }

    private void animateTitles() {
        View title = getView().findViewById(R.id.main_title);
//        title.setTranslationX(-title.getX() - title.getWidth());

//        int duration = getResources().getInteger(R.integer.tittle_duration);
//        int startOffset = getResources().getInteger(R.integer.subtitle_start_offset);
//
//        title.animate()
//                .translationX(0)
//                .setStartDelay(startOffset)
//                .setDuration(duration)
//                .setInterpolator(new BounceInterpolator())
//                .start();

        Animation titleAnimation = AnimationUtils.loadAnimation(getActivity(), R.animator.title_enter);
        title.startAnimation(titleAnimation);

//        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.title_enter_property);
//        set.setTarget(title);
//        set.start();

        // Then the subtitle
        View subtitle = getView().findViewById(R.id.main_subtitle);
//        subtitle.setAlpha(0);
//
//        int subtitleDuration = getResources().getInteger(R.integer.subtitle_duration);
//        int subtitleStartOffset = getResources().getInteger(R.integer.subtitle_start_offset);
//
//        subtitle.animate()
//                .alpha(1)
//                .setDuration(subtitleDuration)
//                .setStartDelay(subtitleStartOffset)
//                .setInterpolator(new DecelerateInterpolator())
//                .start();

        Animation subtitleAnimation = AnimationUtils.loadAnimation(getActivity(), R.animator.subtitle_enter);
        subtitle.startAnimation(subtitleAnimation);

//        subtitle.setAlpha(0);
//        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.subtitle_enter_property);
//        set.setTarget(subtitle);
//        set.start();

    }

    private void updateSoundAndMusicButtons() {
        SoundManager soundManager = getYassActivity().getSoundManager();
        boolean music = soundManager.getMusicStatus();
        ImageView btnMusic = (ImageView) getView().findViewById(R.id.btn_music);
        if (music) {
            btnMusic.setImageResource(R.drawable.music_on_no_bg);
        }
        else {
            btnMusic.setImageResource(R.drawable.music_off_no_bg);
        }
        boolean sound = soundManager.getSoundStatus();
        ImageView btnSounds= (ImageView) getView().findViewById(R.id.btn_sound);
        if (sound) {
            btnSounds.setImageResource(R.drawable.sounds_on_no_bg);
        }
        else {
            btnSounds.setImageResource(R.drawable.sounds_off_no_bg);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start){
           getYassActivity().startGame();
        }
        else if (v.getId() == R.id.btn_music) {
            SoundManager soundManager = getYassActivity().getSoundManager();
            soundManager.toggleMusicStatus();
            updateSoundAndMusicButtons();
        }
        else if (v.getId() == R.id.btn_sound) {
            SoundManager soundManager = getYassActivity().getSoundManager();
            soundManager.toggleSoundStatus();
            updateSoundAndMusicButtons();
        }
        else if (v.getId() == R.id.btn_achievements) {
            try {
                GoogleApiClient apiClient = getYassActivity().getGameHelper().getApiClient();
                Intent achievementsIntent = Games.Achievements.getAchievementsIntent(apiClient);
                startActivityForResult(achievementsIntent, REQUEST_ACHIEVEMENTS);
            }
            catch (Exception e) {
                GameHelper gameHelper = getYassActivity().getGameHelper();
                gameHelper.disconnect();
                gameHelper.beginUserInitiatedSignIn();
            }
        }
        else if (v.getId() == R.id.btn_leaderboards) {
            try {
                GoogleApiClient apiClient = getYassActivity().getGameHelper().getApiClient();
                Intent leaderboardsIntent = Games.Leaderboards.getLeaderboardIntent(apiClient, getString(R.string.leaderboard_high_scores));
                startActivityForResult(leaderboardsIntent, REQUEST_LEADERBOARDS);
            }
            catch (Exception e) {
                GameHelper gameHelper = getYassActivity().getGameHelper();
                gameHelper.disconnect();
                gameHelper.beginUserInitiatedSignIn();
            }
        }
        else if (v.getId() == R.id.btn_sign_in) {
            getYassActivity().getGameHelper().beginUserInitiatedSignIn();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGameControllerConnected() && shouldDisplayGamepadHelp()) {
            displayGamepadHelp();
            // Do not show the dialog again
            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putBoolean(PREF_SHOULD_DISPLAY_GAMEPAD_HELP, false)
                    .commit();
        }
    }

    private void displayGamepadHelp() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.gampad_help_title)
                .setMessage(R.string.gamepad_help_message)
                .create()
                .show();
    }

    private boolean shouldDisplayGamepadHelp() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean(PREF_SHOULD_DISPLAY_GAMEPAD_HELP, true);
    }

    public boolean isGameControllerConnected() {
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) ||
                ((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBackPressed() {
        boolean consumed = super.onBackPressed();
        if (!consumed){
            QuitDialog quitDialog = new QuitDialog(getYassActivity());
            quitDialog.setListener(this);
            showDialog(quitDialog);
            return true;
        }
        return consumed;
    }

    @Override
    public void exit() {
        getYassActivity().finish();
    }
}

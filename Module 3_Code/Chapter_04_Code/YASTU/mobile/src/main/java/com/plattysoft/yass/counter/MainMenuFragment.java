package com.plattysoft.yass.counter;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plattysoft.yass.YassActivity;
import com.plattysoft.yass.R;
import com.plattysoft.yass.YassBaseFragment;

/**
 * Created by Raul Portales on 03/03/15.
 */
public class MainMenuFragment extends YassBaseFragment implements View.OnClickListener {

    private static final String PREF_SHOULD_DISPLAY_GAMEPAD_HELP = "com.example.yass.display.gamepad.help.boolean";

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
    }

    @Override
    public void onClick(View v) {
        ((YassActivity)getActivity()).startGame();
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
}

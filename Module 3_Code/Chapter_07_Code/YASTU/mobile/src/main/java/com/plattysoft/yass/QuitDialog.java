package com.plattysoft.yass;

import android.view.View;
import android.widget.ImageView;

import com.plattysoft.yass.engine.BaseCustomDialog;
import com.plattysoft.yass.sound.SoundManager;

/**
 * Created by Raul Portales on 16/04/15.
 */
public class QuitDialog extends BaseCustomDialog implements View.OnClickListener {

    private QuitDialogListener mListener;

    public QuitDialog(YassActivity activity) {
        super(activity);
        setContentView(R.layout.dialog_quit);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_resume).setOnClickListener(this);
     }

    public void setListener(QuitDialogListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_exit) {
            dismiss();
            mListener.exit();
        }
        else if (v.getId() == R.id.btn_resume) {
            dismiss();
        }
    }

    public interface QuitDialogListener {
        void exit();
    }
}

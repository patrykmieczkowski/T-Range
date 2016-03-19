package com.kitowcy.t_range.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kitowcy.t_range.R;

import org.w3c.dom.Text;

/**
 * Created by Patryk Mieczkowski on 19.03.16.
 */
public class SendDialog extends Dialog {

    public Activity activity;
    public Dialog d;
    public String text;
    public TextView infoText;


    public SendDialog(Activity a, String text) {
        super(a);
        this.activity = a;
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        setContentView(R.layout.send_dialog_layout);
        infoText = (TextView) findViewById(R.id.info_text);
        infoText.setText(text);

    }

}

package com.kitowcy.t_range.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Patryk Mieczkowski on 19.03.16.
 */
public class DialogHelper {

    public static void showAlertNoInternet(Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your request has been send!")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

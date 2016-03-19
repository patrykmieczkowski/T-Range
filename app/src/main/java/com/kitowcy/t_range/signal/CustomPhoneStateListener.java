package com.kitowcy.t_range.signal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;

import com.kitowcy.t_range.App;
import com.kitowcy.t_range.MainActivity;
import com.kitowcy.t_range.utils.NotificationBuilder;

/**
 * Created by Paulina on 2016-03-18.
 */
public class CustomPhoneStateListener extends PhoneStateListener {
    Context mContext;
    public static String LOG_TAG = "CustomPhoneStateListener";
    public int previousSignalLevel = 1;
    public static final String TAG = CustomPhoneStateListener.class.getSimpleName();
    public static final String NotificationPREFERENCES = "NotificationSharedPreferences";
    public static final String noSignalKey = "noSignal";
    public static final String signalBackKey = "signalBack";
    SharedPreferences sharedpreferences;

    public CustomPhoneStateListener(Context context) {
        mContext = context;
        sharedpreferences = context.getSharedPreferences(NotificationPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        Log.i(LOG_TAG, "onSignalStrengthsChanged: " + signalStrength);
        if (signalStrength.isGsm()) {

            //sending broadcast to MainActivity about signal chenged and level
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.mBroadcastSignalLevel);
            broadcastIntent.putExtra("Signal level", signalStrength.getLevel());
            broadcastIntent.putExtra("Signal strength", signalStrength.getCdmaDbm());
            mContext.sendBroadcast(broadcastIntent);

            //sending broadcast to MainActivity about loosing signal
            if (signalStrength.getLevel() == 0) {
                Log.d(TAG, "No signal");
                Boolean noSignalFlag = sharedpreferences.getBoolean(noSignalKey, false);
                if(noSignalFlag) {
                    NotificationBuilder.createNotification(App.INSTANCE.getApplicationContext(), "Signal lost!", "You lost your signal :(");
                }
                Intent broadcastIntentNoSignal = new Intent();
                broadcastIntentNoSignal.setAction(MainActivity.mBroadcastNoSignal);
                mContext.sendBroadcast(broadcastIntentNoSignal);
            }

            //sending broadcast to MainActivity about getting back signal
            if (previousSignalLevel == 0 && signalStrength.getLevel() > previousSignalLevel) {
                Log.d(TAG, "Signal back");
                Boolean signalBackFlag = sharedpreferences.getBoolean(signalBackKey, false);
                if(signalBackFlag){
                    NotificationBuilder.createNotification(App.INSTANCE.getApplicationContext(), "Signal is back!", "Yay! Signal is here again :)");
                }
                Intent broadcastIntentSignalBack = new Intent();
                broadcastIntentSignalBack.setAction(MainActivity.mBroadcastSignalBack);
                mContext.sendBroadcast(broadcastIntentSignalBack);
            }

            previousSignalLevel = signalStrength.getLevel();
        }
    }
}

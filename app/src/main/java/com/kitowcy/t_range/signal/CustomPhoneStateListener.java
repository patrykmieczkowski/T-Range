package com.kitowcy.t_range.signal;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;

import com.kitowcy.t_range.MainActivity;

/**
 * Created by Paulina on 2016-03-18.
 */
public class CustomPhoneStateListener extends PhoneStateListener {
    Context mContext;
    public static String LOG_TAG = "CustomPhoneStateListener";

    public CustomPhoneStateListener(Context context) {
        mContext = context;
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        Log.i(LOG_TAG, "onSignalStrengthsChanged: " + signalStrength);
        if (signalStrength.isGsm()) {
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getGsmBitErrorRate "
                    + signalStrength.getGsmBitErrorRate());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getGsmSignalStrength "
                    + signalStrength.getGsmSignalStrength());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getGsmSignalStrength level "
                    + signalStrength.getLevel());

            //sending broadcast to MainActivity
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.mBroadcastSignalLevel);
            broadcastIntent.putExtra("Signal level", signalStrength.getLevel());
            mContext.sendBroadcast(broadcastIntent);

        } else if (signalStrength.getCdmaDbm() > 0) {
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getCdmaDbm "
                    + signalStrength.getCdmaDbm());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getCdmaEcio "
                    + signalStrength.getCdmaEcio());
        } else {
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getEvdoDbm "
                    + signalStrength.getEvdoDbm());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getEvdoEcio "
                    + signalStrength.getEvdoEcio());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getEvdoSnr "
                    + signalStrength.getEvdoSnr());
        }
    }
}

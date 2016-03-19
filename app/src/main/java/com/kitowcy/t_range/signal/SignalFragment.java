package com.kitowcy.t_range.signal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitowcy.t_range.MainActivity;
import com.kitowcy.t_range.R;
import com.kitowcy.t_range.utils.NotificationBuilder;

public class SignalFragment extends Fragment {
    public static final String TAG = SignalFragment.class.getSimpleName();
    public int signalLevel;
    private IntentFilter mIntentFilter;

    public static SignalFragment newInstance() {
        SignalFragment fragment = new SignalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(MainActivity.mBroadcastNoSignal);
        mIntentFilter.addAction(MainActivity.mBroadcastSignalBack);
        mIntentFilter.addAction(MainActivity.mBroadcastSignalLevel);

    }

    @Override
    public void onResume() {
        super.onResume();
       // registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signal, container, false);
        return v;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MainActivity.mBroadcastNoSignal)) {
                Log.d(TAG, "No signal!");
                NotificationBuilder.createNotification(context, "Signal lost!", "You lost your signal :(");
            }
            if (intent.getAction().equals(MainActivity.mBroadcastSignalBack)) {
                Log.d(TAG, "Signal back");
            }
            if (intent.getAction().equals(MainActivity.mBroadcastSignalLevel)) {
                signalLevel = intent.getIntExtra("Signal level", -1);
                Log.d(TAG, "Signal level " + signalLevel);
            }
        }
    };

}

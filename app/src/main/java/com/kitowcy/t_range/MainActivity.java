package com.kitowcy.t_range;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kitowcy.t_range.signal.BroadcastSignalStateService;
import com.kitowcy.t_range.utils.NotificationBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static String POSITION = "POSITION";
    public static final String mBroadcastSignalLevel = "SIGNAL_LEVEL";
    public static final String mBroadcastNoSignal = "NO_SIGNAL";
    public static final String mBroadcastSignalBack = "SIGNAL_BACK";
    public int signalLevel;

    private IntentFilter mIntentFilter;

    @Bind(R.id.main_view_pager)
    ViewPager mainViewPager;

    @Bind(R.id.slide_tab)
    TabLayout slideTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prepareViewPager();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastNoSignal);
        mIntentFilter.addAction(mBroadcastSignalBack);
        mIntentFilter.addAction(mBroadcastSignalLevel);

        Intent serviceIntent = new Intent(this, BroadcastSignalStateService.class);
        startService(serviceIntent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mainViewPager.getCurrentItem());
        Log.d(TAG, "onSaveInstanceState: saved pager item: " + mainViewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mainViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
        Log.d(TAG, "onRestoreInstanceState: saved pager: " + savedInstanceState.getInt(POSITION));
    }

    private void prepareViewPager() {
        Log.d(TAG, "prepareViewPager()");

        MainViewPagerAdapter mvpa = new MainViewPagerAdapter(getSupportFragmentManager(), this);
        mainViewPager.setAdapter(mvpa);
        slideTab.setupWithViewPager(mainViewPager);
//        for (int x = 0; x < slideTab.getTabCount(); x++) {
//            slideTab.getTabAt(x).setIcon(R.drawable.signal_icon);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mBroadcastNoSignal)) {
                Log.d(TAG, "No signal!");
                NotificationBuilder.createNotification(context, "Signal lost!", "You lost your signal :(");
            }
            if (intent.getAction().equals(mBroadcastSignalBack)) {
                Log.d(TAG, "Signal back");
            }
            if (intent.getAction().equals(mBroadcastSignalLevel)) {
                signalLevel = intent.getIntExtra("Signal level", -1);
                Log.d(TAG, "Signal level " + signalLevel);
            }
        }
    };

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }
}

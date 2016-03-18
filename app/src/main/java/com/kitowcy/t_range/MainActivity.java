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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static String POSITION = "POSITION";
    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    public static final String mBroadcastIntegerAction = "com.truiton.broadcast.integer";
    public static final String mBroadcastArrayListAction = "com.truiton.broadcast.arraylist";
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
        mIntentFilter.addAction(mBroadcastStringAction);
        mIntentFilter.addAction(mBroadcastIntegerAction);
        mIntentFilter.addAction(mBroadcastArrayListAction);

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
            Log.d(TAG, "");
            Log.e(TAG,"");
            if (intent.getAction().equals(mBroadcastStringAction)) {
                Log.d(TAG, intent.getStringExtra("Data") + "\n\n");
            } else if (intent.getAction().equals(mBroadcastIntegerAction)) {
                Log.d(TAG, intent.getIntExtra("Data", 0) + "\n\n");
            } else if (intent.getAction().equals(mBroadcastArrayListAction)) {
                Log.d(TAG, intent.getStringArrayListExtra("Data").toString()
                        + "\n\n");
                Intent stopIntent = new Intent(MainActivity.this,
                        BroadcastSignalStateService.class);
                stopService(stopIntent);
            }
        }
    };

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }
}

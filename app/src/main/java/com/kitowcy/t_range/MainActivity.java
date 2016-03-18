package com.kitowcy.t_range;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kitowcy.t_range.signal.BroadcastSignalStateService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    public static final String mBroadcastIntegerAction = "com.truiton.broadcast.integer";
    public static final String mBroadcastArrayListAction = "com.truiton.broadcast.arraylist";
    private IntentFilter mIntentFilter;

    @Bind(R.id.main_view_pager)
    ViewPager mainViewPager;

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


    private void prepareViewPager() {
        Log.d(TAG, "prepareViewPager()");

        MainViewPagerAdapter mvpa = new MainViewPagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(mvpa);
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

package com.kitowcy.t_range;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static String POSITION = "POSITION";

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
}

package com.kitowcy.t_range;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.Bind;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.main_view_pager)
    ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareViewPager();
    }

    private void prepareViewPager() {
        Log.d(TAG, "prepareViewPager()");

        MainViewPagerAdapter mvpa = new MainViewPagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(mvpa);
    }
}

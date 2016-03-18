package com.kitowcy.t_range;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kitowcy.t_range.signal.SignalFragment;

/**
 * Created by Patryk Mieczkowski on 18.03.16.
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    String[] titles = {"Alert", "Message"};

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SignalFragment.newInstance();
            default:
                return new SearchFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}

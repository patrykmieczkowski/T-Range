package com.kitowcy.t_range;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.kitowcy.t_range.search.SearchFragment;
import com.kitowcy.t_range.settings.SettingsFragment;
import com.kitowcy.t_range.signal.SignalFragment;

/**
 * @author Patryk Mieczkowski
 * @since 18.03.16.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    String[] titles = {"Alert", "Message", "Settings"};
    Context context;

    public MainViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SignalFragment.newInstance();
            case 1:
                return new SearchFragment();
            case 2:
                return SettingsFragment.newInstance();
            default:
                return SignalFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//
//        Drawable image = ContextCompat.getDrawable(context, R.drawable.signal_icon);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//        SpannableString sb = new SpannableString(titles[position]);
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return sb;
//    }

}

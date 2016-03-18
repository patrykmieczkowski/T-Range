package com.kitowcy.t_range;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kitowcy.t_range.search.AdditionalSearchActivity;
import com.kitowcy.t_range.search.Contact;
import com.kitowcy.t_range.search.SearchFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.main_view_pager)
    ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prepareViewPager();
    }

    private void prepareViewPager() {
        Log.d(TAG, "prepareViewPager()");

        MainViewPagerAdapter mvpa = new MainViewPagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(mvpa);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (data != null) {
            Contact c = (Contact) data.getSerializableExtra(AdditionalSearchActivity.CONTACT);
            if (c != null) {
                Fragment fragment = App.INSTANCE.currentFragment;
                if (fragment instanceof SearchFragment) {
                    SearchFragment fragg = (SearchFragment) fragment;
                    fragg.contactChosen = true;
                    fragg.startAnimateRecorder(c);
                } else Log.e(TAG, "onActivityResult: " + c);
            }
        }
    }
}

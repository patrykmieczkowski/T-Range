package com.kitowcy.t_range;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kitowcy.t_range.search.AdditionalSearchActivity;
import com.kitowcy.t_range.search.Contact;
import com.kitowcy.t_range.search.SearchFragment;
import com.kitowcy.t_range.signal.BroadcastSignalStateService;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

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

        Intent serviceIntent = new Intent(this, BroadcastSignalStateService.class);
        startService(serviceIntent);

        prepareMockData();
    }

    private void prepareMockData() {
        Log.d(TAG, "prepareMockData: ");
        Realm realm = Realm.getInstance(this);
        RealmResults<RealmLocation> locations = realm.allObjects(RealmLocation.class);
        if (locations != null && locations.size() > 0) {
            realm.beginTransaction();
            locations.clear();
            realm.commitTransaction();

        }
        realm.beginTransaction();

        double latt = 50.06799f;
        double lonn = 19.9128143f;
        Random random = new Random();
        for (int j = 0; j < 10; j++) {
            int sgn = random.nextBoolean() ? -1 : 1;
            int sgn2 = random.nextBoolean() ? -1 : 1;
            double lat = latt + (random.nextFloat() + sgn * .5f) / 40;
            double lon = lonn + (random.nextFloat() + sgn2 * .5f) / 40;
            int strength = random.nextInt(5);
            String name = "Position " + strength;
            RealmLocation location = new RealmLocation(lat, lon, name, strength);
            realm.copyToRealmOrUpdate(location);
        }
        realm.commitTransaction();
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

        View view1 = getLayoutInflater().inflate(R.layout.tab_layout_custom_layout, null);
        View view2 = getLayoutInflater().inflate(R.layout.tab_layout_custom_layout, null);
        View view3 = getLayoutInflater().inflate(R.layout.tab_layout_custom_layout, null);
        View view4 = getLayoutInflater().inflate(R.layout.tab_layout_custom_layout, null);

        view1.findViewById(R.id.tab_layout_icon).setBackgroundResource(R.drawable.icon_signal);
        view2.findViewById(R.id.tab_layout_icon).setBackgroundResource(R.drawable.icon_message);
        view3.findViewById(R.id.tab_layout_icon).setBackgroundResource(R.drawable.icon_map);
        view4.findViewById(R.id.tab_layout_icon).setBackgroundResource(R.drawable.icon_settings);

        slideTab.getTabAt(0).setCustomView(view1);
        slideTab.getTabAt(1).setCustomView(view2);
        slideTab.getTabAt(2).setCustomView(view3);
        slideTab.getTabAt(3).setCustomView(view4);

    }

    @Override
    protected void onDestroy() {
        if (!App.INSTANCE.subscription.isUnsubscribed())
            App.INSTANCE.subscription.unsubscribe();
        super.onDestroy();
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

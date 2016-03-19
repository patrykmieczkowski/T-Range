package com.kitowcy.t_range.signal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitowcy.t_range.App;
import com.kitowcy.t_range.MainActivity;
import com.kitowcy.t_range.R;
import com.kitowcy.t_range.dialog.SendDialog;
import com.kitowcy.t_range.utils.AnimateUtils;
import com.kitowcy.t_range.utils.GeocodingUtils;

import java.util.concurrent.atomic.AtomicBoolean;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SignalFragment extends Fragment {
    public static final String TAG = SignalFragment.class.getSimpleName();
    public int signalLevel;
    public int signalStrength;
    private IntentFilter mIntentFilter;

    public static final String PERFECT = "perfect";
    public static final String GOOD = "good";
    public static final String VERY_GOOD = "very good";
    public static final String WEAK = "week";
    public static final String NO_SIGNAL = "no signal";
    android.os.Handler handler = new Handler();
    AtomicBoolean mutex = new AtomicBoolean(false);

    @Bind(R.id.localization_text)
    TextView localizationText;

    @OnClick(R.id.request_signal_button)
    public void onRequest() {
        Log.d(TAG, "onRequest()");

        refreshLocation();

        SendDialog sendDialog = new SendDialog(getActivity(), "data sent");
        sendDialog.show();

    }

    void runOnUiThread(Runnable r) {
        getActivity().runOnUiThread(r);
    }

    TextView signalStrengthLevel;
    TextView signalStrengthText;
    ImageView signalImage;

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
        getActivity().registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signal, container, false);

        ButterKnife.bind(this, v);

        signalStrengthLevel = (TextView) v.findViewById(R.id.signal_desc_text);
        signalImage = (ImageView) v.findViewById(R.id.signal_image);
        signalStrengthText = (TextView) v.findViewById(R.id.signal_power_text);

        refreshLocation();
        return v;
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MainActivity.mBroadcastNoSignal)) {
                Log.d(TAG, "No signal!");
            }
            if (intent.getAction().equals(MainActivity.mBroadcastSignalBack)) {
                Log.d(TAG, "Signal back");
            }
            if (intent.getAction().equals(MainActivity.mBroadcastSignalLevel)) {
                signalLevel = intent.getIntExtra("Signal level", -1);
                signalStrength = intent.getIntExtra("Signal strength", -70);
                signalStrengthText.setText(signalStrength + " " + "dBm");
                Log.d(TAG, "Signal level " + signalLevel);
                Log.d(TAG, "Signal strength" + signalStrength);
                switch (signalLevel) {
                    case 0:
                        signalStrengthLevel.setText(NO_SIGNAL);
                        signalImage.setImageDrawable(getResources().getDrawable(R.drawable.signal_0));
                        break;
                    case 1:
                        signalStrengthLevel.setText(WEAK);
                        signalImage.setImageDrawable(getResources().getDrawable(R.drawable.signal_1));
                        break;
                    case 3:
                        signalStrengthLevel.setText(VERY_GOOD);
                        signalImage.setImageDrawable(getResources().getDrawable(R.drawable.signal_3));
                        break;
                    case 4:
                        signalStrengthLevel.setText(PERFECT);
                        signalImage.setImageDrawable(getResources().getDrawable(R.drawable.signal_4));
                        break;
                    default:
                        signalStrengthLevel.setText(GOOD);
                        signalImage.setImageDrawable(getResources().getDrawable(R.drawable.signal_2));

                }
            }
        }
    };

    private void refreshLocation() {
        Log.d(TAG, "refreshLocation()");

        if (mutex.get()) {
            Log.e(TAG, "mutexed");
            return;
        }
        mutex.set(true);

        Location lastLocation = App.INSTANCE.location;
        if (lastLocation == null) {
            Log.e(TAG, "last location is null");
            localizationText.setText("location not ready, enable GPS");
            AnimateUtils.compositeFade(handler, localizationText, 1, 0, 600);
            mutex.set(false);
        } else {
            Log.i(TAG, "fetch location: " + lastLocation.getLatitude() + "," + lastLocation.getLongitude());
            GeocodingUtils.getCurrentLocation(App.INSTANCE, lastLocation)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Address>() {
                        @Override
                        public void call(Address address) {
                            String message = address.getAddressLine(0);
                            AnimateUtils.compositeFade(handler, localizationText, 1, 0, 300);
                            localizationText.setText("location: " + message);
                            AnimateUtils.compositeFade(handler, localizationText, 1, 0, 300);
                            mutex.set(false);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    localizationText.setText("location not available now");
                                    AnimateUtils.compositeFade(handler, localizationText, 1, 0, 300);
                                    mutex.set(false);
                                }
                            });
                        }
                    });
        }
    }

}

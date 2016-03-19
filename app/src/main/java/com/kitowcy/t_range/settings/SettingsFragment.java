package com.kitowcy.t_range.settings;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.kitowcy.t_range.R;
import com.kitowcy.t_range.utils.NotificationBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Patryk Mieczkowski on 18.03.16.
 */
public class SettingsFragment extends Fragment {

    public static final String TAG = SettingsFragment.class.getSimpleName();
    public static final String NotificationPREFERENCES = "NotificationSharedPreferences";
    public static final String noSignalKey = "noSignal";
    public static final String signalBackKey = "signalBack";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Bind(R.id.switch_lost_signal)
    Switch lostSignalSwitch;
    @Bind(R.id.switch_signal_back)
    Switch signalBackSwitch;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(NotificationPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.switch_lost_signal)
    public void onLostSignalSwitchClick() {
        editor = sharedpreferences.edit();
        if (lostSignalSwitch.isChecked()) {
           // NotificationBuilder.createNotification(getActivity(), "Signal Lost", "You lost your signal :(");
            editor.putBoolean(noSignalKey, true);
        }
        else{
            editor.putBoolean(noSignalKey, false);
        }
        editor.commit();
    }

    @OnClick(R.id.switch_signal_back)
    public void onSignalBackSwitchClick() {
        editor = sharedpreferences.edit();
        if (signalBackSwitch.isChecked()) {
            // NotificationBuilder.createNotification(getActivity(), "Signal Lost", "You lost your signal :(");
            editor.putBoolean(signalBackKey, true);
        }
        else{
            editor.putBoolean(signalBackKey, false);
        }
        editor.commit();
    }
}

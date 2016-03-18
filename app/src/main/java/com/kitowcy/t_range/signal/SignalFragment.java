package com.kitowcy.t_range.signal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitowcy.t_range.R;

public class SignalFragment extends Fragment {

    public static SignalFragment newInstance() {
        SignalFragment fragment = new SignalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signal, container, false);
        return v;
    }

}

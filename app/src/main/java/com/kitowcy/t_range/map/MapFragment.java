package com.kitowcy.t_range.map;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kitowcy.t_range.R;
import com.kitowcy.t_range.RealmLocation;
import com.kitowcy.t_range.utils.AnimateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Patryk Mieczkowski on 18.03.16.
 */
public class MapFragment extends Fragment {

    public static final String TAG = MapFragment.class.getSimpleName();
    @Bind(R.id.fragment_map_parent)
    RelativeLayout parentLayout;
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    @Bind(R.id.progressMap)
    ProgressBar progressBar;
    public final Handler handler = new android.os.Handler();
    List<MarkerOptions> options;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);

        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
        }

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mapFragmentContainer,
                mapFragment).commitAllowingStateLoss();

        progressBar.setVisibility(View.VISIBLE);
        AnimateUtils.animateFade(progressBar, 0, 1, 300);

        MapObservable.init(mapFragment)
                .subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GoogleMap>() {
                    @Override
                    public void call(GoogleMap googleMap) {
                        AnimateUtils.animateFade(progressBar, 1, 0, 300, handler, new AnimateUtils.EndCallback() {
                            @Override
                            public void onEnd() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        MapFragment.this.googleMap = googleMap;
                        options = getMarkerOpts();
                        setupMap();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(parentLayout, "Error to load map", Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                });

        return v;
    }

    void runOnUiThread(Runnable runnable) {
        getActivity().runOnUiThread(runnable);
    }

    public List<MarkerOptions> getMarkerOpts() {
        List<MarkerOptions> opts = new ArrayList<>();

        Realm realm = Realm.getInstance(getActivity());
        RealmResults<RealmLocation> mockedLocations = realm.allObjects(RealmLocation.class);
        for (int j = 0; j < mockedLocations.size(); j++) {
            RealmLocation location = mockedLocations.get(j);
            float hue = matchHue(location.getStrength());
            MarkerOptions markerOpts = new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(location.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(hue));
            opts.add(markerOpts);
        }
        return opts;
    }

    private void setupMap() {
        Log.d(TAG, "setupMap: ");

        for (int j = 0; j < options.size(); j++) {
            MarkerOptions markerOpts = options.get(j);

            googleMap.addMarker(markerOpts);
        }
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition(new LatLng(50.06799, 19.9128143), 12, 60, 15)));
    }

    private float matchHue(int strength) {
        switch (strength) {
            case 0: {
                return BitmapDescriptorFactory.HUE_VIOLET;
            }
            case 1: {
                return BitmapDescriptorFactory.HUE_RED;
            }
            case 2: {
                return BitmapDescriptorFactory.HUE_ORANGE;
            }
            case 3: {
                return BitmapDescriptorFactory.HUE_YELLOW;
            }
            case 4: {
                return BitmapDescriptorFactory.HUE_GREEN;
            }
        }
        return BitmapDescriptorFactory.HUE_VIOLET;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}

package com.kitowcy.t_range.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kitowcy.t_range.R;
import com.kitowcy.t_range.RealmLocation;
import com.kitowcy.t_range.utils.AnimateUtils;

import java.util.ArrayList;

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
    @Bind(R.id.backgroundView)
    View pinkView;
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    ArrayList<MarkerOptions> options;
    boolean initialized;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        options = getMarkerOpts();
        mapFragment.setRetainInstance(true);
//        View view1  = new View(getActivity().getApplicationContext());
//        parentLayout.addView(view1);
        AnimateUtils.animateFade(pinkView, 0, 1, 400);
        MapObservable.init(mapFragment)
                .subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GoogleMap>() {
                    @Override
                    public void call(GoogleMap googleMap) {
                        MapFragment.this.googleMap = googleMap;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);
        Log.d(TAG, "onCreateView: ");

        mapFragment = new SupportMapFragment();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mapFragmentContainer,
                mapFragment).commitAllowingStateLoss();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    void runOnUiThread(Runnable runnable) {
        getActivity().runOnUiThread(runnable);
    }

    public ArrayList<MarkerOptions> getMarkerOpts() {
        ArrayList<MarkerOptions> opts = new ArrayList<>();

        Realm realm = Realm.getInstance(getActivity());
        RealmResults<RealmLocation> mockedLocations = realm.allObjects(RealmLocation.class);
        for (int j = 0; j < mockedLocations.size(); j++) {
            RealmLocation location = mockedLocations.get(j);
            int hue = location.getStrength();
            MarkerOptions markerOpts = new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(location.getName())
                    .icon(BitmapDescriptorFactory.fromBitmap(createScaledBitmap(hue)));
            opts.add(markerOpts);
        }
        return opts;
    }

    private Bitmap createScaledBitmap(int hue) {
        Log.d(TAG, "createScaledBitmap: " + hue);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                hue == 0 ? R.drawable.marker_1 :
                        hue == 1 ? R.drawable.marker_2 :
                                hue == 2 ? R.drawable.marker_3 :
                                        R.drawable.marker_4
        );
        return Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 3, bmp.getHeight() / 3, false);
    }

    private void setupMap() {
        Log.d(TAG, "setupMap: ");

        for (int j = 0; j < options.size(); j++) {
            MarkerOptions markerOpts = options.get(j);
            googleMap.addMarker(markerOpts);
        }
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition(new LatLng(50.06799, 19.9128143), 12, 60, 15)));

        initialized = true;
    }

    @DrawableRes
    private int matchHue(int strength) {
        Log.d(TAG, "matchHue: " + strength);
        switch (strength) {
            case 0:
            case 1:
                return R.drawable.marker_3;
            case 2:
                return R.drawable.marker_2;
            case 3:
                return R.drawable.marker_3;
            case 4:
                return R.drawable.marker_4;
            default:
                return R.drawable.marker_4;
        }
    }
}

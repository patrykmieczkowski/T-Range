package com.kitowcy.t_range.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Łukasz Marczak
 *
 * @since 19.03.16
 */
public class MapObservable {
    public static Observable<GoogleMap> init(final SupportMapFragment mapFragment) {
        return Observable.create(new Observable.OnSubscribe<GoogleMap>() {
            @Override
            public void call(final Subscriber<? super GoogleMap> subscriber) {
                if (mapFragment == null) {
                    subscriber.onError(new Throwable("Nullable map fragment!"));
                } else {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            subscriber.onNext(googleMap);
                        }
                    });
                }
            }
        });
    }
}

package com.kitowcy.t_range;

import android.app.Application;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Łukasz Marczak
 *
 * @since 18.03.16
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    public static App INSTANCE;
    public Location location;
    public Subscription subscription;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
             //   .setNumUpdates(5)
                .setInterval(1000);

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
          subscription = locationProvider.getUpdatedLocation(request)
                .subscribe(new Subscriber<Location>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Location location) {
                        App.this.location = location;
                        final String name = Thread.currentThread().getName();
                        Log.d(TAG, "name: " + name + ", location: " +
                                (location != null ? (location.getLatitude() + "," + location.getLongitude())
                                        : "Location is empty =("));
                    }
                });


        startLocationUpdates();
    }

    public Fragment currentFragment;


    public void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates: ");

    }
}

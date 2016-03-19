package com.kitowcy.t_range;

import android.app.Application;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import ru.solodovnikov.rxlocationmanager.LocationRequestBuilder;
import ru.solodovnikov.rxlocationmanager.LocationTime;
import rx.Subscriber;

/**
 * Created by Łukasz Marczak
 *
 * @since 18.03.16
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    public static App INSTANCE;
    public Location location;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        startLocationUpdates();
    }

    public Fragment currentFragment;


    public void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates: ");
        final LocationRequestBuilder locationRequestBuilder = new LocationRequestBuilder(this);

        locationRequestBuilder.addLastLocation(LocationManager.NETWORK_PROVIDER, new LocationTime(30, TimeUnit.SECONDS), false)
                .addRequestLocation(LocationManager.GPS_PROVIDER, new LocationTime(10, TimeUnit.SECONDS))
                .setDefaultLocation(new Location(LocationManager.PASSIVE_PROVIDER))
                .create().subscribe(new Subscriber<Location>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }

            @Override
            public void onNext(Location location) {
                Log.d(TAG, "onNext: ");
                App.this.location = location;
                final String name = Thread.currentThread().getName();
                Log.d(TAG, "name: "+name + ", location: " +
                        (location != null ? location.toString() : "Location is empty =("));
            }
        });
    }
}

package com.kitowcy.t_range.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Łukasz Marczak
 *
 * @since 19.03.16
 */
public class GeocodingUtils {
    public static final String TAG = GeocodingUtils.class.getSimpleName();
    public static final String errorMessage = "Cannot fetch address";

    public static rx.Observable<Address> getCurrentLocation(final Context ctx, final Location location) {
        return rx.Observable.create(new Observable.OnSubscribe<Address>() {
            @Override
            public void call(Subscriber<? super Address> subscriber) {
                Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
                List<Address> addresses;

                try {
                    addresses = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            // In this sample, get just a single address.
                            1);
                } catch (IOException ioException) {
                    // Catch network or other I/O problems.
                    Log.e(TAG, errorMessage, ioException);
                    subscriber.onError(new Throwable(ioException.getMessage()));
                    return;
                } catch (IllegalArgumentException illegalArgumentException) {
                    // Catch invalid latitude or longitude values.
                    subscriber.onError(new Throwable(illegalArgumentException.getMessage()));
                    Log.e(TAG, errorMessage + ". " +
                            "Latitude = " + location.getLatitude() +
                            ", Longitude = " +
                            location.getLongitude(), illegalArgumentException);
                    return;
                }
                // Handle case where no address was found.
                if (addresses == null || addresses.size() == 0) {
                    if (errorMessage.isEmpty()) {
                        Address address = new Address(Locale.getDefault());
                        address.setAddressLine(0, errorMessage);
                        subscriber.onNext(address);
                        Log.e(TAG, errorMessage);
                    }
                } else {
                    Address address = addresses.get(0);
                    subscriber.onNext(address);
                }
            }
        });
    }
}

package com.kitowcy.t_range.dial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kitowcy.t_range.internet.MessagesClient;
import com.kitowcy.t_range.search.AudioRecorderProxy;

import java.io.File;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Łukasz Marczak
 *
 * @since 19.03.16
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String TAG = NetworkChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG, "onReceive: ");
        if (internetAvailable(context)) {
            try {

                makePOST();
            } catch (Exception x) {
                x.printStackTrace();
            }

        } else {
            Log.e(TAG, "internet not available");
        }
    }

    private void makePOST() throws Exception {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("multipart", "form-data");
            }
        };
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(MessagesClient.endpoint)
                .setRequestInterceptor(requestInterceptor)
                .build();
        MessagesClient client = restAdapter.create(MessagesClient.class);
        List<MediaFile> files = AudioRecorderProxy.getAllRecordings();

        MediaFile mediaFile = files.get(0);
        client.postAudioMessage(new TypedFile("file:", new File(mediaFile.path)), mediaFile.title)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        Log.i(TAG, "received: " + response.getStatus());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "received: " + throwable.getMessage());
                        if (throwable instanceof RetrofitError) {
                            Log.e(TAG, "onError: " + ((RetrofitError) throwable).getKind().name());
                            Log.e(TAG, "onError: " + ((RetrofitError) throwable).getUrl());
                        }
                        throwable.printStackTrace();
                    }
                });
    }

    boolean internetAvailable(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        return serviceManager.isNetworkAvailable();
    }
}

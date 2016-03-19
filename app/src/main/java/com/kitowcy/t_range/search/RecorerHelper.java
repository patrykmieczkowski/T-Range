package com.kitowcy.t_range.search;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Łukasz Marczak
 *
 * @since 19.03.16
 */
public class RecorerHelper {
    public static final String TAG = RecorerHelper.class.getSimpleName();
    public MediaRecorder recorder;

    public RecorerHelper() {

    }

    public rx.Observable<MediaRecorder> prepareMediaRecorder() {
        return Observable.create(new Observable.OnSubscribe<MediaRecorder>() {
            @Override
            public void call(Subscriber<? super MediaRecorder> subscriber) {
                recorder = new MediaRecorder();

                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                String timeStamp = new SimpleDateFormat("dd.MM-HH:mm:ss.SSS", Locale.ENGLISH).format(new Date());
                String fileName = "/CALL_" + timeStamp + ".mp4";

                File storageDirAlpha = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                File storageDir = new File(storageDirAlpha, "/audalize");
                storageDir.mkdirs();

                Log.d(TAG, "prepareMediaRecorder: " + storageDir.getAbsolutePath());
                recorder.setOutputFile(storageDir.getAbsolutePath() + fileName);
                Log.d(TAG, "prepareMediaRecorder: " + storageDir.getAbsolutePath() + fileName);

                recorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                    @Override
                    public void onError(MediaRecorder mediaRecorder, int i, int i1) {
                        Log.e(TAG, "onError() called with: " + "mediaRecorder = [" + mediaRecorder + "], i = [" + i + "], i1 = [" + i1 + "]");
                    }
                });
                recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                    @Override
                    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
                        Log.d(TAG, "onInfo() called with: " + "mediaRecorder = [" + mediaRecorder + "], i = [" + i + "], i1 = [" + i1 + "]");
                    }
                });
                boolean wasError = false;
                try {
                    recorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable("Error " + e.getMessage()));
                    wasError = true;
                } finally {
                    if (!wasError) {
                        subscriber.onNext(recorder);
                    }
                }
            }
        });
    }

    public void prepareMediaRecorder2() {
        Log.d(TAG, "prepareMediaRecorder()");

        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        String timeStamp = new SimpleDateFormat("dd.MM-HH:mm:ss.SSS", Locale.ENGLISH).format(new Date());
        String fileName = "/CALL_" + timeStamp + ".mp4";

        File storageDirAlpha = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File storageDir = new File(storageDirAlpha, "/audalize");
        storageDir.mkdirs();

        Log.d(TAG, "prepareMediaRecorder: " + storageDir.getAbsolutePath());
        recorder.setOutputFile(storageDir.getAbsolutePath() + fileName);
        Log.d(TAG, "prepareMediaRecorder: " + storageDir.getAbsolutePath() + fileName);

        recorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mediaRecorder, int i, int i1) {
                Log.e(TAG, "onError() called with: " + "mediaRecorder = [" + mediaRecorder + "], i = [" + i + "], i1 = [" + i1 + "]");
            }
        });
        recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
                Log.d(TAG, "onInfo() called with: " + "mediaRecorder = [" + mediaRecorder + "], i = [" + i + "], i1 = [" + i1 + "]");
            }
        });

        try {
            recorder.prepare();
            recorder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

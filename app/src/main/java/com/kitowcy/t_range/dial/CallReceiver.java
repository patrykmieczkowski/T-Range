package com.kitowcy.t_range.dial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Łukasz Marczak
 *
 * @since 18.03.16
 */
public class CallReceiver extends BroadcastReceiver {

    public static final String TAG = CallReceiver.class.getSimpleName();
    static MyPhoneStateListener phoneListener;
    Context ctx;
    int x = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive() called with: " + "context = [" + context + "], intent = [" + intent + "]");
        ctx = context;

        try {

            TelephonyManager tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            if (phoneListener == null) {
                phoneListener = new MyPhoneStateListener();

                tmgr.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Phone Receive Error" + e);
        }

    }


//    public void onDestroy() {
//        telephony.listen(null, PhoneStateListener.LISTEN_NONE);
//    }


    private class MyPhoneStateListener extends PhoneStateListener {

        MediaRecorder recorder;
        boolean recordStatus = false;
        boolean isRecordingStarted = false;

        public void onCallStateChanged(int state, String incomingNumber) {

            Log.d("MyPhoneListener", state + "   incoming no:" + incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "onCallStateChanged: CALL_STATE_RINGING " + x++);
//                    String msg = "New Phone Call Event. Incoming Number : " + incomingNumber;
//                    int duration = Toast.LENGTH_LONG;
//                    Toast toast = Toast.makeText(ctx, msg, duration);
//                    toast.show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "onCallStateChanged: CALL_STATE_OFFHOOK " + x++);
                    Log.d(TAG, "starting recording");
                    recordStatus = true;
                    if (!isRecordingStarted) {
                        isRecordingStarted = true;
                        prepareMediaRecorder();
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "onCallStateChanged: CALL_STATE_IDLE " + x++);
                    if (recordStatus) {
                        recordStatus = false;
                        if (recorder != null) {
                            Log.d(TAG, "stoping recording");
                            try {
                                recorder.stop();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    break;
            }
        }

        private void prepareMediaRecorder() {
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
}

package com.kitowcy.t_range.utils;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Łukasz Marczak
 *
 * @since 18.03.16
 */
public class AnimateUtils {
    public interface EndCallback {
        void onEnd();
    }

    public static void animateFade(View v, float alphaStart, float alphaEnd, int duration) {
        v.setAlpha(alphaStart);
        v.animate().alpha(alphaEnd).setDuration(duration).start();
    }

    public static void animateFade(View v, float alphaStart, float alphaEnd, int duration, Handler h, @Nullable final EndCallback callback) {
        v.setAlpha(alphaStart);
        v.animate().alpha(alphaEnd).setDuration(duration).start();
        if (callback != null) {
            if (h != null) h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onEnd();
                }
            }, duration);
        }

    }

    public static void compositeFade(Handler handler, final View v, final float alphaStart, final float alphaEnd, final int duration) {
        compositeFade(handler, v, alphaStart, alphaEnd, duration, null);
    }

    public static void compositeFade(final Handler handler, final View v, final float alphaStart,
                                     final float alphaEnd, final int duration, @Nullable final EndCallback callback) {
        animateFade(v, alphaStart, alphaEnd, duration / 2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateFade(v, alphaEnd, alphaStart, duration / 2);
                if (callback != null)
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callback.onEnd();
                        }
                    }, duration / 2);
            }
        }, duration / 2);
    }
}

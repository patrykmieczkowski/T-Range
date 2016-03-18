package com.kitowcy.t_range.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by Patryk Mieczkowski on 18.03.16.
 */
public class NotificationBuilder {

    public static void createNotification(Context context, String title, String text) {
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}

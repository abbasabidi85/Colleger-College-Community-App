package com.abs.colleger.app.student.timetable;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.abs.colleger.app.R;
import com.abs.colleger.app.student.UserMainActivity;
import com.abs.colleger.app.student.home.HomeFragment;

import java.util.Calendar;

public class UserLectureNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context, "Upcoming Lecture", "You have a lecture in 30 minutes");
    }

    private void showNotification(Context context, String title, String content) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the device is running Android 8.0 (Oreo) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "Lectures",
                    "Lectures",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableVibration(true);

            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, "Lectures")
                    .setContentTitle(title)
                    .setContentText(content)
                    .setColor(context.getColor(R.color.cardview4ColorLight))
                    .setAutoCancel(true)
                    .setWhen(Calendar.getInstance().getTimeInMillis())
                    .setShowWhen(true)
                    .setSmallIcon(R.drawable.ic_stat_name);

            Intent intent = new Intent(context, UserMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("SELECTED_TAB", "1");

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);
            builder.setContentIntent(pendingIntent);
        }
        notificationManager.notify(1, builder.build());
    }
}

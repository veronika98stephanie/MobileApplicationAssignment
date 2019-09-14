package veroNstella.rmit.assignment.model;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.model.movie.Movie;
import veroNstella.rmit.assignment.service.NotificationReceiver;
import veroNstella.rmit.assignment.service.RemindAgainReceiver;

public class Utility {
    private Context context;

    public Utility(Context context) {
        this.context = context;
    }

    public static void cancelNotification(Context context, int notificationId) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancel(notificationId);
        }
    }

    public static void startRemindAgainAlarm(Context context, int minutes, int notificationId, String eventId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RemindAgainReceiver.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("notificationId", notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, notificationId, intent,
                PendingIntent.FLAG_ONE_SHOT);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(minutes),
                    pendingIntent);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setMovieDetail(Movie movie, TextView movieTitle, TextView year, ImageView movieImage) {
        if (movie != null) {
            movieTitle.setText(movie.getTitle());
            if (year != null) {
                year.setText(Integer.toString(movie.getYear()));
            }
            try {
                int id = context.getResources().getIdentifier(
                        movie.getPoster().toLowerCase(),
                        "drawable", context.getPackageName());
                if (id == 0) {
                    throw new Exception();
                } else {
                    movieImage.setImageResource(id);
                }
            } catch (Exception e) {
                movieImage.setImageResource(R.drawable.movie);
            }
        } else {
            movieTitle.setText(context.getString(R.string.n_a));
            if (year != null) {
                year.setText(context.getString(R.string.n_a));
            }
            movieImage.setImageResource(R.drawable.movie);
        }
    }

    public static void startAlarm(int minutes, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, -1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                    TimeUnit.MINUTES.toMillis(minutes), pendingIntent);
        }
    }

    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

}

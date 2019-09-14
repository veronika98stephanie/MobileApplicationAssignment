package veroNstella.rmit.assignment.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.service.CancelNotificationReceiver;
import veroNstella.rmit.assignment.service.DismissNotificationReceiver;
import veroNstella.rmit.assignment.service.RemindAgainNotificationReceiver;

public class CreateNotification {

    private Context context;
    private Model model;

    public CreateNotification(Context context) {
        this.context = context;
        model = ModelImpl.getSingletonInstance(context);
    }

    public void sendNotification(String eventId, String title, String content, int id) {

        PendingIntent dismissPendingIntent = this.getPendingIntent(context, DismissNotificationReceiver.class, eventId, id);
        PendingIntent cancelPendingIntent = this.getPendingIntent(context, CancelNotificationReceiver.class, eventId, id);
        PendingIntent remindAgainPendingIntent = this.getPendingIntent(context, RemindAgainNotificationReceiver.class, eventId, id);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID);
        notification.setSmallIcon(R.drawable.event).setContentTitle(title);
        notification.setContentText(content).setPriority(NotificationCompat.PRIORITY_HIGH);
        notification.setCategory(NotificationCompat.CATEGORY_EVENT);
        notification.addAction(R.drawable.event, "DISMISS", dismissPendingIntent);
        notification.addAction(R.drawable.event, "CANCEL", cancelPendingIntent);
        notification.addAction(R.drawable.event, "REMIND AGAIN", remindAgainPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        model.addEventNotificationPair(eventId, id);
        notificationManagerCompat.notify(id, notification.build());
    }

    private PendingIntent getPendingIntent(Context context, Class<?> cls, String eventId, int id) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("eventId", eventId);
        intent.putExtra("notificationId", id);
        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

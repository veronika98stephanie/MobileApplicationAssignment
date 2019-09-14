package veroNstella.rmit.assignment.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import veroNstella.rmit.assignment.controller.CancelCancelNotificationListener;
import veroNstella.rmit.assignment.controller.ConfirmCancelNotificationListener;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.event.Event;

public class DialogActivity extends AppCompatActivity implements
        DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Intent intent = getIntent();
            Model model = ModelImpl.getSingletonInstance(this);
            Log.i(TAG, "onCreate: " + intent.getStringExtra("eventId"));
            Event event = model.getEventByEventId(intent.getStringExtra("eventId"));

            super.onCreate(savedInstanceState);
            AlertDialog LDialog = new AlertDialog.Builder(this)
                    .setTitle(event.getTitle())
                    .setMessage(event.getVenue() + "\n" +
                            event.getStartDate() + "\n" +
                            event.getEndDate() + "\n" +
                            event.getAttendees().size() + " attendees")
                    .setOnCancelListener(this)
                    .setOnDismissListener(this)
                    .setPositiveButton("Confirm", new ConfirmCancelNotificationListener(
                            this, intent.getStringExtra("eventId"),
                            intent.getIntExtra("notificationId", -1)))
                    .setNegativeButton("Cancel", new CancelCancelNotificationListener(
                            this, intent.getStringExtra("eventId"),
                            intent.getIntExtra("notificationId", -1)))
                    .create();
            LDialog.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if (!DialogActivity.this.isFinishing()) {
            finish();
            // kalo user langsung back
            Log.i(TAG, "onCancel: ");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (!DialogActivity.this.isFinishing()) {
            finish();
            // kalo button di pencet
            Log.i(TAG, "onDismiss: ");
        }
    }
}

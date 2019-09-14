package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

public class AttendeeAddListener implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private int PICK_CONTACT;

    public AttendeeAddListener(Activity activity, int requestCode) {
        this.activity = activity;
        this.PICK_CONTACT = requestCode;
    }

    // start contact activity from android to get result
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, PICK_CONTACT);
        } else {
            Log.i(TAG, "Cannot open activity for this intent");
        }
    }
}

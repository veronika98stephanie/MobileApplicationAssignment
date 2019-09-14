package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.util.Log;

import veroNstella.rmit.assignment.view.CalendarActivity;
import veroNstella.rmit.assignment.view.EventActivity;

public class EventTabLayoutListener implements TabLayout.OnTabSelectedListener {
    private String TAG = getClass().getName();
    private Context context;
    private Intent intent;

    public EventTabLayoutListener(Context context) {
        this.context = context;
    }

    // interchange between EventActivity and CalendarActivity when tab is clicked
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String selectedTabName = null;
        if (tab.getText() != null) {
            selectedTabName = tab.getText().toString();
        }
        if (selectedTabName != null) {
            if (selectedTabName.equals("Calendar")) {
                intent = new Intent(context, CalendarActivity.class);
            } else {
                intent = new Intent(context, EventActivity.class);
            }
        }
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            ((Activity) context).finish();
        } else {
            Log.i(TAG, "Cannot open activity for this intent");
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

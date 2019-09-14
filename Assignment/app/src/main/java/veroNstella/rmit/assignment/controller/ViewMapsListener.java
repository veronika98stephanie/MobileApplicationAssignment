package veroNstella.rmit.assignment.controller;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import veroNstella.rmit.assignment.view.MapsActivity;

public class ViewMapsListener implements MenuItem.OnMenuItemClickListener {
    private Context context;

    public ViewMapsListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onMenuItemClick(MenuItem v) {
        Intent intent = new Intent(context, MapsActivity.class);
        context.startActivity(intent);
        return true;
    }
}

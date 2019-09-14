package veroNstella.rmit.assignment.service;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;

public class LocationServiceListener implements LocationListener {
    private Context context;
    private Model model;

    public LocationServiceListener(Context context) {
        this.context = context;
        model = ModelImpl.getSingletonInstance(context);
    }

    @Override
    public void onLocationChanged(Location location) {
        model.setLatitude(location.getLatitude());
        model.setLongitude(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }
}

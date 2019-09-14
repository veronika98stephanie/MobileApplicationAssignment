package veroNstella.rmit.assignment.controller;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import veroNstella.rmit.assignment.view.TimePickerFragment;

public class SelectTimeListener implements View.OnClickListener {
    private AppCompatActivity activity;
    private String requestCode;

    public SelectTimeListener(AppCompatActivity activity, String requestCode) {
        this.activity = activity;
        this.requestCode = requestCode;
    }

    @Override
    public void onClick(View v) {
        showTimePickerDialog();
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(activity.getSupportFragmentManager(), requestCode);
    }
}

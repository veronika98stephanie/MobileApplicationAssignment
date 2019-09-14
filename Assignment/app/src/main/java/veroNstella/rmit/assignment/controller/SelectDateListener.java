package veroNstella.rmit.assignment.controller;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import veroNstella.rmit.assignment.view.DatePickerFragment;

public class SelectDateListener implements View.OnClickListener {
    private AppCompatActivity appCompatActivity;
    private String requestCode;

    public SelectDateListener(AppCompatActivity appCompatActivity, String requestCode) {
        this.appCompatActivity = appCompatActivity;
        this.requestCode = requestCode;
    }

    @Override
    public void onClick(View v) {
        showDatePickerDialog();
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(appCompatActivity.getSupportFragmentManager(), requestCode);
    }

}


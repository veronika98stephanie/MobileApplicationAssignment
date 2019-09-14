package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import veroNstella.rmit.assignment.view.MovieActivity;

public class EventEditMovieListener implements View.OnClickListener {
    private String TAG = getClass().getName();
    private Activity context;
    private int EDIT_MOVIE;

    public EventEditMovieListener(Activity activity, int requestCode) {
        this.context = activity;
        this.EDIT_MOVIE = requestCode;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, MovieActivity.class);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivityForResult(intent, EDIT_MOVIE);
        } else {
            Log.i(TAG, "Cannot open activity for this intent");
        }
    }
}

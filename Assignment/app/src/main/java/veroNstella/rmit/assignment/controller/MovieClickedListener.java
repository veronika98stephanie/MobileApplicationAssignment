package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import static android.app.Activity.RESULT_OK;

public class MovieClickedListener implements View.OnClickListener {
    private Activity context;
    private String index;

    public MovieClickedListener(Activity activity, int index) {
        this.context = activity;
        this.index = Integer.toString(index);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("movie", index);
        context.setResult(RESULT_OK, intent);
        context.finish();
    }
}

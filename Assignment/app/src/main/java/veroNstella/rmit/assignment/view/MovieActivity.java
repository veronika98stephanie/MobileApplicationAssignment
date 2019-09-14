package veroNstella.rmit.assignment.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.database.MovieEventDB;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.movie.Movie;
import veroNstella.rmit.assignment.thread.PersistDatabase;
import veroNstella.rmit.assignment.view.adapter.MovieAdapter;

public class MovieActivity extends AppCompatActivity {
    private MovieEventDB movieEventDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieEventDB = new MovieEventDB(getApplicationContext());
        Model model = ModelImpl.getSingletonInstance(MovieActivity.this);
        List<Movie> movies = model.getMovieList();

        RecyclerView recyclerView = findViewById(R.id.movieRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(
                MovieActivity.this, 2,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter myAdapter = new MovieAdapter(MovieActivity.this, movies);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        new Thread(new PersistDatabase(movieEventDB)).start();
    }
}

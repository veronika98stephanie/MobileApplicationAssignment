package veroNstella.rmit.assignment.view.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.controller.MovieClickedListener;
import veroNstella.rmit.assignment.model.Utility;
import veroNstella.rmit.assignment.model.movie.Movie;
import veroNstella.rmit.assignment.model.movie.MovieImpl;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private Activity context;
    private Utility utility;

    public MovieAdapter(Activity context, List<Movie> movieList) {
        movies = movieList;
        this.context = context;
        utility = new Utility(context);
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_movie_list, viewGroup, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        utility.setMovieDetail(movies.get(i),
                movieViewHolder.txtMovieTitle,
                movieViewHolder.txtMovieYear,
                movieViewHolder.imgMoviePoster);
        movieViewHolder.itemView.setTag(movies.get(i));
        movieViewHolder.itemView.setOnClickListener(
                new MovieClickedListener(context,
                        movies.indexOf((MovieImpl) movieViewHolder.itemView.getTag())
                )
        );
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMovieTitle, txtMovieYear;
        private ImageView imgMoviePoster;
        private View itemView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMovieTitle = itemView.findViewById(R.id.txtMovieTitle);
            txtMovieYear = itemView.findViewById(R.id.txtMovieYear);
            imgMoviePoster = itemView.findViewById(R.id.imgMoviePoster);
            this.itemView = itemView;
        }
    }
}

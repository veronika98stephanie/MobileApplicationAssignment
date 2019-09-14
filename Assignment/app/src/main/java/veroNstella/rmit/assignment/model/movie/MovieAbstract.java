package veroNstella.rmit.assignment.model.movie;

public abstract class MovieAbstract implements Movie {
    private String movieId;
    private String title;
    private int year;
    private String poster;

    MovieAbstract(String movieId, String title, int year, String poster) {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
        this.poster = poster;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }
}

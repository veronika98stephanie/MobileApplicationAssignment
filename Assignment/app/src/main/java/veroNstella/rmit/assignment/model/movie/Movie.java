package veroNstella.rmit.assignment.model.movie;

public interface Movie {
    String getMovieId();

    String getTitle();

    void setTitle(String title);

    int getYear();

    void setYear(int year);

    String getPoster();
}

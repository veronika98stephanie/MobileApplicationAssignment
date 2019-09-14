package veroNstella.rmit.assignment.thread;

import android.database.SQLException;

import veroNstella.rmit.assignment.database.MovieEventDB;

public class PersistDatabase implements Runnable {
    private MovieEventDB movieEventDB;

    public PersistDatabase(MovieEventDB movieEventDB) {
        this.movieEventDB = movieEventDB;
    }

    @Override
    public void run() {
        try {
            movieEventDB.open();
            movieEventDB.resetTable();
            movieEventDB.storeAllData();
            movieEventDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

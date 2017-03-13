/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import entities.Movie;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Melody
 */
public class MovieTableModel extends AbstractTableModel {

    private static final int TITLE_COL = 0;
    private static final int GENRE_COL = 1;
    private static final int TAGS_COL = 2;
    private static final int YEAR_COL = 3;
    private static final int COUNTRY_COL = 4;
    private static final int RATINGS_COL = 5;
    private static final int NUM_OF_REVIEWS_COL = 6;

    private static final String[] colNames = {"Title", "Genre", "Tags", "Year", "Country", "Ratings", "# of reviews"};
    private final List<Movie> movies;

    public MovieTableModel(List<Movie> movies) {
        this.movies = movies;
    }
    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Movie movie = movies.get(row);
        switch (col) {
            case TITLE_COL:
                return movie.getTitle();
            case GENRE_COL:
                return movie.getGenre();
            case TAGS_COL:
                return movie.getTags();
            case YEAR_COL:
                return movie.getYear().toString();
            case COUNTRY_COL:
                return movie.getCountry();
            case RATINGS_COL:
                return movie.getRating().toString();
            case NUM_OF_REVIEWS_COL:
                return movie.getNumOfReviews().toString();
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return String.class;
    }

    @Override
    public String getColumnName(int col) {
        return colNames[col];
    }
}

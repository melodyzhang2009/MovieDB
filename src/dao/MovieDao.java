/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.Movie;
import entities.QueryParams;
import gui.Operators;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Melody
 */
public class MovieDao {

    private final Connection myConn;

    private static final String AVERAGE_RATING = "TRUNC((rtAllCriticsRating + rtTopCriticsRating + rtAudienceRating)/3, 1)";
    private static final String NUM_OF_REVIEWS = "TRUNC((rtAllCriticsNumReviews + rtTopCriticsNumReviews + rtAudienceNumRatings) /3,1)";

    public MovieDao() {
        myConn = MyConnection.getConnection();
    }

    public Set<String> getAllGenres() {
        Set<String> genres = new TreeSet<>();
        try (PreparedStatement myStmt = myConn.prepareStatement("select distinct genre from movie_genres")) {
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                genres.add(myRs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return genres;
    }

    public void closeDbConnection() {
        try {
            myConn.close();
            Logger.getLogger(MovieDao.class.getName()).log(Level.INFO, "DB closed successfully");
        } catch (SQLException ex) {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Set<String> getCountriesForGenres(Set<String> genres) {
        Set<String> countries = new TreeSet<>();
        for (String genre : genres) {
            try (PreparedStatement myStmt = myConn.prepareStatement(
                    "select distinct m.country from movies m, movie_genres mg"
                    + " where m.mid = mg.mid and mg.genre = ? and m.country IS NOT NULL")) {
                myStmt.setString(1, genre);
                ResultSet results = myStmt.executeQuery();
                while (results.next()) {
                    countries.add(results.getString(1));
                }
                results.close();
            } catch (SQLException ex) {
                Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
        return countries;
    }

    public Set<String> getAllCountries() {
        Set<String> countries = new TreeSet<>();
        try (PreparedStatement myStmt = myConn.prepareStatement(
                "select distinct country from movies where country IS NOT NULL")) {
            ResultSet results = myStmt.executeQuery();
            while (results.next()) {
                countries.add(results.getString(1));
            }
            results.close();
        } catch (SQLException ex) {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countries;
    }

    public String generateAndQuery(QueryParams queryParams) {
        String selectClause
                = "SELECT DISTINCT m.title, g.genres as genre, m.year, m.country, tags, "
                + AVERAGE_RATING + " as ratings, " + NUM_OF_REVIEWS + " as numberofreviews \n";

        // construct FROM clause
        StringBuilder sb = new StringBuilder("FROM  MOVIES m \n")
                .append("LEFT JOIN (SELECT mt.mid as mid, LISTAGG(t.value, ', ') WITHIN GROUP(ORDER BY t.value) as tags \n")
                .append("FROM Movie_Tags mt, Tags t \n")
                .append("WHERE mt.tid = t.tid \n")
                .append("GROUP BY mt.mid) t1 \n")
                .append("ON m.mid = t1.mid, \n")
                .append("(SELECT mg.mid as mid, LISTAGG(mg.genre, ', ') WITHIN GROUP(ORDER BY mg.genre) as genres \n")
                .append("FROM movie_genres mg \n")
                .append("GROUP BY mg.mid) g \n");

        if (!(queryParams.getActor1Name().isEmpty()
                && queryParams.getActor2Name().isEmpty()
                && queryParams.getActor3Name().isEmpty()
                && queryParams.getActor4Name().isEmpty())) {
            sb.append(", (SELECT m1.mid as mid, LISTAGG(ma.aname, ', ') WITHIN GROUP(ORDER BY ma.aname) as actors \n")
                    .append("FROM Movie_Actors ma, Movies m1 \n")
                    .append("WHERE ma.mid = m1.mid \n")
                    .append("GROUP BY m1.mid) a ");
        }

        if (!queryParams.getDirectorName().isEmpty()) {
            sb.append(", movie_directors md \n");
        }

        // user id
        if (!queryParams.getUserId().isEmpty()
                && (queryParams.getRateDateFrom() != null || queryParams.getRateDateTo() != null
                || queryParams.getUserRatingOperator() != Operators.ALL && !queryParams.getUserRating().isEmpty())) {
            sb.append(", User_RatedMovie_Timedate urt \n");
        }
        String fromClause = sb.toString();

        // construct WHERE clause
        // genres
        sb = new StringBuilder("WHERE g.mid = m.mid AND g.genres like ")
                .append("'");
        for (String genre : queryParams.getGenresSelected()) {
            sb.append("%");
            sb.append(genre);
        }
        sb.append("%").append("'").append(" ");

        // country
        if (!queryParams.getCountriesSelected().isEmpty()) {
            sb.append("AND m.country = ")
                    .append("'")
                    .append(queryParams.getCountriesSelected().iterator().next())
                    .append("' ");
        }

        // actors
        if (!(queryParams.getActor1Name().isEmpty()
                && queryParams.getActor2Name().isEmpty()
                && queryParams.getActor3Name().isEmpty()
                && queryParams.getActor4Name().isEmpty())) {
            sb.append("AND m.mid = a.mid AND a.actors like ")
                    .append("'");
            if (!queryParams.getActor1Name().isEmpty()) {
                sb.append("%")
                        .append(handleEscape(queryParams.getActor1Name()));
            }
            if (!queryParams.getActor2Name().isEmpty()) {
                sb.append("%")
                        .append(handleEscape(queryParams.getActor2Name()));
            }
            if (!queryParams.getActor3Name().isEmpty()) {
                sb.append("%")
                        .append(handleEscape(queryParams.getActor3Name()));
            }
            if (!queryParams.getActor4Name().isEmpty()) {
                sb.append("%")
                        .append(handleEscape(queryParams.getActor4Name()));
            }
            sb.append("%' ");
        }

        // director
        if (!queryParams.getDirectorName().isEmpty()) {
            sb.append("AND m.mid = md.mid  AND md.dname like ")
                    .append("'%")
                    .append(handleEscape(queryParams.getDirectorName()))
                    .append("%' ");
        }

        // rating
        if (queryParams.getRatingOperator() != Operators.ALL && !queryParams.getRating().isEmpty()) {
            sb.append("AND ")
                    .append(AVERAGE_RATING)
                    .append(queryParams.getRatingOperator())
                    .append(" ")
                    .append(queryParams.getRating())
                    .append(" ");
        }

        // num of ratings
        if (queryParams.getNumOfRatingsOperator() != Operators.ALL && !queryParams.getNumOfRatings().isEmpty()) {
            sb.append("AND ")
                    .append(NUM_OF_REVIEWS)
                    .append(queryParams.getNumOfRatingsOperator())
                    .append(" ")
                    .append(queryParams.getNumOfRatings())
                    .append(" ");
        }

        // movie year from
        if (queryParams.getMovieYearFrom() != null) {
            sb.append("AND m.year >= ")
                    .append(queryParams.getMovieYearFrom())
                    .append(" ");
        }

        // movie year to
        if (queryParams.getMovieYearTo() != null) {
            sb.append("AND m.year <= ")
                    .append(queryParams.getMovieYearTo())
                    .append(" ");
        }

        // user id
        if (!queryParams.getUserId().isEmpty()
                && (queryParams.getRateDateFrom() != null || queryParams.getRateDateTo() != null
                || queryParams.getUserRatingOperator() != Operators.ALL && !queryParams.getUserRating().isEmpty())) {
            sb.append("AND m.mid = urt.mid AND urt.userid = ")
                    .append(queryParams.getUserId())
                    .append(" ");

            if (queryParams.getRateDateFrom() != null) {
                sb.append("AND ")
                        .append("urt.reviewday >= TO_DATE('")
                        .append(queryParams.getRateDateFrom())
                        .append("', 'YYYY-MM-DD') ");
            }
            if (queryParams.getRateDateTo() != null) {
                sb.append("AND ")
                        .append("urt.reviewday <= TO_DATE('")
                        .append(queryParams.getRateDateTo())
                        .append("', 'YYYY-MM-DD') ");
            }

            if (queryParams.getUserRatingOperator() != Operators.ALL && !queryParams.getUserRating().isEmpty()) {
                sb.append("AND ")
                        .append("urt.rating ")
                        .append(queryParams.getUserRatingOperator())
                        .append(" ")
                        .append(queryParams.getUserRating())
                        .append(" ");
            }
        }

        String whereClause = sb.toString();

        return selectClause + fromClause + whereClause;
    }

    public List<Movie> getMoviesForAndQuery(QueryParams queryParams) {
        String queryString = generateAndQuery(queryParams);
        return executeQuery(queryString);
    }

    public String generateOrQuery(QueryParams queryParams) {
        StringBuilder queryBuilder = new StringBuilder();
        String selectClause
                = "SELECT DISTINCT m.title, g.genres as genre, m.year, m.country, tags, "
                + AVERAGE_RATING + " as ratings, " + NUM_OF_REVIEWS + " as numberofreviews \n";
        queryBuilder.append(selectClause);

        // base FROM clause
        StringBuilder sb = new StringBuilder("FROM  MOVIES m \n")
                .append("LEFT JOIN (SELECT mt.mid as mid, LISTAGG(t.value, ', ') WITHIN GROUP(ORDER BY t.value) as tags \n")
                .append("FROM Movie_Tags mt, Tags t \n")
                .append("WHERE mt.tid = t.tid \n")
                .append("GROUP BY mt.mid) t1 \n")
                .append("ON m.mid = t1.mid, \n")
                .append("(SELECT mg.mid as mid, LISTAGG(mg.genre, ', ') WITHIN GROUP(ORDER BY mg.genre) as genres \n")
                .append("FROM movie_genres mg \n")
                .append("GROUP BY mg.mid) g \n");
        String baseFromClause = sb.toString();

        queryBuilder.append(baseFromClause);

        //// genre
        sb = new StringBuilder("WHERE g.mid = m.mid AND (");
        for (String genre : queryParams.getGenresSelected()) {
            sb.append("g.genres like '%").append(genre).append("%' OR ");
        }

        //// country
        if (!queryParams.getCountriesSelected().isEmpty()) {
            for (String country : queryParams.getCountriesSelected()) {
                sb.append("m.country = '")
                        .append(country).append("' OR ");
            }
        }

        //// rating
        if (queryParams.getRatingOperator() != Operators.ALL && !queryParams.getRating().isEmpty()) {
            sb.append(AVERAGE_RATING)
                    .append(queryParams.getRatingOperator())
                    .append(" ")
                    .append(queryParams.getRating())
                    .append(" OR ");
        }

        // num of ratings
        if (queryParams.getNumOfRatingsOperator() != Operators.ALL && !queryParams.getNumOfRatings().isEmpty()) {
            sb.append(NUM_OF_REVIEWS)
                    .append(queryParams.getNumOfRatingsOperator())
                    .append(" ")
                    .append(queryParams.getNumOfRatings())
                    .append(" OR ");
        }

        // movie year from to
        if (queryParams.getMovieYearFrom() != null && queryParams.getMovieYearTo() != null) {
            sb.append("(m.year >= ")
                    .append(queryParams.getMovieYearFrom())
                    .append(" AND ");
            sb.append("m.year <= ")
                    .append(queryParams.getMovieYearTo())
                    .append(") OR ");
        } else if (queryParams.getMovieYearFrom() != null) {
            sb.append("m.year >= ")
                    .append(queryParams.getMovieYearFrom())
                    .append(" OR ");
        } else if (queryParams.getMovieYearTo() != null) {
            sb.append("m.year <= ")
                    .append(queryParams.getMovieYearTo())
                    .append(" OR ");
        }

        sb.delete(sb.lastIndexOf(" OR "), sb.length())
                .append(") \n ");

        //// genre, country, rating, numOfRatings, movieYearFrom, movieYearTo where clause
        queryBuilder.append(sb.toString());

        //// actor
        if (!(queryParams.getActor1Name().isEmpty()
                && queryParams.getActor2Name().isEmpty()
                && queryParams.getActor3Name().isEmpty()
                && queryParams.getActor4Name().isEmpty())) {
            String fromClause = baseFromClause + ", Movie_Actors ma \n";
            sb = new StringBuilder("WHERE g.mid = m.mid AND m.mid = ma.mid AND (");

            if (!queryParams.getActor1Name().isEmpty()) {
                sb.append("ma.aname like '%")
                        .append(handleEscape(queryParams.getActor1Name()))
                        .append("%' OR ");
            }
            if (!queryParams.getActor2Name().isEmpty()) {
                sb.append("ma.aname like '%")
                        .append(handleEscape(queryParams.getActor2Name()))
                        .append("%' OR ");
            }
            if (!queryParams.getActor3Name().isEmpty()) {
                sb.append("ma.aname like '%")
                        .append(handleEscape(queryParams.getActor3Name()))
                        .append("%' OR ");
            }
            if (!queryParams.getActor4Name().isEmpty()) {
                sb.append("ma.aname like '%")
                        .append(handleEscape(queryParams.getActor4Name()))
                        .append("%' OR ");
            }
            sb.delete(sb.lastIndexOf(" OR "), sb.length())
                    .append(") \n");
            queryBuilder.append("union \n").append(selectClause).append(fromClause).append(sb.toString());
        }

        //// director
        if (!queryParams.getDirectorName().isEmpty()) {
            String fromClause = baseFromClause + ", movie_directors md \n";
            sb = new StringBuilder("WHERE g.mid = m.mid AND m.mid = md.mid AND (");

            sb.append("md.dname like '%")
                    .append(handleEscape(queryParams.getDirectorName()))
                    .append("%' OR ");

            sb.delete(sb.lastIndexOf(" OR "), sb.length())
                    .append(") \n");
            queryBuilder.append("union \n").append(selectClause).append(fromClause).append(sb.toString());
        }

        //// user id
        if (!queryParams.getUserId().isEmpty()
                && (queryParams.getRateDateFrom() != null || queryParams.getRateDateTo() != null
                || queryParams.getUserRatingOperator() != Operators.ALL && !queryParams.getUserRating().isEmpty())) {
            String fromClause = baseFromClause + ", User_RatedMovie_Timedate urt \n";
            
            sb = new StringBuilder("WHERE g.mid = m.mid AND urt.mid = m.mid AND urt.userid = ")
                    .append(queryParams.getUserId()).append(" AND (");
            if (queryParams.getRateDateFrom() != null && queryParams.getRateDateTo() != null) {
                sb.append("(urt.reviewday >= TO_DATE('")
                        .append(queryParams.getRateDateFrom())
                        .append("', 'YYYY-MM-DD') ");
                sb.append("AND ")
                        .append("urt.reviewday <= TO_DATE('")
                        .append(queryParams.getRateDateTo())
                        .append("', 'YYYY-MM-DD')) OR ");
                
            } else if (queryParams.getRateDateFrom() != null) {
                sb.append("urt.reviewday >= TO_DATE('")
                        .append(queryParams.getRateDateFrom())
                        .append("', 'YYYY-MM-DD') OR ");
            } else if (queryParams.getRateDateTo() != null) {
                sb.append("urt.reviewday <= TO_DATE('")
                        .append(queryParams.getRateDateTo())
                        .append("', 'YYYY-MM-DD') OR ");
            }
            
            if (queryParams.getUserRatingOperator() != Operators.ALL && !queryParams.getUserRating().isEmpty()) {
                sb.append("urt.rating ")
                        .append(queryParams.getUserRatingOperator())
                        .append(" ")
                        .append(queryParams.getUserRating())
                        .append(" OR ");
            }

            sb.delete(sb.lastIndexOf(" OR "), sb.length())
                    .append(") \n");
            queryBuilder.append("union \n").append(selectClause).append(fromClause).append(sb.toString());
        }
        
        return queryBuilder.toString();
    }

    public List<Movie> getMoviesForOrQuery(QueryParams queryParams) {
        String queryString = generateOrQuery(queryParams);
        return executeQuery(queryString);
    }

    private String handleEscape(String string) {
        return string.replaceAll("'", "''");
    }

    private List<Movie> executeQuery(String queryString) {
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement myStmt = myConn.prepareStatement(queryString)) {
            ResultSet results = myStmt.executeQuery();
            while (results.next()) {
                movies.add(new Movie(
                        results.getString(1), // title
                        results.getString(2), // tiltl
                        results.getInt(3), // year
                        results.getString(4), // county
                        results.getString(5), // tags
                        results.getFloat(6), // rating
                        results.getInt(7) // numOfReviews
                ));
            }
            results.close();
        } catch (SQLException ex) {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movies;
    }
}

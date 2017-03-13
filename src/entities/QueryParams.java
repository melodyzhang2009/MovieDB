/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import gui.Operators;
import java.sql.Date;
import java.util.Set;

/**
 *
 * @author Melody
 */
public class QueryParams {
    private Set<String> genresSelected;
    private Set<String> countriesSelected;

    private Integer movieYearFrom;
    private Integer movieYearTo;

    private Operators ratingOperator = Operators.ALL;
    private Operators numOfRatingOperator = Operators.ALL;
    private Operators userRatingOperator = Operators.ALL;
    
    private Date rateDateFrom;
    private Date rateDateTo;
    
    private String actor1Name = "";
    private String actor2Name = "";
    private String actor3Name = "";
    private String actor4Name = "";
    
    private String directorName = "";
    
    private String rating = "";
    private String numOfRatings = "";
    private String userId = "";
    private String userRating = "";
    
    public QueryParams() {
    }

    public Set<String> getGenresSelected() {
        return genresSelected;
    }

    public void setGenresSelected(Set<String> genresSelected) {
        this.genresSelected = genresSelected;
    }

    public Set<String> getCountriesSelected() {
        return countriesSelected;
    }

    public void setCountriesSelected(Set<String> countriesSelected) {
        this.countriesSelected = countriesSelected;
    }

    public Integer getMovieYearFrom() {
        return movieYearFrom;
    }

    public void setMovieYearFrom(Integer movieYearFrom) {
        this.movieYearFrom = movieYearFrom;
    }

    public Integer getMovieYearTo() {
        return movieYearTo;
    }

    public void setMovieYearTo(Integer movieYearTo) {
        this.movieYearTo = movieYearTo;
    }

    public Operators getRatingOperator() {
        return ratingOperator;
    }

    public void setRatingOperator(Operators ratingOperator) {
        this.ratingOperator = ratingOperator;
    }

    public Operators getNumOfRatingsOperator() {
        return numOfRatingOperator;
    }

    public void setNumOfRatingOperator(Operators numOfRatingOperator) {
        this.numOfRatingOperator = numOfRatingOperator;
    }

    public Operators getUserRatingOperator() {
        return userRatingOperator;
    }

    public void setUserRatingOperator(Operators userRatingOperator) {
        this.userRatingOperator = userRatingOperator;
    }

    public Date getRateDateFrom() {
        return rateDateFrom;
    }

    public void setRateDateFrom(Date rateDateFrom) {
        this.rateDateFrom = rateDateFrom;
    }

    public Date getRateDateTo() {
        return rateDateTo;
    }

    public void setRateDateTo(Date rateDateTo) {
        this.rateDateTo = rateDateTo;
    }

    public String getActor1Name() {
        return actor1Name;
    }

    public void setActor1Name(String actor1Name) {
        this.actor1Name = actor1Name;
    }

    public String getActor2Name() {
        return actor2Name;
    }

    public void setActor2Name(String actor2Name) {
        this.actor2Name = actor2Name;
    }

    public String getActor3Name() {
        return actor3Name;
    }

    public void setActor3Name(String actor3Name) {
        this.actor3Name = actor3Name;
    }

    public String getActor4Name() {
        return actor4Name;
    }

    public void setActor4Name(String actor4Name) {
        this.actor4Name = actor4Name;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getNumOfRatings() {
        return numOfRatings;
    }

    public void setNumOfRatings(String numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }
}

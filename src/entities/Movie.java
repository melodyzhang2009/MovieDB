/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author Melody
 */
public class Movie {
    private final String title;
    private final String genre;
    private final Integer year;
    private final String country;
    private final String tags;
    private final Float rating;
    private final Integer numOfReviews;

    public Movie(String title, String genre, Integer year, String country, String tags, Float rating, Integer numOfReviews) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.country = country;
        this.tags = tags;
        this.rating = rating;
        this.numOfReviews = numOfReviews;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getYear() {
        return year;
    }

    public String getCountry() {
        return country;
    }

    public String getTags() {
        return tags;
    }

    public Float getRating() {
        return rating;
    }

    public Integer getNumOfReviews() {
        return numOfReviews;
    }
}

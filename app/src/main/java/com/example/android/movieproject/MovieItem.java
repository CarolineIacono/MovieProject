package com.example.android.movieproject;

import java.io.Serializable;

/**
 * Created by carolinestewart on 5/2/16.
 */
public class MovieItem implements Serializable {
    String image;
    String title;
    String overview;
    String vote_average;
    String release_date;
    String id;
    String key;
    String name;






    public MovieItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return "User rating: " + vote_average ;
    }

    public void setVoteAverage(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease() {
        return "Release date: " + release_date;
    }

    public void setRelease(String release_date){
        this.release_date = release_date;
    }

    public void setId(String id) {
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setKey(String key) {
        this.key = key;

    }

    public String getKey() {
        return "https://www.youtube.com/watch?v=" + key;

    }

    public void setName(String name) {
       this.name = name;

    }

    public String getName() {
        return name;
    }


}

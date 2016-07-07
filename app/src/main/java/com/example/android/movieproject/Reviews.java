package com.example.android.movieproject;

import java.io.Serializable;

/**
 * Created by carolinestewart on 6/9/16.
 */
public class Reviews implements Serializable {
    private String review;
    private String author;
    private String url;

    public Reviews(String review, String author, String url) {

        this.review = review;
        this.author = author;
        this.url = url;

    }

    public String getReview() {
        return review;
    }
    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}
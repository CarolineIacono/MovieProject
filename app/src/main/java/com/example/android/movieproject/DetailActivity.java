package com.example.android.movieproject;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


public class DetailActivity extends FragmentActivity  {

    public static final String EXTRA_MOVIE = "movie";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);


        MovieItem movieItem = (MovieItem) getIntent().getSerializableExtra(EXTRA_MOVIE);
        DetailFragment detailFragment = new DetailFragment(movieItem);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, detailFragment);
        fragmentTransaction.commit();

    }


}
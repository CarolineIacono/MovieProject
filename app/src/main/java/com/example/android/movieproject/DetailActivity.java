package com.example.android.movieproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;


public class DetailActivity extends FragmentActivity implements DetailFragment.OnFragmentInteractionListener {

    public static final String EXTRA_MOVIE = "movie";
    public static final String EXTRA_TRAILERS = "trailers";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);




            MovieItem movieItem = (MovieItem) getIntent().getSerializableExtra(EXTRA_MOVIE);
            DetailFragment detailFragment = new DetailFragment(movieItem);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container,detailFragment);
            fragmentTransaction.commit();

        }



    public void onTrailerClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="));
        startActivity(intent);
    }







    @Override
    public void onFragmentInteraction(Uri uri) {

    }




}
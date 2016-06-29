package com.example.android.movieproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by carolinestewart on 6/7/16.
 */
public class MainActivity extends FragmentActivity implements GridFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean twoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movielayout);

        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true;
        }

    }

    @Override
    public void onMovieClick(MovieItem movieItem) {

        if (twoPane) {
            DetailFragment fragment = new DetailFragment(movieItem);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, movieItem);
            //intent.putExtra(DetailActivity.EXTRA_TRAILERS, (Serializable) trailers.get(position));
            startActivity(intent);
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
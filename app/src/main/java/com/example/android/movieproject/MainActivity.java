package com.example.android.movieproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by carolinestewart on 6/7/16.
 */
public class MainActivity extends FragmentActivity implements GridFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movielayout);
    }

    @Override
    public void onMovieClick(MovieItem movieItem) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movieItem);
        //intent.putExtra(DetailActivity.EXTRA_TRAILERS, (Serializable) trailers.get(position));
        startActivity(intent);
    }
}
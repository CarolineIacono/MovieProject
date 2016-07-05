package com.example.android.movieproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by carolinestewart on 6/7/16.
 */
public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, OnFavoriteListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GridFragment gridFragment;

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
        gridFragment = new GridFragment();
        getSupportFragmentManager().beginTransaction().add(gridFragment, "gridFragment").commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMovieClick(MovieItem movieItem) {

        if (twoPane) {
            DetailFragment fragment = new DetailFragment(movieItem);
            fragment.setFavoriteListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, movieItem);
            startActivity(intent);
        }
    }

    @Override
    public void onUpdateFavorite(String favorite, boolean isFavorited) {

        gridFragment.updateFavorite(favorite, isFavorited);
    }


    public interface OnFragmentInteractionListener {

        void onMovieClick(MovieItem movieItem);

    }
}
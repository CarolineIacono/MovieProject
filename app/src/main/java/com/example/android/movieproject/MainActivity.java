package com.example.android.movieproject;

import android.net.Uri;
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
    public void onFragmentInteraction(Uri uri) {

    }
}
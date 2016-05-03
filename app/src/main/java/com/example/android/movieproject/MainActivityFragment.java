package com.example.android.movieproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;

/**
 * Created by carolinestewart on 5/3/16.
 */
public class MainActivityFragment extends Fragment {

    private MovieGridAdapter movieAdapter;

    MovieItem[] movieItems = {

            new MovieItem("Aladdin", "2", 0),
            new MovieItem("Cinderella", "2", 0),
            new MovieItem("101 Dalmations", "101", 0),
            new MovieItem("Snow White", "3", 0),
            new MovieItem("Lion King", "3", 0)

    };

    public MainActivityFragment() {

    }


 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View rootView = inflater.inflate(R.layout.movielayout, container, false);

     movieAdapter = new MovieGridAdapter(getActivity(), Arrays.asList(movieItems));

     GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
     gridView.setAdapter(movieAdapter);

     return rootView;
 }
}

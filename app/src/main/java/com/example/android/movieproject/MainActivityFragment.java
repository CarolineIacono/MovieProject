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

            new MovieItem("Aladdin", "A boy and his carpet."),
            new MovieItem("Cinderella", "A girl and her shoes."),
            new MovieItem("101 Dalmations", "A woman and her fur coat."),
            new MovieItem("Snow White", "A witch and her apple."),
            new MovieItem("Lion King", "A lion and his daddy issues.")

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

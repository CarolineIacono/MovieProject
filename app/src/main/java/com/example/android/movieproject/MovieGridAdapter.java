package com.example.android.movieproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by carolinestewart on 5/2/16.
 */
public class MovieGridAdapter extends ArrayAdapter<MovieItem> {
    private static final String LOG_TAG = MovieGridAdapter.class.getSimpleName();

    public MovieGridAdapter (Activity context, List<MovieItem> movieItems) {
        super(context, 0, movieItems);
    }
@Override
    public View getView(int position, View convertView, ViewGroup parent) {

    if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(
            R.layout.movie_item, parent, false);

    }


    TextView name = (TextView)convertView.findViewById(R.id.name);
    name.setText(getItem(position).name);

    return convertView;
}

}

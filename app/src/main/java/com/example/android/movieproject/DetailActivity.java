package com.example.android.movieproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by carolinestewart on 5/6/16.
 */
public class DetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);

        MovieItem movieItem = (MovieItem)getIntent().getSerializableExtra(GridViewActivity.EXTRA_MOVIE);
        TextView overview = (TextView)findViewById(R.id.overview);
        overview.setText(movieItem.getOverview());

        TextView title = (TextView)findViewById(R.id.title);
        title.setText(movieItem.getTitle());


        ImageView imageView = (ImageView)findViewById(R.id.movieImage);
        Picasso.with(this).load(movieItem.getImage()).into(imageView);

        TextView voteAverage = (TextView)findViewById(R.id.vote_average);
        voteAverage.setText(movieItem.getVote());

        TextView releaseDate = (TextView)findViewById(R.id.release_date);
        releaseDate.setText(movieItem.getRelease());







    }
}
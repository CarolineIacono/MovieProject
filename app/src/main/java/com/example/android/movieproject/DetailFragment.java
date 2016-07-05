package com.example.android.movieproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DetailFragment extends Fragment {

    private static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    private boolean isFavorited = false;
    public Button favoriteButton;
    private MovieItem movieItem;


    private OnFavoriteListener favoriteListener;

    public DetailFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public DetailFragment(MovieItem movieItem) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MOVIE, movieItem);
        setArguments(args);
    }

    private void updateButton() {
        if (isFavorited) {
            favoriteButton.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        } else {
            favoriteButton.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
        }
    }

    public void setFavoriteListener (OnFavoriteListener favoriteListener) {
        this.favoriteListener = favoriteListener;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        movieItem = (MovieItem) getArguments().getSerializable(EXTRA_MOVIE);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> set = sharedPref.getStringSet(getActivity().getResources().getString(R.string.favorite), null);
        if (set != null) {
            if (set.contains(String.valueOf(movieItem.getId()))) {
                isFavorited = true;
            }
        }

        // Inflate the layout for this fragment
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.details_fragment, container, false);


        // Create an asyncTask to get the trailer
        AsyncTask<String, Void, List<Trailer>> getDetailTask = new AsyncTask<String, Void, List<Trailer>>() {
            @Override
            protected List<Trailer> doInBackground(String... ids) {
                String id = ids[0];

                String trailerUrl = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=3bdc29f12e89d25098ebe99dbec16f9b";
                String unformattedJSON = DataUtil.fetch(trailerUrl);
                return parseTrailer(unformattedJSON);
            }


            @Override
            protected void onPostExecute(List<Trailer> trailers) {
                TextView overview = (TextView) layout.findViewById(R.id.overview);
                overview.setText(movieItem.getOverview());

                TextView title = (TextView) layout.findViewById(R.id.title);
                title.setText(movieItem.getTitle());


                ImageView imageView = (ImageView) layout.findViewById(R.id.movieImage);
                Picasso.with(getContext()).load(movieItem.getImage()).into(imageView);

                TextView voteAverage = (TextView) layout.findViewById(R.id.vote_average);
                voteAverage.setText(movieItem.getVoteAverage());

                TextView releaseDate = (TextView) layout.findViewById(R.id.release_date);
                releaseDate.setText(movieItem.getRelease());

                ViewGroup trailerContainer = (ViewGroup) layout.findViewById(R.id.trailer_container);
                for (final Trailer trailer : trailers) {
                    ViewGroup trailerView = (ViewGroup) inflater.inflate(R.layout.trailer_item, trailerContainer, false);
                    ((TextView) trailerView.findViewById(R.id.trailer_name)).setText(trailer.getName());
                    trailerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey()));
                            startActivity(intent);
                        }
                    });

                    trailerContainer.addView(trailerView);
                }

            }
        };
        AsyncTask<String, Void, List<Reviews>> getReviewTask = new AsyncTask<String, Void, List<Reviews>>() {
            @Override
            protected List<Reviews> doInBackground(String... ids) {
                String id = ids[0];

                String reviewUrl = "https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=3bdc29f12e89d25098ebe99dbec16f9b";
                String unformattedJSON = DataUtil.fetch(reviewUrl);
                return parseReviews(unformattedJSON);
            }

            @Override
            protected void onPostExecute(List<Reviews> reviews) {
                super.onPostExecute(reviews);


                ViewGroup reviewsContainer = (ViewGroup) layout.findViewById(R.id.review_container);
                for (final Reviews review : reviews) {
                    ViewGroup reviewsView = (ViewGroup) inflater.inflate(R.layout.review_item, reviewsContainer, false);
                    ((TextView) reviewsView.findViewById(R.id.review_name)).setText(review.getReview());
                    ((TextView) reviewsView.findViewById(R.id.author)).setText(review.getAuthor());
                    reviewsView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
                            startActivity(intent);
                        }
                    });


                    reviewsContainer.addView(reviewsView);
                }
            }
        };


        getDetailTask.execute(movieItem.getId());
        getReviewTask.execute(movieItem.getId());


        favoriteButton = (Button) layout.findViewById(R.id.favoriteButton);
        updateButton();

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorited = !isFavorited;
                updateButton();
                if(favoriteListener != null) {
                    favoriteListener.onUpdateFavorite(movieItem.getId(), isFavorited);
                }
            }
        });

        return layout;
    }


    @Override
    public void onPause() {
        if(favoriteListener == null) {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            Set<String> set = new HashSet<>(sharedPref.getStringSet(getActivity().getResources().getString(R.string.favorite), new HashSet<String>()));


            if (isFavorited) {
                set.add(String.valueOf(movieItem.getId()));
            } else {
                set.remove(String.valueOf(movieItem.getId()));
            }

            editor.putStringSet(getActivity().getResources().getString(R.string.favorite), set);
            editor.commit();
        }
        super.onPause();

    }

    private List<Trailer> parseTrailer(String result) {
        List<Trailer> trailers = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(result);
            JSONArray trailerArray = root.optJSONArray("results");
            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject t = trailerArray.optJSONObject(i);

                String key = t.optString("key");
                String name = t.optString("name");

                trailers.add(new Trailer(key, name));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailers;
    }


    private List<Reviews> parseReviews(String result) {
        List<Reviews> review = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(result);
            JSONArray reviewArray = root.optJSONArray("results");
            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject t = reviewArray.optJSONObject(i);

                String reviews = t.optString("content");
                String author = t.optString("author");
                String url = t.optString("url");


                review.add(new Reviews(reviews, author, url));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return review;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}

package com.example.android.movieproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class GridFragment extends Fragment {
    private static final String FEED_URL = "https://api.themoviedb.org/3/movie/popular?api_key=3bdc29f12e89d25098ebe99dbec16f9b";
    private static final String FEED_URL2 = "http://api.themoviedb.org/3/movie/top_rated?api_key=3bdc29f12e89d25098ebe99dbec16f9b";
    private static final String FEED_URL3 = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "?api_key=3bdc29f12e89d25098ebe99dbec16f9b";

    private OnFragmentInteractionListener mListener;

    private ArrayList<MovieItem> gridData;
    private MovieGridAdapter gridAdapter;
    private Set<String> favoriteSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_grid, container, false);

        GridView gridView = (GridView) layout.findViewById(R.id.gridView);

        gridData = new ArrayList<>();
        gridAdapter = new MovieGridAdapter(getContext(), R.layout.movie_item, gridData);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            ///take to the details view
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (mListener != null) {
                    mListener.onMovieClick((MovieItem) parent.getItemAtPosition(position));
                }
            }
        });

        new MovieFetchTask().execute(FEED_URL);

        return layout;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        favoriteSet = new HashSet<>(sharedPref.getStringSet(getActivity().getResources().getString(R.string.favorite), new HashSet<String>()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        gridData.clear();

        switch (item.getItemId()) {

            case R.id.popular:
                new MovieFetchTask().execute(FEED_URL2);
                break;
            case R.id.rated:
                new MovieFetchTask().execute(FEED_URL);
                break;
            case R.id.favorite:
                Iterator<String> iterator = favoriteSet.iterator();
                while (iterator.hasNext()) {
                    new MovieFetchTask().execute(FEED_URL3 + iterator.next() + API_KEY);
                }


        }

        return true;
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
    public class MovieFetchTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String urlString = params[0];
            return parseMovieItems(DataUtil.fetch(urlString));

        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 1) {

                gridAdapter.setGridData(gridData);

            } else {
                Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

        }

        private int parseMovieItems(String result) {
            int success = 0;
            try {
                JSONObject response = new JSONObject(result);
                JSONArray posts = response.optJSONArray("results");
                if (posts == null) {
                    parseSingleObject(response);
                } else {

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);
                        parseSingleObject(post);
                    }
                }
                success = 1;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return success;


        }
    }

    @Override
    public void onPause() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putStringSet(getActivity().getResources().getString(R.string.favorite), favoriteSet);
        editor.commit();

        super.onPause();
    }

    public void updateFavorite(String favorite, boolean isFavorited) {
        if (isFavorited) {
            //adding to the set of favorite movies
            favoriteSet.add(favorite);
        } else {
            favoriteSet.remove(favorite);
        }
    }

    public void parseSingleObject(JSONObject post) {
        String title = post.optString("title");
        MovieItem item = new MovieItem();
        item.setTitle(title);


        String image = post.optString("poster_path");
        image = "http://image.tmdb.org/t/p/w185/" + image;
        item.setImage(image);

        String overview = post.optString("overview");
        item.setOverview(overview);

        String vote = post.optString("vote_average");
        item.setVoteAverage(vote);

        String release = post.optString("release_date");
        item.setRelease(release);

        item.setId(post.optString("id"));

        gridData.add(item);
    }
}


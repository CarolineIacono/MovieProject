package com.example.android.movieproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class GridFragment extends Fragment {
    private static final String FEED_URL = "https://api.themoviedb.org/3/movie/popular?api_key=3bdc29f12e89d25098ebe99dbec16f9b";
    private static final String FEED_URL2 = "http://api.themoviedb.org/3/movie/top_rated?api_key=3bdc29f12e89d25098ebe99dbec16f9b";

    private OnFragmentInteractionListener mListener;

    private Map<Integer, List<MovieItem>> trailers = new HashMap<>();
    private ArrayList<MovieItem> gridData;
    private MovieGridAdapter gridAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_grid, container, false);

        GridView gridView = (GridView) layout.findViewById(R.id.gridView);

        gridData = new ArrayList<>();
        gridAdapter = new MovieGridAdapter(getContext(), R.layout.movie_item, gridData);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            ///take to the details view
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_MOVIE, (MovieItem) parent.getItemAtPosition(position));
                intent.putExtra(DetailActivity.EXTRA_TRAILERS, (Serializable) trailers.get(position));
                startActivity(intent);
            }
        });

        new MovieFetchTask().execute(FEED_URL);

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void OnItemClick(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class MovieFetchTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String urlString = params[0];


            int result = parseResult(fetch(urlString));
            for (int i = 0; i < gridData.size(); i++) {
                MovieItem item = gridData.get(i);
                String trailerUrl = "https://api.themoviedb.org/3/movie/" + item.getId() + "/videos?api_key=3bdc29f12e89d25098ebe99dbec16f9b";
                String unformattedJSON = fetch(urlString);
                parseTrailer(i, unformattedJSON);

            }
            return 1;

        }

        private String fetch(String urlString) {

            Integer result = 0;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            try {

                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                forecastJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return forecastJsonStr;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 1) {

                gridAdapter.setGridData(gridData);

            } else {
                Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

        }

        private int parseResult(String result) {
            String id;
            int success = 0;
            try {
                JSONObject response = new JSONObject(result);
                JSONArray posts = response.optJSONArray("results");
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);

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

                    id = post.optString("id");
                    item.setId(id);


                    gridData.add(item);
                }
                success = 1;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return success;

        }

        private void parseTrailer(Integer index, String result) {

            ArrayList<MovieItem> movieItems = new ArrayList<>();

            try {
                JSONObject trailer = new JSONObject(result);
                JSONArray posts = trailer.optJSONArray("results");
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);

                    String key = post.optString("key");
                    MovieItem item = new MovieItem();
                    item.setKey(key);

                    String name = post.optString("name");
                    item.setName(name);

                    movieItems.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            trailers.put(index, movieItems);
        }

    }
}

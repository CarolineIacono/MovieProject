package com.example.android.movieproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by carolinestewart on 5/6/16.
 */
public class GridViewActivity extends AppCompatActivity {
    private static final String TAG = GridViewActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "movie";

    private GridView mGridView;

    private MovieGridAdapter mGridAdapter;
    private ArrayList<MovieItem> mGridData;
    private String FEED_URL = "https://api.themoviedb.org/3/movie/popular?api_key=[API KEY IN README]";
    String FEED_URL2 = "http://api.themoviedb.org/3/movie/top_rated?api_key=[API KEY IN README]";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mGridData.clear();

        switch (item.getItemId()) {

            case R.id.popular:
                new AsyncHttpTask().execute(FEED_URL2);
                break;
            case R.id.rated:
                new AsyncHttpTask().execute(FEED_URL);
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movielayout);

        mGridView = (GridView) findViewById(R.id.gridView);

        mGridData = new ArrayList<>();
        mGridAdapter = new MovieGridAdapter(this, R.layout.movie_item, mGridData);
        mGridView.setAdapter(mGridAdapter);



        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           ///take to the details view

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent intent = new Intent(GridViewActivity.this, DetailActivity.class);
                intent.putExtra(EXTRA_MOVIE, (MovieItem)parent.getItemAtPosition(position));
                startActivity(intent);


            }
        });


        new AsyncHttpTask().execute(FEED_URL);

    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String urlString = params[0];
            Integer result = 0;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {

                URL url = new URL(urlString);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();

                parseResult(forecastJsonStr);
                result = 1;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
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

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 1) {

                mGridAdapter.setGridData(mGridData);

            } else {
                Toast.makeText(GridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }


            }

        }

        private void parseResult(String result) {
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
                    item.setVote(vote);

                    String release = post.optString("release_date");
                    item.setRelease(release);








                    mGridData.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




    }








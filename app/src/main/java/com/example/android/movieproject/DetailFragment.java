package com.example.android.movieproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetailFragment extends Fragment {

    private static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    private OnFragmentInteractionListener mListener;


    public DetailFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public DetailFragment(MovieItem movieItem) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MOVIE, movieItem);
        setArguments(args);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final MovieItem movieItem = (MovieItem) getArguments().getSerializable(EXTRA_MOVIE);

        // Inflate the layout for this fragment
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.details_fragment, container, false);


        // Create an asyncTask to get the trailer
        AsyncTask<String, Void, List<Trailer>> task = new AsyncTask<String, Void, List<Trailer>>() {
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
                for (Trailer trailer : trailers) {
                    ViewGroup trailerView = (ViewGroup) inflater.inflate(R.layout.trailer_item, trailerContainer, false);
                    ((TextView) trailerView.findViewById(R.id.trailer_name)).setText(trailer.name);


                    trailerContainer.addView(trailerView);
                }

            }
        };

        task.execute(movieItem.getId());

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}

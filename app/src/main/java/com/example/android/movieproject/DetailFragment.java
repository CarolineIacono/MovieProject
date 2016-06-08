package com.example.android.movieproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.details_fragment, container, false);
        MovieItem movieItem = (MovieItem)getArguments().getSerializable(EXTRA_MOVIE);




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

            // List<MovieItem> trailerItem = (ArrayList<MovieItem>) getIntent().getSerializableExtra(GridViewActivity.EXTRA_TRAILERS);
            //ListView trailers = (ListView) findViewById(R.id.trailer);
            //trailerItem.setKey(movieItem.getKey());


        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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

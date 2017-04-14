package com.mal.ahmed.popularmovies.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mal.ahmed.popularmovies.Adapters.MyAdapter;
import com.mal.ahmed.popularmovies.Activities.Main;
import com.mal.ahmed.popularmovies.Movie;
import com.mal.ahmed.popularmovies.MoviesTable;
import com.mal.ahmed.popularmovies.R;
import com.mal.ahmed.popularmovies.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String sortType;
    private ArrayList<Movie> resultSet;
    int recyclerPostion;


    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("toolbar", sortType);
        outState.putInt("postion", recyclerPostion);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        ((Main)getActivity()).clearfragment();
        if (id == R.id.action_popularity) {
            sortType = "popularity.desc";
            mAdapter.clear();
            setSortTypeToToolbar();
            getData(sortType);
            return true;
        }
        if (id == R.id.action_vote) {
            sortType = "vote_average.desc";
            mAdapter.clear();
            setSortTypeToToolbar();
            getData(sortType);
            return true;
        }

        if (id == R.id.action_revenue) {
            sortType = "revenue.desc";
            mAdapter.clear();
            setSortTypeToToolbar();
            getData(sortType);
            return true;
        }
        if (id == R.id.action_vote_count) {
            sortType = "vote_count.desc";
            mAdapter.clear();
            setSortTypeToToolbar();
            getData(sortType);
            return true;
        }
        if (id == R.id.action_favorite) {
            showFavorite();
            return true;
        }


        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        ((Main)getActivity()).clearfragment();

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && getActivity().getResources().getConfiguration().screenWidthDp < 580)
            mLayoutManager = new GridLayoutManager(getActivity(),4);

        else {
            mLayoutManager = new GridLayoutManager(getActivity(),2);
        }


        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Movie item = mAdapter.getItem(position);
                        recyclerPostion = position;
                        ((Main) getActivity()).setMovie(item);
                    }

                    @Override
                    public void onItemLongPress(View childView, int position) {

                    }
                })
        );


        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Main)getActivity()).clearfragment();

    }

    private void getDataFromBD() {
        Cursor cursor = getActivity().getContentResolver().query(MoviesTable.CONTENT_URI, null, null, null, null);
        ArrayList<Movie> movies = (ArrayList<Movie>) MoviesTable.getRows(cursor, false);
        mAdapter.setItems(movies);
    }


    private void setSortTypeToToolbar() {
        if (sortType.equals("popularity.desc")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Popularity");
        }
        if (sortType.equals("vote_average.desc")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Vote Average");
        }
        if (sortType.equals("revenue.desc")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Revenue");
        }
        if (sortType.equals("vote_count.desc")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Vote count");
        }
        if (sortType.equals("Favorite")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Favorite");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            sortType = savedInstanceState.getString("toolbar");
            recyclerPostion = savedInstanceState.getInt("postion");
        } else {
            recyclerPostion = 0;
            sortType = "popularity.desc";
        }


    }

    public void onStart() {
        super.onStart();
        if (sortType != "Favorite") {
            mAdapter.clear();
            setSortTypeToToolbar();
            getData(sortType);
        } else {
            showFavorite();
        }
        if (recyclerPostion != 0) {
            mRecyclerView.scrollToPosition(recyclerPostion);
        }
    }

    public void upDateMovies(ArrayList<Movie> resultSet) {

        mAdapter.setItems(resultSet);

    }


    public void getData(String sortType) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String BASE = "http://api.themoviedb.org/3/discover/movie?=";
        final String SORT = "sort_by";
        final String API = "api_key";
        final String key = "14f5cc9c009d46efae06b2838783af1e";
        resultSet = new ArrayList<>();
        Uri uri = Uri.parse(BASE).buildUpon().
                appendQueryParameter(SORT, sortType).
                appendQueryParameter(API, key).
                build();
        String url = uri.toString();
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            resultSet = getMoviesDataFromJson(response);
                            upDateMovies(resultSet);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showFavorite();
                Toast.makeText(getActivity(), "Connection error !", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void showFavorite() {
        sortType = "Favorite";
        setSortTypeToToolbar();
        mAdapter.clear();
        getDataFromBD();
    }


    private ArrayList<Movie> getMoviesDataFromJson(String forecastJsonStr)
            throws JSONException {
        final String RESULTS = "results";
        final String POSTER = "poster_path";
        final String OVERVIEW = "overview";
        final String ID = "id";
        final String DATE = "release_date";
        final String TITLE = "title";
        final String VOTEAVARAGE = "vote_average";
        ArrayList<Movie> resultSet = new ArrayList<>();
        JSONObject moviesJson = new JSONObject(forecastJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(RESULTS);
        for (int i = 0; i < moviesArray.length(); i++) {
            String poster;
            String overview;
            String id;
            String date;
            String name;
            String voteAvarage;
            JSONObject itemMovie = moviesArray.getJSONObject(i);
            poster = itemMovie.getString(POSTER);
            id = itemMovie.getString(ID);
            overview = itemMovie.getString(OVERVIEW);
            date = itemMovie.getString(DATE);
            name = itemMovie.getString(TITLE);
            voteAvarage = itemMovie.getString(VOTEAVARAGE);
            Movie movie = new Movie(poster, overview, id, date, name, voteAvarage, "");
            resultSet.add(movie);
        }
        return resultSet;
    }

    public interface Callback {
        public void setMovie(Movie m);
        public void clearfragment();
    }
}

package com.mal.ahmed.popularmovies.Fragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mal.ahmed.popularmovies.Activities.Main;
import com.mal.ahmed.popularmovies.Adapters.ReviewsAdapter;
import com.mal.ahmed.popularmovies.Adapters.VideosAdapter;
import com.mal.ahmed.popularmovies.Movie;
import com.mal.ahmed.popularmovies.MoviesTable;
import com.mal.ahmed.popularmovies.R;
import com.mal.ahmed.popularmovies.RecyclerItemClickListener;
import com.mal.ahmed.popularmovies.Review;
import com.mal.ahmed.popularmovies.Video;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {
    private static DetailsFragment detailsFragment;
    private ImageView detailsIV;
    private TextView overviewTV;
    private TextView voteTV;
    private TextView videosErrorTV;
    private TextView reviewsErrorTV;
    private TextView dateTV;
    private TextView titleTV;
    private Movie currentItem;
    private FloatingActionButton fabFavorite;

    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private RecyclerView.LayoutManager reviewsLayoutManager;

    private RecyclerView videosRecyclerView;
    private VideosAdapter videosAdapter;
    private RecyclerView.LayoutManager videosLayoutManager;
    private ShareActionProvider mShareActionProvider;
    private boolean added;
    private Intent shareIntent;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance(Movie m) {

        detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", m);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_details, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
    }

    private void changeShareIntent() {
        if (currentItem.getTrailers() != null && currentItem.getTrailers().get(0) != null) {
            String shareText = "i like this movie '" + currentItem.getName() + "' !        " + "watch it : http://www.youtube.com/watch?v=" + currentItem.getTrailers().get(0).getKey();
            shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain").setText(shareText).getIntent();
        } else {
            shareIntent = null;
        }
    }

    private void setShare() {
        changeShareIntent();
        if (shareIntent != null && mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_share) {
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_details, container, false);

        overviewTV = (TextView) rootview.findViewById(R.id.overview);
        voteTV = (TextView) rootview.findViewById(R.id.vote_tv);
        dateTV = (TextView) rootview.findViewById(R.id.date_tv);
        titleTV = (TextView) rootview.findViewById(R.id.title_tv);
        reviewsErrorTV = (TextView) rootview.findViewById(R.id.reviews_error_tv);
        videosErrorTV = (TextView) rootview.findViewById(R.id.videos_error_tv);
        detailsIV = (ImageView) rootview.findViewById(R.id.details_iv);
        fabFavorite = (FloatingActionButton) rootview.findViewById(R.id.fabFavorite);

        currentItem = detailsFragment.getArguments().getParcelable("item");

        reviewsRecyclerView = (RecyclerView) rootview.findViewById(R.id.reviews_recycler_view);
        reviewsRecyclerView.setHasFixedSize(true);
        reviewsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        reviewsRecyclerView.setNestedScrollingEnabled(false);
        reviewsAdapter = new ReviewsAdapter();
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        videosRecyclerView = (RecyclerView) rootview.findViewById(R.id.trailers_recycler_view);
        videosRecyclerView.setHasFixedSize(true);
        videosLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        videosRecyclerView.setLayoutManager(videosLayoutManager);
        videosAdapter = new VideosAdapter();
        videosRecyclerView.setAdapter(videosAdapter);

        videosRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Video item = videosAdapter.getItem(position);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + item.getKey())));

                    }

                    @Override
                    public void onItemLongPress(View childView, int position) {

                    }
                })
        );

        Cursor cursor = getActivity().getContentResolver().query(MoviesTable.CONTENT_URI, null, MoviesTable.FIELD_ID + "=" + currentItem.getId(), null, null);
        ArrayList<Movie> checker = (ArrayList<Movie>) MoviesTable.getRows(cursor, false);
        if (checker.size() == 0) {
            added = false;
            fabFavorite.setImageDrawable(ContextCompat.getDrawable(getContext(), android.R.drawable.star_big_off));
        } else {
            added = true;
            fabFavorite.setImageDrawable(ContextCompat.getDrawable(getContext(), android.R.drawable.star_big_on));
        }
        reviewsErrorTV.setVisibility(View.GONE);
        videosErrorTV.setVisibility(View.GONE);


        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!added) {
                    fabFavorite.setImageDrawable(ContextCompat.getDrawable(getContext(), android.R.drawable.star_big_on));
                    Bitmap bitmap = ((BitmapDrawable) detailsIV.getDrawable()).getBitmap();

                    System.out.println("......................." + currentItem.getId());

                    currentItem.setPosterPath(saveImage(bitmap, currentItem.getPoster()));
                    getActivity().getContentResolver().insert(MoviesTable.CONTENT_URI, MoviesTable.getContentValues(currentItem, false));
                    Toast.makeText(getActivity(), "Added to favorite", Toast.LENGTH_SHORT).show();
                    added = true;
                } else {
                    fabFavorite.setImageDrawable(ContextCompat.getDrawable(getContext(), android.R.drawable.star_big_off));
                    getActivity().getContentResolver().delete(MoviesTable.CONTENT_URI, MoviesTable.FIELD_ID + "=" + currentItem.getId(), null);
                    System.out.println("......................." + currentItem.getId());
                    deleteImage(currentItem.getPoster());
                    currentItem.setPosterPath("");
                    Toast.makeText(getActivity(), "Removed from favorite", Toast.LENGTH_SHORT).show();
                    added = false;
                }

            }
        });


        return rootview;
    }

    private boolean dbContain(Movie item) {
        Cursor cursor = getActivity().getContentResolver().query(MoviesTable.CONTENT_URI, null, null, null, null);
        ArrayList<Movie> movies = (ArrayList<Movie>) MoviesTable.getRows(cursor, false);
        for (Movie m : movies) {

            if (m.getId().equals(item.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (currentItem.getPosterPath().equals("")) {
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/".concat(currentItem.getPoster())).into(detailsIV);
        } else {
            Bitmap b = loadImage(currentItem.getPosterPath(), currentItem.getPoster());
            detailsIV.setImageBitmap(b);
        }
        overviewTV.setText(currentItem.getOverview());
        voteTV.setText(currentItem.getVoteAvarage());
        dateTV.setText(currentItem.getDate());
        titleTV.setText(currentItem.getName());
        showTrailers();
        showReviews();
    }

    private Bitmap loadImage(String path, String poster) {

        Bitmap b = null;
        try {
            File f = new File(path, poster);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    private void deleteImage(String poster) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File mypath = new File(directory, poster);
        try {
            mypath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private String saveImage(Bitmap bitmapImage, String poster) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File mypath = new File(directory, poster);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public void showReviews() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String key = "14f5cc9c009d46efae06b2838783af1e";
        String url = "http://api.themoviedb.org/3/movie/" + currentItem.getId() + "/reviews?api_key=" + key;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            ArrayList<Review> resultSet;
                            resultSet = getReviewsDataFromJson(response);
                            if (resultSet.size() == 0) {
                                reviewsErrorTV.setText("No reviews for this movie");
                                reviewsErrorTV.setVisibility(View.VISIBLE);
                            } else {
                                currentItem.setReviews(resultSet);
                                updateReviewsRV(currentItem);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                reviewsErrorTV.setVisibility(View.VISIBLE);
            }
        });
        queue.add(stringRequest);
    }


    private void updateReviewsRV(Movie m) {
        ArrayList<Review> reviews = m.getReviews();
        reviewsAdapter.setItems(reviews);

    }


    private ArrayList<Review> getReviewsDataFromJson(String forecastJsonStr)
            throws JSONException {
        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        ArrayList<Review> resultSet = new ArrayList<>();
        JSONObject reviewsJson = new JSONObject(forecastJsonStr);
        JSONArray reviewsArray = reviewsJson.getJSONArray(RESULTS);
        for (int i = 0; i < reviewsArray.length(); i++) {
            String author;
            String content;
            JSONObject itemMovie = reviewsArray.getJSONObject(i);
            author = itemMovie.getString(AUTHOR);
            content = itemMovie.getString(CONTENT);
            Review review = new Review(author, content);
            resultSet.add(review);
        }
        return resultSet;
    }


    public void showTrailers() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String key = "14f5cc9c009d46efae06b2838783af1e";
        String url = "http://api.themoviedb.org/3/movie/" + currentItem.getId() + "/videos?api_key=" + key;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            ArrayList<Video> resultSet;
                            resultSet = getVideosDataFromJson(response);
                            if (resultSet.size() == 0) {
                                videosErrorTV.setText("No trailers for this movie");
                                videosErrorTV.setVisibility(View.VISIBLE);
                            } else {
                                currentItem.setTrailers(resultSet);
                                updateVideosRV(currentItem);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                videosErrorTV.setVisibility(View.VISIBLE);
            }
        });
        queue.add(stringRequest);
    }

    private void updateVideosRV(Movie m) {
        ArrayList<Video> videos = m.getTrailers();
        videosAdapter.setItems(videos);
        setShare();

    }


    private ArrayList<Video> getVideosDataFromJson(String forecastJsonStr)
            throws JSONException {
        final String RESULTS = "results";
        final String KEY = "key";
        final String NAME = "name";
        ArrayList<Video> resultSet = new ArrayList<>();
        JSONObject reviewsJson = new JSONObject(forecastJsonStr);
        JSONArray reviewsArray = reviewsJson.getJSONArray(RESULTS);
        for (int i = 0; i < reviewsArray.length(); i++) {
            String key;
            String name;
            JSONObject itemMovie = reviewsArray.getJSONObject(i);
            key = itemMovie.getString(KEY);
            name = itemMovie.getString(NAME);
            Video videos = new Video(name, key);
            resultSet.add(videos);
        }
        return resultSet;
    }


}

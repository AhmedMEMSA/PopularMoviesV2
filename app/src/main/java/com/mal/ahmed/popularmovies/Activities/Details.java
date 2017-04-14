package com.mal.ahmed.popularmovies.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.mal.ahmed.popularmovies.Fragments.DetailsFragment;
import com.mal.ahmed.popularmovies.Movie;
import com.mal.ahmed.popularmovies.R;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Movie Details");

        Movie m = getIntent().getParcelableExtra("item");
        DetailsFragment detailsActivityFragment = DetailsFragment.newInstance(m);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, detailsActivityFragment).commit();

    }



}

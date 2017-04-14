package com.mal.ahmed.popularmovies.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.mal.ahmed.popularmovies.Fragments.DetailsFragment;
import com.mal.ahmed.popularmovies.Fragments.MainFragment;
import com.mal.ahmed.popularmovies.Movie;
import com.mal.ahmed.popularmovies.R;

public class Main extends AppCompatActivity implements MainFragment.Callback {

    public boolean twoPane;
    DetailsFragment detailsActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.fragment1) != null) {
            twoPane = true;
        } else {
            twoPane = false;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        clearfragment();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void setMovie(Movie m) {
        if (twoPane) {
            detailsActivityFragment = DetailsFragment.newInstance(m);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, detailsActivityFragment).commit();

        } else {
            Intent i = new Intent(Main.this, Details.class);
            i.putExtra("item", m);
            startActivity(i);
        }
    }

    @Override
    public void clearfragment() {
        if (detailsActivityFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(detailsActivityFragment).commit();
        }
    }
}


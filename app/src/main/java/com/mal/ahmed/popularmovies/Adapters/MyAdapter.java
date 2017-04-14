package com.mal.ahmed.popularmovies.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mal.ahmed.popularmovies.Movie;
import com.mal.ahmed.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 3/12/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PersonViewHolder> {


    private Context context;

    public MyAdapter() {
        items = new ArrayList<>();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView item;
        CardView cv;


        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            item = (ImageView) itemView.findViewById(R.id.item_iv);

        }


    }

    ArrayList<Movie> items;

    public MyAdapter(ArrayList<Movie> items) {
        this.items = items;
    }

    public List<Movie> getItems() {
        return items;
    }

    public void setItems(ArrayList<Movie> items) {
        this.items = items;
        this.notifyDataSetChanged();

    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.main_grid_item, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        if (items.get(position).getPosterPath().equals("")) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/".concat(items.get(position).getPoster())).into(holder.item);
        } else {
            Bitmap b = loadImage(items.get(position).getPosterPath(),items.get(position).getPoster());
            holder.item.setImageBitmap(b);
        }
    }
    private Bitmap loadImage(String path , String poster) {

        Bitmap b = null;
        try {
            File f = new File(path, poster);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
        this.notifyDataSetChanged();
    }

    public Movie getItem(int position) {
        return items.get(position);
    }


}
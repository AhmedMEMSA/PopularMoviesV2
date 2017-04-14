package com.mal.ahmed.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mal.ahmed.popularmovies.R;
import com.mal.ahmed.popularmovies.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmed on 3/12/2016.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.PersonViewHolder> {


    private Context context;

    public VideosAdapter() {
        items = new ArrayList<>();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView rcv;
        ImageView youTube;
        TextView trailers;


        PersonViewHolder(View itemView) {
            super(itemView);
            rcv = (CardView) itemView.findViewById(R.id.videos_card_view);
            youTube = (ImageView) itemView.findViewById(R.id.videos_item_iv);
            trailers = (TextView) itemView.findViewById(R.id.videos_item_tv);
        }


    }

    ArrayList<Video> items;

    public VideosAdapter(ArrayList<Video> items) {
        this.items = items;
    }

    public ArrayList<Video> getItems() {
        return items;
    }

    public void setItems(ArrayList<Video> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.videos_rv_item, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
            holder.trailers.setText(items.get(position).getName());
        Picasso.with(context).load("http://img.youtube.com/vi/" + items.get(position).getKey() + "/0.jpg").into(holder.youTube);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
        this.notifyDataSetChanged();
    }

    public Video getItem(int position) {
        return items.get(position);
    }


}
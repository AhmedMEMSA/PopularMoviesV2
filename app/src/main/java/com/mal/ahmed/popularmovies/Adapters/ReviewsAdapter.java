package com.mal.ahmed.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mal.ahmed.popularmovies.R;
import com.mal.ahmed.popularmovies.Review;

import java.util.ArrayList;

/**
 * Created by ahmed on 3/12/2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.PersonViewHolder> {


    private Context context;

    public ReviewsAdapter() {
        items = new ArrayList<>();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView rcv;
        TextView anther;
        TextView content;


        PersonViewHolder(View itemView) {
            super(itemView);
            rcv = (CardView) itemView.findViewById(R.id.reviews_card_view);
            anther = (TextView) itemView.findViewById(R.id.reviews_auther_tv);
            content = (TextView) itemView.findViewById(R.id.reviews_content_tv);
        }


    }

    ArrayList<Review> items;

    public ReviewsAdapter(ArrayList<Review> items) {
        this.items = items;
    }

    public ArrayList<Review> getItems() {
        return items;
    }

    public void setItems(ArrayList<Review> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.reviews_rv_item, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
            holder.anther.setText(items.get(position).getAuther());
            holder.content.setText(items.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
        this.notifyDataSetChanged();
    }

    public Review getItem(int position) {
        return items.get(position);
    }


}
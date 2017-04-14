package com.mal.ahmed.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by ahmed on 3/17/2016.
 */
@SimpleSQLTable(table = "movies", provider = "MovieProvider")
public class Movie implements Parcelable{
    @SimpleSQLColumn(value = "id")
    private String id;
    @SimpleSQLColumn(value = "poster")
    private String poster;
    @SimpleSQLColumn(value = "overview")
    private String overview;
    @SimpleSQLColumn(value = "date")
    private String date;
    @SimpleSQLColumn(value = "name")
    private String name;
    @SimpleSQLColumn(value = "voteAvarage")
    private String voteAvarage;
    @SimpleSQLColumn(value = "posterPath")
    private String posterPath;


    private ArrayList<Video> trailers;
    private ArrayList<Review> reviews;


    public Movie(String poster, String overview,String id, String date, String name, String voteAvarage,String posterPath) {
        this.poster = poster;
        this.overview = overview;
        this.id = id;
        this.date = date;
        this.name = name;
        this.voteAvarage = voteAvarage;
        this.posterPath = posterPath;
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        id = in.readString();
        poster = in.readString();
        overview = in.readString();
        date = in.readString();
        name = in.readString();
        voteAvarage = in.readString();
        posterPath = in.readString();
        trailers = in.createTypedArrayList(Video.CREATOR);
        reviews = in.createTypedArrayList(Review.CREATOR);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getVoteAvarage() {
        return voteAvarage;
    }

    public void setVoteAvarage(String voteAvarage) {
        this.voteAvarage = voteAvarage;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Video> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<Video> trailers) {
        this.trailers = trailers;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(date);
        dest.writeString(name);
        dest.writeString(voteAvarage);
        dest.writeString(posterPath);
        dest.writeTypedList(trailers);
        dest.writeTypedList(reviews);
    }
}

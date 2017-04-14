package com.mal.ahmed.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahmed on 4/2/2016.
 */
public class Review implements Parcelable{
    private String auther;
    private String content;



    public Review(String auther, String content) {
        this.auther = auther;
        this.content = content;
    }

    protected Review(Parcel in) {
        auther = in.readString();
        content = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(auther);
        dest.writeString(content);
    }
}

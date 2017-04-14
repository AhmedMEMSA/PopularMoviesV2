package com.mal.ahmed.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahmed on 4/2/2016.
 */
public class Video implements Parcelable{
    private String name;
    private String key;

    public Video(String name, String key) {
        this.name = name;
        this.key = key;
    }

    protected Video(Parcel in) {
        name = in.readString();
        key = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
    }
}

package es.davmartor.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by david on 6/04/16.
 */
public class ParcelableMovie implements Parcelable{

    public static final Creator<ParcelableMovie> CREATOR = new Creator<ParcelableMovie>() {
        @Override
        public ParcelableMovie createFromParcel(Parcel in) {
            return new ParcelableMovie(in);
        }

        @Override
        public ParcelableMovie[] newArray(int size) {
            return new ParcelableMovie[size];
        }
    };

    String title;
    String posterPath;
    String synopsis;
    String rating;
    String releaseDate;

    ParcelableMovie(Parcel movie){
        this.title = movie.readString();
        this.posterPath = movie.readString();
        this.synopsis = movie.readString();
        this.rating = movie.readString();
        this.releaseDate = movie.readString();
    }

    public ParcelableMovie() { }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(synopsis);
        dest.writeString(rating);
        dest.writeString(releaseDate);
    }
}

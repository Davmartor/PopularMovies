package es.davmartor.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by david on 19/05/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "es.davmartor.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";


    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +"/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";


        //Title, stored as string
        public static final String COLUMN_TITLE = "title";


        public static final String COLUMN_POSTER_PATH= "poster_path";
        public static final String COLUMN_SYNOPSIS= "synopsis";
        public static final String COLUMN_RATING= "rating";
        public static final String COLUMN_RELEASE_DATE= "release_date";
        public static final String COLUMN_THE_MOVIE_DB_ID = "movie_id";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

}

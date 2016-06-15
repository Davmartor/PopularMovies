package es.davmartor.popularmovies;

/**
 * Created by david on 20/05/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import es.davmartor.popularmovies.data.MovieContract;

/**
 *  Asyncron task to load movies info
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {

    private String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private Context mContext;

    public FetchMovieTask(Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String MOVIEBD_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String APPID_PARAM = "api_key";


            String orderSettings = Utility.getPreferredOrder(mContext);



            Uri builtUri = Uri.parse( MOVIEBD_BASE_URL + orderSettings ).buildUpon()

                    .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, builtUri.toString());


            // Create the request to The MovieDB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();

            Log.v(LOG_TAG, movieJsonStr);
            getMovieDataFromJson(movieJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();

        }

        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    /**
     * Create Cursor from Json movie
     * @param movieJsonStr
     * @return cursor
     * @throws JSONException
     */
    private void getMovieDataFromJson(String movieJsonStr) throws JSONException {

        //Field to extract from the JSON
        final String ARRAY_RESULTS = "results";

        final String MOVIE_ID = "id";
        final String ORIGINAL_TITLE="original_title";
        final String POSTER_PATH="poster_path";
        final String SYNOPSIS="overview";
        final String USER_RATING="vote_average";
        final String RELEASE_DATE="release_date";


        JSONObject page =  new JSONObject(movieJsonStr);
        JSONArray moviesArray = page.getJSONArray(ARRAY_RESULTS);


        MatrixCursor mc = new MatrixCursor(
                new String[]{
                        "_id",
                        ORIGINAL_TITLE,
                        POSTER_PATH,
                        SYNOPSIS,
                        USER_RATING,
                        RELEASE_DATE,
                });

        Vector<ContentValues> cVVector = new Vector<ContentValues>(moviesArray.length());


        for( int i = 0; i < moviesArray.length(); i++) {
            ContentValues movieValues = new ContentValues();

            JSONObject movie = moviesArray.getJSONObject(i);
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE,movie.getString(ORIGINAL_TITLE));
            movieValues.put(MovieContract.MovieEntry.COLUMN_THE_MOVIE_DB_ID,movie.getInt(MOVIE_ID));
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,movie.getString(POSTER_PATH));
            movieValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS,movie.getString(SYNOPSIS));
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,movie.getString(RELEASE_DATE));
            movieValues.put(MovieContract.MovieEntry.COLUMN_RATING,movie.getString(USER_RATING));

            cVVector.add(movieValues);

            Log.d(LOG_TAG,movie.getString(ORIGINAL_TITLE));

        }


        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,null,null);

        int inserted = 0;
        // add to database
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        }

        Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");
    }


}
package es.davmartor.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment  {

    private GridView mGridView;
    private MoviePosterAdapter mMoviePosterAdapter;

    public static final  String MOVIE = "es.davmartor.popular_movie.MOVIE";

    public static final int ID_TITLE = 1;
    public static final int ID_POSTER = 2;
    public static final int ID_SYNOPSIS = 3;
    public static final int ID_RATING = 4;
    public static final int ID_RELEASE_DATE = 5;

    public static final String IMAGE_POSTER_BASE_URL= "http://image.tmdb.org/t/p/w185";

    public int mPosition;
    private static final String SELECTED_KEY = "selected_position";

    public MoviePosterFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //create the adapter and fill the Gridview with the cursor
        mMoviePosterAdapter = new MoviePosterAdapter(getActivity(), null ,0);
        updateMovies();

        mGridView = (GridView) rootView.findViewById(R.id.gridview_movie_poster);
        mGridView.setAdapter(mMoviePosterAdapter);

        //Config click event for any movie
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {


                    ParcelableMovie movie = new ParcelableMovie();

                    movie.title = cursor.getString(ID_TITLE);
                    movie.posterPath = cursor.getString(ID_POSTER);
                    movie.synopsis = cursor.getString(ID_SYNOPSIS);
                    movie.rating = cursor.getString(ID_RATING);
                    movie.releaseDate = cursor.getString(ID_RELEASE_DATE);

                    Intent intent = new Intent(getActivity(), DetailActivity.class);

                    intent.putExtra(MOVIE, movie);
                    startActivity(intent);
                }

                mPosition = position;
            }
        });



        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    private AsyncTask<String, Void, Cursor> updateMovies() {
        return new FetchMovieTask(getActivity()).execute();
    }

    public void onOrderChanged() {
        updateMovies();
    }


    /**
     *  Asyncron task to load movies info
     */
    public class FetchMovieTask extends AsyncTask<String, Void, Cursor> {

        private String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private Context mContext;

        public FetchMovieTask(Context context){
            mContext = context;
        }

        @Override
        protected Cursor doInBackground(String... params) {

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
                return getMovieDataFromJson(movieJsonStr);

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
        private Cursor getMovieDataFromJson(String movieJsonStr) throws JSONException {

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

            for( int i = 0; i < moviesArray.length(); i++) {
                JSONObject movie = moviesArray.getJSONObject(i);

                int id = movie.getInt(MOVIE_ID);
                String title = movie.getString(ORIGINAL_TITLE);
                String posterPath = movie.getString(POSTER_PATH);
                String synopsis = movie.getString(SYNOPSIS);
                String rating = movie.getString(USER_RATING);
                String releaseDate = movie.getString(RELEASE_DATE);


                mc.addRow(new Object[]{id, title, posterPath, synopsis, rating, releaseDate});
            }
            return mc;

        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            mMoviePosterAdapter.swapCursor(cursor);
        }
    }
}

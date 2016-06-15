package es.davmartor.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import es.davmartor.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIE_LOADER = 0;

    private GridView mGridView;
    private MoviePosterAdapter mMoviePosterAdapter;

    public static final  String MOVIES = "es.davmartor.popular_movie.MOVIES";

    public static final int ID_TITLE = 1;
    public static final int ID_THE_MOVIE_DB_ID = 2;
    public static final int ID_POSTER = 3;
    public static final int ID_SYNOPSIS = 4;
    public static final int ID_RATING = 5;
    public static final int ID_RELEASE_DATE = 6;

    public static final String IMAGE_POSTER_BASE_URL= "http://image.tmdb.org/t/p/w185";

    public int mPosition;
    private static final String SELECTED_KEY = "selected_position";

    private static final String[] MOVIE_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_THE_MOVIE_DB_ID,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE
    };



    public interface Callback {
        public void onItemSelected(Uri dateUri);
    }

    public MoviePosterFragment() { }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        //Cursor cur = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
         //       null, null, null, null);

        //create the adapter and fill the Gridview with the cursor
        mMoviePosterAdapter = new MoviePosterAdapter(getActivity(), null ,0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridview_movie_poster);
        mGridView.setAdapter(mMoviePosterAdapter);







        //Config click event for any movie
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("POSITION", String.valueOf(position));

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieEntry.buildMovieUri(
                                     cursor.getLong(ID_THE_MOVIE_DB_ID)
                            ));
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


    private void updateMovies() {
        new FetchMovieTask(getActivity()).execute();
    }

    public void onOrderChanged() {
        updateMovies();
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri movieUri = MovieContract.MovieEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                movieUri,
                MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviePosterAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviePosterAdapter.swapCursor(null);
    }

}

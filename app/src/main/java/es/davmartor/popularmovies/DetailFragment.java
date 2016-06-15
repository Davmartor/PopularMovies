package es.davmartor.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.davmartor.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public final static String MOVIE_DETAILS = "es.davmartor.popular_movies.DetailFragment.MOVIE_DETAILS";
    private static final int DETAIL_LOADER = 0;
    private static final String[] DETAIL_COLUMNS = {
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


    public static final int COL_MOVIE_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_THE_MOVIE_DB_ID = 2;
    public static final int COL_POSTER_PATH = 3;
    public static final int COL_SYNOPSIS = 4;
    public static final int COL_RATING = 5;
    public static final int COL_RELEASE_DAT = 6;

    private Uri mUri;
    View mRootView;

    public DetailFragment() { }

    @Bind(R.id.title_movie) TextView movieTitleView;
    @Bind(R.id.poster_movie) ImageView posterView;
    @Bind(R.id.plot_movie) TextView movieSynopsisView;
    @Bind(R.id.rating_movie) TextView movieRatingView;
    @Bind(R.id.release_date_movie) TextView movieReleaseDateView;


    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if(arguments!=null){
            mUri = arguments.getParcelable(DetailFragment.MOVIE_DETAILS);
        }

        mRootView = inflater.inflate(R.layout.fragment_detail,
                container, false);

        ButterKnife.bind(this,mRootView);

        return mRootView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER,null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if( null != mUri){
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null || data.getCount() != 0) {
            data.moveToFirst();

            movieTitleView.setText(data.getString(COL_TITLE));

            String url = MoviePosterFragment.IMAGE_POSTER_BASE_URL + data.getString(COL_POSTER_PATH);
            Picasso.with(getActivity()).load(url).into(posterView);


            movieSynopsisView.setText(data.getString(COL_SYNOPSIS));
            movieRatingView.setText(data.getString(COL_RATING));
            movieReleaseDateView.setText(data.getString(COL_RELEASE_DAT));



        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

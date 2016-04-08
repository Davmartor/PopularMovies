package es.davmartor.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by david on 6/04/16.
 */
public class DetailActivity extends ActionBarActivity{


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {

            Bundle arg = new Bundle();
            arg.putParcelable(DetailFragment.MOVIE_DETAILS, getIntent().getParcelableExtra(MoviePosterFragment.MOVIE));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arg);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }




    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        public final static String MOVIE_DETAILS = "es.davmartor.popular_movies.DetailFragment.MOVIE_DETAILS";

        public DetailFragment() { }

        @Bind(R.id.title_movie) TextView movieTitleView;
        @Bind(R.id.poster_movie) ImageView posterView;
        @Bind(R.id.plot_movie) TextView movieSynopsisView;
        @Bind(R.id.rating_movie) TextView movieRatingView;
        @Bind(R.id.release_date_movie) TextView movieReleaseDateView;


        @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Bundle arguments = getArguments();
            ParcelableMovie movie = null;
            if(arguments!=null){
                movie = arguments.getParcelable(DetailFragment.MOVIE_DETAILS);
            }

            View rootView = inflater.inflate(R.layout.fragment_detail,
                    container, false);

            ButterKnife.bind(this,rootView);

            if(movie!=null) {
                movieTitleView.setText( movie.title);
                String url = MoviePosterFragment.IMAGE_POSTER_BASE_URL + movie.posterPath;
                Picasso.with(getActivity()).load(url).into(posterView);
                movieSynopsisView.setText(movie.synopsis);
                movieRatingView.setText(movie.rating);
                movieReleaseDateView.setText(movie.releaseDate);

            }
            return rootView;
        }
    }

}

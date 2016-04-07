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

/**
 * Created by david on 6/04/16.
 */
public class DetailActivity extends ActionBarActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            Bundle arguments = getArguments();
            ParcelableMovie movie = null;
            if(arguments!=null){
                movie = arguments.getParcelable(DetailFragment.MOVIE_DETAILS);
            }

            View rootView = inflater.inflate(R.layout.fragment_detail,
                    container, false);
            if(movie!=null) {


                String movieTitle = movie.title;
                TextView movieTitleView = (TextView) rootView.findViewById(R.id.title_movie);
                movieTitleView.setText(movieTitle);

                ImageView posterView = (ImageView) rootView.findViewById(R.id.poster_movie);
                String url = MoviePosterFragment.IMAGE_POSTER_BASE_URL + movie.posterPath;
                Picasso.with(getActivity()).load(url).into(posterView);

                String movieSynopsis = movie.synopsis;
                TextView movieSynopsisView = (TextView) rootView.findViewById(R.id.plot_movie);
                movieSynopsisView.setText(movieSynopsis);

                String movieRating = movie.rating;
                TextView movieRatingView = (TextView) rootView.findViewById(R.id.rating_movie);
                movieRatingView.setText(movieRating);

                String movieReleaseDate = movie.releaseDate;
                TextView movieReleaseDateView = (TextView) rootView.findViewById(R.id.release_date_movie);
                movieReleaseDateView.setText(movieReleaseDate);

            }
            return rootView;
        }
    }

}

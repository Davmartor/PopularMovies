package es.davmartor.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * Created by david on 6/04/16.
 */
public class DetailActivity extends ActionBarActivity{


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {

            Bundle arg = new Bundle();
            arg.putParcelable(DetailFragment.MOVIE_DETAILS, getIntent().getData());

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

}

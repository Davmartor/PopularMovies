package es.davmartor.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements MoviePosterFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mOrder;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //show the action bar
        getSupportActionBar().setElevation(0f);
        if(findViewById(R.id.movie_detail_container)!=null){
            mTwoPane = true;

            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container,new DetailFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String orderBy = Utility.getPreferredOrder(this);

        MoviePosterFragment mf = null;
        if(!orderBy.equals(mOrder)) {
            mf = (MoviePosterFragment) getSupportFragmentManager().findFragmentById(R.id.movie_poster_fragment);
        }
        if(null!=mf){
            mf.onOrderChanged();
        }
        mOrder = orderBy;
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane){
            Bundle args = new Bundle();
            DetailFragment fragment = new DetailFragment();

            args.putParcelable(DetailFragment.MOVIE_DETAILS,contentUri);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        }else{

            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }

    }


}

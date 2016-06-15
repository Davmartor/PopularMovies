package es.davmartor.popularMovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import es.davmartor.popularmovies.data.MovieContract;
import es.davmartor.popularmovies.data.MovieProvider;

/**
 * Created by david on 20/05/16.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The MOVIES URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIES);
    }

}

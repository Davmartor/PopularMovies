package es.davmartor.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by david on 6/04/16.
 */
public class Utility {

    public static String getPreferredOrder(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(
                context.getString(R.string.pref_order_key),
                context.getString(R.string.pref_order_popular));
    }
}

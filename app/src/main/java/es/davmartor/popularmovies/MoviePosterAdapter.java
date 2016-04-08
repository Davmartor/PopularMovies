package es.davmartor.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by david on 7/03/16.
 */
public class MoviePosterAdapter extends CursorAdapter {


    public MoviePosterAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_movie_poster,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String url = MoviePosterFragment.IMAGE_POSTER_BASE_URL + cursor.getString(MoviePosterFragment.ID_POSTER);

        Picasso.with(context).load(url).into(viewHolder.posterView);
    }

    public static class ViewHolder{
        @Bind(R.id.movie_poster_image)
        ImageView posterView;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);

        }

    }



}

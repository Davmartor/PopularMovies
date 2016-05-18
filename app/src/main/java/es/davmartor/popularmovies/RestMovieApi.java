package es.davmartor.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by david on 4/05/16.
 */
public interface RestMovieApi {
    @GET("3/movie/{order}")
    Call<ResponseJSON> getMovie(@Path("order") String order, @Query("api_key") String  keyApi);


}

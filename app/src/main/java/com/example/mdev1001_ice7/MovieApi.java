package com.example.mdev1001_ice7;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MovieApi {
    @GET("api/list")
    Call<List<Movie>> fetchMovies();

    @POST("add")
    Call<Void> addMovie(@Body Movie movie);

    @PUT("update/{id}")
    Call<Void> updateMovie(@Path("id") String id, @Body Movie movie);

    @DELETE("delete/{id}")
    Call<Void> deleteMovie(@Path("id") String id);
}

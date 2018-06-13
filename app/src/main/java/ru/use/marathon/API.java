package ru.use.marathon;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ilyas on 09-Jun-18.
 */

public interface API {


    @GET("api.php")
    Call<JsonObject> sign_up(
            @Query("v") int v,
            @Query("method") String method,
            @Query("type") int type,
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password
    );


    @GET("api.php")
    Call<JsonObject> sign_in(
            @Query("v") int v,
            @Query("method") String method,
            @Query("type") int type,
            @Query("email") String email,
            @Query("password") String password
    );

    @GET("api.php")
    Call<JsonObject> get_collections(
            @Query("v") int v,
            @Query("method") String method
    );



    @GET("api.php")
    Call<JsonObject> get_collection(
            @Query("v") int v,
            @Query("method") String method,
            @Query("qc_number") int qc_number

    );






}

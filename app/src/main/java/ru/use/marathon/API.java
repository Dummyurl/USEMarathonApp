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
    Call<JsonObject> get_topics(
            @Query("v") int v,
            @Query("method") String method
    );



    @GET("api.php")
    Call<JsonObject> get_solved_tests_by_topic(
            @Query("v") int v,
            @Query("method") String method,
            @Query("user_id") int userid
    );

    @GET("api.php")
    Call<JsonObject> get_collection_by_topics(
            @Query("v") int v,
            @Query("method") String method,
            @Query("topic_id") int topic_ids
    );

    @GET("api.php")
    Call<JsonObject> get_collection(
            @Query("v") int v,
            @Query("method") String method,
            @Query("qc_number") int qc_number

    );




    @GET("api.php")
    Call<JsonObject> get_theory_topics(
            @Query("v") int v,
            @Query("method") String method
    );


    @GET("api.php")
    Call<JsonObject> get_all_feed(
            @Query("v") int v,
            @Query("method") String method,
            @Query("type") int type
    );


    @GET("api.php")
    Call<JsonObject> getFeedTextById(
            @Query("v") int v,
            @Query("method") String method,
            @Query("id") int id
    );


    @GET("api.php")
    Call<JsonObject> updateRegToken(
            @Query("v") int v,
            @Query("method") String method,
            @Query("reg_token") String token,
            @Query("uid") int user_id,
            @Query("utype") int user_type
    );



    @GET("api.php")
    Call<JsonObject> getChatRooms(
            @Query("v") int v,
            @Query("method") String method,
            @Query("uid") int user_id,
            @Query("utype") int user_type
    );








}

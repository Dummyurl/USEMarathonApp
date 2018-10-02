package ru.use.marathon.utils.typeform;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by ilyas on 01-Oct-18.
 */

public interface TypeformAPI {

    @GET("forms")
    Call<JsonObject> getForms();

}

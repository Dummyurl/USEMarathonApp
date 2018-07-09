package ru.use.marathon.models;

import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 03-Jul-18.
 */

public class FeedText {

    JsonObject js;

    public FeedText(Response<JsonObject> response) {
        js = response.body();
    }
    public String getText(){
        return js.get("text").getAsString();
    }
}

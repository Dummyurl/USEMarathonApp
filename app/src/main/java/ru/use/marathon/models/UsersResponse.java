package ru.use.marathon.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 10-Jul-18.
 */

public class UsersResponse
{

    JsonObject js;

    public UsersResponse(Response<JsonObject> response) {
        js = response.body();
    }

    public JsonArray getData(){
        return js.get("users").getAsJsonArray();
    }

    public int size(){
        return getData().size();
    }

    public String getId(int i){
        return getData().get(i).getAsJsonObject().get("id").getAsString();
    }

    public String getName(int i ){
        return getData().get(i).getAsJsonObject().get("name").getAsString();
    }

    public String getImage(int i){
        return getData().get(i).getAsJsonObject().get("image").getAsString();
    }
}

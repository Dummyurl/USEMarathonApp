package ru.use.marathon.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 02-Jul-18.
 */

public class Feed {

    JsonObject js;

    public Feed(Response<JsonObject> response) {
        js = response.body();
    }

    public JsonArray getData(){
        return js.get("feed").getAsJsonArray();
    }

    public int size(){
        return getData().size();
    }

    public int getId(int i){
        return getData().get(i).getAsJsonObject().get("id").getAsInt();
    }

    public String getTitle(int i){
        return getData().get(i).getAsJsonObject().get("title").getAsString();
    }

    public String getImage(int i){
        return getData().get(i).getAsJsonObject().get("image").getAsString();
    }

    public String getContent(int i){
        return getData().get(i).getAsJsonObject().get("content").getAsString();
    }

//    public String getText(int i){
//        return getData().get(i).getAsJsonObject().get("text").getAsString();
//    }

    public String getCreatedAt(int i){
        return getData().get(i).getAsJsonObject().get("created_at").getAsString();
    }
}

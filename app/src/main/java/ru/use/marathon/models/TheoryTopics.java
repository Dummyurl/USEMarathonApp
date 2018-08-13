package ru.use.marathon.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 14-Jun-18.
 */

public class TheoryTopics {

    JsonObject js;

    public TheoryTopics(Response<JsonObject> response) {
        js = response.body();
    }

    public JsonArray getData(){
        return js.get("theory").getAsJsonArray();
    }

    public int size(){
        return getData().size();
    }

    public int getId(int i){
        return getData().get(i).getAsJsonObject().get("id").getAsInt();
    }

    public int getIcon(int i){
        return getData().get(i).getAsJsonObject().get("icon").getAsInt();
    }

    public String getTitle(int i){
        return getData().get(i).getAsJsonObject().get("title").getAsString();
    }


    public String getContent(int i){
        return getData().get(i).getAsJsonObject().get("content").getAsString();
    }

}

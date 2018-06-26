package ru.use.marathon.models.topics;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 20-Jun-18.
 */

public class Topics {

    JsonObject jsonObject;

    public Topics(Response<JsonObject> response) {
        jsonObject = response.body();
    }

    public JsonArray getData(){
        return jsonObject.get("topics").getAsJsonArray();
    }

    public int size(){
        return getData().size();
    }
    public int getTestsNumber(int i){
        return getData().get(i).getAsJsonObject().get("tests_number").getAsInt();
    }

    public String getContent(int i){
        return getData().get(i).getAsJsonObject().get("content").getAsString();
    }

    public int getID(int i){
        return getData().get(i).getAsJsonObject().get("id").getAsInt();
    }
}

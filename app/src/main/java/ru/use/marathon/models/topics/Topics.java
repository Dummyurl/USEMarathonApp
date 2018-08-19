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

    public boolean success(){
        return (jsonObject.has("success") && (jsonObject.get("success").getAsInt() > 0));
    }

    public JsonArray getData(){
        return jsonObject.get("topics").getAsJsonArray();
    }

    public int size(){
        return getData().size();
    }

    public int getAmount(int i){
        return getData().get(i).getAsJsonObject().get("amount").getAsInt();
    }

    public int getSolved(int i){
        return getData().get(i).getAsJsonObject().get("solved").getAsInt();
    }

    public String getTitle(int i){
        return getData().get(i).getAsJsonObject().get("title").getAsString();
    }


    public String getContent(int i){
        return getData().get(i).getAsJsonObject().get("content").getAsString();
    }


    public int getID(int i){
        return getData().get(i).getAsJsonObject().get("id").getAsInt();
    }
}

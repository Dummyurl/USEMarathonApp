package ru.use.marathon.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 10-Jun-18.
 */

public class Collections {

    JsonObject res;

    public Collections(Response<JsonObject> response) {
        res = response.body();
    }


    public JsonArray getData() {
        return res.get("collections").getAsJsonArray();
    }

    public int size() {
        return getData().size();
    }

    public int collection_number(int pos){
        return getData().get(pos).getAsJsonObject().get("collection_number").getAsInt();
    }

}

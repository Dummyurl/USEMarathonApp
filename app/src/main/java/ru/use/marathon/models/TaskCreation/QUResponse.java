package ru.use.marathon.models.TaskCreation;

import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 14-Jul-18.
 */

public class QUResponse {

    JsonObject js;

    public QUResponse(Response<JsonObject> response) {
        js = response.body();
    }

    public int qu_id(){
        return js.get("id").getAsInt();
    }
}

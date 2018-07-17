package ru.use.marathon.models;

import com.google.gson.JsonObject;

import java.util.NoSuchElementException;

import retrofit2.Response;

/**
 * Created by ilyas on 09-Jun-18.
 */

public class Success {

    static int success;
    static JsonObject jsonObject;

    public Success(Response<JsonObject> response) {
        jsonObject = response.body();
    }

    public static boolean success(){
        if(jsonObject.has("success")){
            success = jsonObject.get("success").getAsInt();
        }else{
            return true;
        }

        return success == 1;
    }
}

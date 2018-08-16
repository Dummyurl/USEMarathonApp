package ru.use.marathon.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

public class CityShow {
    static int success;
    static JsonObject jsonObject;

    public CityShow (Response<JsonObject> response) {
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
    public JsonArray getData() {
        return jsonObject.get("citys").getAsJsonArray();
    }

    public int id (int pos){
        return getData().get(pos).getAsJsonObject().get("id").getAsInt();
    }

    public String name (int pos){
        return getData().get(pos).getAsJsonObject().get("name").getAsString();
    }


}


package ru.use.marathon.models;
/**
 * Created by Marat on 24-July-18.
 */
import com.google.gson.JsonObject;

import retrofit2.Response;

public class RestoreCode {
    static int success;
    static JsonObject jsonObject;

    public RestoreCode (Response<JsonObject> response) {
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
    public static int code(){
        return jsonObject.get("code").getAsInt();
    }

}

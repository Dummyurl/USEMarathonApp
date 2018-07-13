package ru.use.marathon.models.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 13-Jul-18.
 */

public class ChatMembers  {

    JsonObject js;

    public ChatMembers(Response<JsonObject>response){
        js  = response.body();
    }

    public JsonArray getData(){
        return js.get("members").getAsJsonArray();
    }

    public int size(){
        return getData().size();
    }

    public int getId(int i){
        return getData().get(i).getAsJsonObject().get("user_id").getAsInt();
    }
    public int getUserType(int i){
        return getData().get(i).getAsJsonObject().get("user_type").getAsInt();
    }
    public String getName(int i){
        return getData().get(i).getAsJsonObject().get("name").getAsString();
    }
    public String getImage(int i){
        return getData().get(i).getAsJsonObject().get("image").getAsString();
    }

}

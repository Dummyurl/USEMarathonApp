package ru.use.marathon.models.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 08-Jul-18.
 *
 * Chat rooms from server
 * ChatRoom class is defined to make chat rooms update itself
 */

public class Rooms {

    JsonObject js;

    public Rooms(Response<JsonObject> response) {
        js = response.body();
    }

    public JsonArray getData() {
        return js.get("rooms").getAsJsonArray();
    }

    public int size(){
        return getData().size();
    }

    public int getId(int i) {
        return getData().get(i).getAsJsonObject().get("id").getAsInt();
    }

    public int getChatId(int i){
        return getData().get(i).getAsJsonObject().get("chat_id").getAsInt();
    }

    public String getTitle(int i){
        return getData().get(i).getAsJsonObject().get("title").getAsString();
    }

    public String getName(int i){
        return getData().get(i).getAsJsonObject().get("user_name").getAsString();
    }

    public String getLastMessage(int i){
        return getData().get(i).getAsJsonObject().get("last_message").getAsString();
    }

    public String getTimestamp(int i){
        return getData().get(i).getAsJsonObject().get("timestamp").getAsString();
    }



}

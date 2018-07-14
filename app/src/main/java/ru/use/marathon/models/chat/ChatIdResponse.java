package ru.use.marathon.models.chat;

import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 14-Jul-18.
 */

public class ChatIdResponse {

    JsonObject jsonObject;

    public ChatIdResponse(Response<JsonObject> response) {
        jsonObject = response.body();
    }

    public String getChatId(){
        return jsonObject.get("chat_room_id").getAsString();
    }
}

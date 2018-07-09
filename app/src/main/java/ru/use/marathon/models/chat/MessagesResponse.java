package ru.use.marathon.models.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 09-Jul-18.
 */

public class MessagesResponse {

    JsonObject js;

    public MessagesResponse(Response<JsonObject> response) {
        js = response.body();

    }

    public JsonArray getData() {
        return js.get("msgs").getAsJsonArray();
    }

    public String getText(int i) {
        return getData().get(i).getAsJsonObject().get("txt").getAsString();
    }

    public String getChatId(int i) {
        return getData().get(i).getAsJsonObject().get("chat_id").getAsString();
    }

    public String getUserId(int i) {
        return getData().get(i).getAsJsonObject().get("uid").getAsString();
    }

    public int getUserType(int i) {
        return getData().get(i).getAsJsonObject().get("ut").getAsInt();
    }

    public String getUserName(int i) {
        return getData().get(i).getAsJsonObject().get("name").getAsString();
    }

    public String getTimestamp(int i) {
        return getData().get(i).getAsJsonObject().get("tmstmp").getAsString();
    }
}

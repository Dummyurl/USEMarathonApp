package ru.use.marathon.models.typeform;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 01-Oct-18.
 */

public class Forms {

    JsonObject js;

    public Forms(Response<JsonObject> response) {
        js = response.body();
    }

    public int getTotalItems(){
        return js.get("total_items").getAsInt();
    }

    public int getPageCount(){
        return js.get("page_count").getAsInt();
    }

    public JsonArray itemsData(){
        return js.get("items").getAsJsonArray();
    }

    public int size(){
        return itemsData().size();
    }
    public String itemId(int i){
        return itemsData().get(i).getAsJsonObject().get("id").getAsString();
    }

    public String itemTitle(int i){
        return itemsData().get(i).getAsJsonObject().get("title").getAsString();
    }

    public String itemLastUpdated(int i){
        return itemsData().get(i).getAsJsonObject().get("last_updated_at").getAsString();
    }

    public boolean itemSettingsPublic(int i){
        return itemsData()
                .get(i)
                .getAsJsonObject()
                .get("settings")
                .getAsJsonObject()
                .get("is_public")
                .getAsBoolean();
    }

    public boolean itemSettingsTrial(int i){
        return itemsData()
                .get(i)
                .getAsJsonObject()
                .get("settings")
                .getAsJsonObject()
                .get("is_public")
                .getAsBoolean();
    }
}

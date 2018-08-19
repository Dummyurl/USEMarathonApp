package ru.use.marathon.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 10-Jun-18.
 */

public class Collections {

    JsonObject res;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Collections(Response<JsonObject> response) {
        res = response.body();
    }

    public Collections(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("collections",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public boolean success(){
        return (res.has("success") && (res.get("success").getAsInt() > 0));
    }

    public void saveCollection(Collection collection){
        Gson gson = new Gson();
        String s = gson.toJson(collection);
        editor.putString("collection",s);
        editor.commit();
    }

    public Collection getCollection(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("collection", "");
        return  gson.fromJson(json, Collection.class);
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

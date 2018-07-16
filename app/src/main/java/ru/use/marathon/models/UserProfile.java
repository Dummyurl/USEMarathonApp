package ru.use.marathon.models;

import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 16-Jul-18.
 */

public class UserProfile  {

    JsonObject js;

    public UserProfile(Response<JsonObject> response) {
        js = response.body();
    }

    public int id (){
        return js.get("id").getAsInt();
    }

    public String image(){
        return js.get("image").getAsString();
    }
    public String name(){
        return js.get("name").getAsString();
    }
    public String subject(){
        return js.get("subject").getAsString();
    }
    public int teacher_id(){
        return js.get("teacher_id").getAsInt();
    }
    public String teacher_name(){
        return js.get("teacher_name").getAsString();
    }
}

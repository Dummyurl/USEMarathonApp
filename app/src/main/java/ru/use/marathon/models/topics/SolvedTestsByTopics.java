package ru.use.marathon.models.topics;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 20-Jun-18.
 */

public class SolvedTestsByTopics  {

    JsonObject jsonObject;

    public SolvedTestsByTopics(Response<JsonObject> response) {
        jsonObject = response.body();
    }

    public boolean success(){
        return (jsonObject.has("success") && (jsonObject.get("success").getAsInt() > 0));
    }

    public JsonArray getData(){
        return jsonObject.get("solved").getAsJsonArray();
    }

    public int size(){
        return getData().size();
    }


    public int getTopicID(int i){
        return getData().get(i).getAsJsonObject().get("topic_id").getAsInt();
    }
    public int getSolvedTestsNumber(int i){
        return getData().get(i).getAsJsonObject().get("tests_number").getAsInt();
    }

}

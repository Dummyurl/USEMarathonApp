package ru.use.marathon.models;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by ilyas on 12-Jun-18.
 */

public class Collection  {

    private  JsonObject object;

    public Collection(Response<JsonObject> response){
        object = response.body();
    }

    private JsonArray getCollectionArray(){
        return object.get("collection").getAsJsonArray();
    }

    public int size(){
        return getCollectionArray().size();
    }

    public int getId(int i) {
        return getCollectionArray().get(i).getAsJsonObject().get("id").getAsInt();
    }

    public String getSubject(int i) {
        return getCollectionArray().get(i).getAsJsonObject().get("subject").getAsString();
    }

    public String getTopic(int i) {
        return getCollectionArray().get(i).getAsJsonObject().get("topic").getAsString();
    }

    public int getTaskNumber(int i) {
        return getCollectionArray().get(i).getAsJsonObject().get("task_num").getAsInt();
    }

    public String getContent(int i) {
        return getCollectionArray().get(i).getAsJsonObject().get("content").getAsString();
    }

    public int getAnswerType(int i){
        return getCollectionArray().get(i).getAsJsonObject().get("answer_type").getAsInt();
    }

    public List<String> getAnswers(int i){

        int answers_size = getCollectionArray().get(i).getAsJsonObject().get("answers").getAsJsonArray().size();
        List<String> answers = new ArrayList<>();
        try{
            for (int j = 0; j < answers_size; j++){
                answers.add(getCollectionArray()
                        .get(i)
                        .getAsJsonObject()
                        .get("answers")
                        .getAsJsonArray()
                        .get(j)
                        .getAsJsonObject()
                        .get("answer")
                        .getAsString());
            }
        }catch (Exception e){
            answers.add("Error.");
        }



        return answers;
    }

    public int getAnswersSize(int i){
        return getCollectionArray().get(i).getAsJsonObject().get("answers").getAsJsonArray().size();
    }

    public List<String> getRightAnswers(int i){
        int right_answers_size = getCollectionArray().get(i).getAsJsonObject().get("right_answers").getAsJsonArray().size();
        List<String> answers = new ArrayList<>();
        try{
            for (int j = 0; j < right_answers_size; j++){
                answers.add(getCollectionArray()
                        .get(i)
                        .getAsJsonObject()
                        .get("right_answers")
                        .getAsJsonArray()
                        .get(j)
                        .getAsJsonObject()
                        .get("order")
                        .getAsString());
            }
        }catch (Exception e){
            answers.add("-");
        }
        return answers;
    }



    public String getCreatedAt(int i) {
        return getCollectionArray().get(i).getAsJsonObject().get("created_at").getAsString();
    }
}

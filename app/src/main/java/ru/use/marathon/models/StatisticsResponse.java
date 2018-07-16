package ru.use.marathon.models;

import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by ilyas on 16-Jul-18.
 */

public class StatisticsResponse {

    JsonObject js;

    public StatisticsResponse(Response<JsonObject> response) {
        js = response.body();
    }

    public double totalTests(){
        return js.get("total").getAsInt();
    }

    public double averageTime(){
        return js.get("aver_time").getAsDouble();
    }

    public double wrongPercent(){
        return js.get("wrong_percent").getAsDouble();

    }

    public int right(){
        return js.get("right_answers").getAsInt();
    }
}

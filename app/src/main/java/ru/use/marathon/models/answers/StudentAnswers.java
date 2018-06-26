package ru.use.marathon.models.answers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import ru.use.marathon.utils.ObjectSerializer;

/**
 * Created by ilyas on 21-Jun-18.
 */

public class StudentAnswers {

    Context context;
    SharedPreferences shPrefs;
    SharedPreferences.Editor editor;
    public static final String KEY_TASK_ID = "task_id";
    public static final String KEY_ANSWERS_AMOUNT = "tasks_amount";

    public StudentAnswers(Context context) {
        this.context = context;
        shPrefs  = context.getSharedPreferences("student_answers",Context.MODE_PRIVATE);
        editor = shPrefs.edit();
    }


    public void saveAbstractAnswer(List<AbstractAnswer> abstractAnswers){
    }

    public List<AbstractAnswer> getAbstractAnswers(){
        Gson gson = new Gson();
        String js = shPrefs.getString("abstract_answers","");
        Type type = new TypeToken<List<AbstractAnswer>>(){}.getType();
        return gson.fromJson(js,type);
    }


}

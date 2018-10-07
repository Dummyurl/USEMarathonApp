package ru.use.marathon.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.HashSet;

import retrofit2.Response;

/**
 * Created by ilyas on 09-Jun-18.
 */

public class Student {

    public static final String PREF_NAME = "student";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME =                   "name";
    public static final String KEY_SURNAME =                   "surname";
    public static final String KEY_VK_ID =                   "vk_id";
    public static final String KEY_EMAIL =                  "email";
    public static final String KEY_IMAGE =                  "image";
    public static final String KEY_TEACHER_ID =             "teacher_id";
    public static final String KEY_TESTS_COUNTER =          "tests_counter";
    public static final String KEY_TESTS_TIME =             "tests_time";
    public static final String KEY_ANSWERS_COUNTER =        "answers_counter";
    public static final String KEY_WRONG_ANSWERS_COUNTER =  "wrong_answers_counter";
    public static final String IS_LOGGED_IN = "isLoggedIn";

    int id;
    String name;
    String email;
    String token;
    int teacher_id;

    private Context context;
    private JsonObject jsonObject;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Student(Context context, Response<JsonObject> response) {
        this.context = context;
        jsonObject = response.body();
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Student(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getName(){
        return jsonObject.get("name").getAsString();
    }

    public int getID(){
        return jsonObject.get("id").getAsInt();
    }

    public String getImage(){
        return jsonObject.get("image").getAsString();
    }

    public int getTeacher_id(){return jsonObject.get("teacher_id").getAsInt();}

    public int getTests_counter(){
        return jsonObject.get(KEY_TESTS_COUNTER).getAsInt();
    }

    public double getTest_time(){
        return jsonObject.get(KEY_TESTS_TIME).getAsDouble();
    }

    public int getAnswersCounter(){
        return jsonObject.get(KEY_ANSWERS_COUNTER).getAsInt();
    }

    public int getAnswersWrongCounter(){
        return jsonObject.get(KEY_WRONG_ANSWERS_COUNTER).getAsInt();
    }

    public int UID(){
        return sharedPreferences.getInt(KEY_ID,-1);
    }
    public String NAME(){
        return sharedPreferences.getString(KEY_NAME,"");
    }

    public void getVkSessionData(int id,String name,String surname){
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_SURNAME,surname);
        editor.putInt(KEY_VK_ID,id);
        editor.commit();
    }

    public void createSession(int id, String name,String email,String image,int teacher_id, int tests_counter, double tests_time,int answers_counter,int answers_wrong_counter) {
        editor.putInt(KEY_ID,id);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_IMAGE,image);
        editor.putInt(KEY_TEACHER_ID,teacher_id);
        editor.putInt(KEY_TESTS_COUNTER,tests_counter);
        editor.putFloat(KEY_TESTS_TIME, (float) tests_time);
        editor.putInt(KEY_ANSWERS_COUNTER,answers_counter);
        editor.putInt(KEY_WRONG_ANSWERS_COUNTER,answers_wrong_counter);
        editor.putBoolean(IS_LOGGED_IN,true);
        editor.commit();
    }

    public void setStatistics(int tests_counter,double tests_time,int answers_counter,int answers_wrong_counter){
        editor.putInt(KEY_TESTS_COUNTER,tests_counter);
        editor.putFloat(KEY_TESTS_TIME, (float) tests_time);
        editor.putInt(KEY_ANSWERS_COUNTER,answers_counter);
        editor.putInt(KEY_WRONG_ANSWERS_COUNTER,answers_wrong_counter);
        editor.commit();
    }

    public void setSubject(int subject){
        editor.putInt("subject",subject);editor.commit();
    }

    public int getSubject(){
        return sharedPreferences.getInt("subject",-1);
    }

    public HashMap<String,String> getStatistics(){
        HashMap<String,String> data = new HashMap<>();
        data.put(KEY_TESTS_COUNTER, String.valueOf(sharedPreferences.getInt(KEY_TESTS_COUNTER,0)));
        data.put(KEY_TESTS_TIME, String.valueOf(sharedPreferences.getFloat(KEY_TESTS_TIME,0)));
        data.put(KEY_ANSWERS_COUNTER, String.valueOf(sharedPreferences.getInt(KEY_ANSWERS_COUNTER,0)));
        data.put(KEY_WRONG_ANSWERS_COUNTER, String.valueOf(sharedPreferences.getInt(KEY_WRONG_ANSWERS_COUNTER,0)));
        return data;
    }

    public void deleteStat(){
        editor.remove(KEY_TESTS_COUNTER);
        editor.remove(KEY_WRONG_ANSWERS_COUNTER);
        editor.remove(KEY_TESTS_TIME);
        editor.remove(KEY_ANSWERS_COUNTER);
        editor.commit();

    }
    public void createFirstSession(int id,String name, String email){
        editor.putInt(KEY_ID,id);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.putBoolean(IS_LOGGED_IN,true);
        editor.commit();
    }

    public HashMap<String,String> getData(){
        HashMap<String,String> data = new HashMap<>();
        data.put(KEY_ID,String.valueOf(sharedPreferences.getInt(KEY_ID,0)));
        data.put(KEY_NAME,sharedPreferences.getString(KEY_NAME,null));
        data.put(KEY_SURNAME,sharedPreferences.getString(KEY_SURNAME,null));
        data.put(KEY_EMAIL,sharedPreferences.getString(KEY_EMAIL,null));
        data.put(KEY_IMAGE,sharedPreferences.getString(KEY_IMAGE,null));
        data.put(KEY_TEACHER_ID, String.valueOf(sharedPreferences.getInt(KEY_TEACHER_ID,0)));
        data.put(KEY_TESTS_COUNTER, String.valueOf(sharedPreferences.getInt(KEY_TESTS_COUNTER,0)));
        data.put(KEY_TESTS_TIME, String.valueOf(sharedPreferences.getFloat(KEY_TESTS_TIME,0)));
        data.put(KEY_ANSWERS_COUNTER, String.valueOf(sharedPreferences.getInt(KEY_ANSWERS_COUNTER,0)));
        data.put(KEY_WRONG_ANSWERS_COUNTER, String.valueOf(sharedPreferences.getInt(KEY_WRONG_ANSWERS_COUNTER,0)));
        data.put(KEY_VK_ID,String.valueOf(sharedPreferences.getInt(KEY_VK_ID,0)));
        return data;
    }

    public void login(boolean isLoggedIn){
        editor.putBoolean(IS_LOGGED_IN,isLoggedIn);
        editor.commit();
    }



    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }






}

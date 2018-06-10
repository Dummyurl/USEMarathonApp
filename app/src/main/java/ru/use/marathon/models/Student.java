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
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
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

    public void createSession(int id, String name,String email) {

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
        data.put(KEY_EMAIL,sharedPreferences.getString(KEY_EMAIL,null));
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
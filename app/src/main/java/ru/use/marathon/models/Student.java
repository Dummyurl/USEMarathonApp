package ru.use.marathon.models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ilyas on 09-Jun-18.
 */

public class Student {

    public static final String PREF_NAME = "student";
    public static final String IS_LOGGED_IN = "isLoggedIn";

    int id;
    String name;
    String email;
    String token;
    int teacher_id;

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Student(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void login(boolean isLoggedIn){
        editor.putBoolean(IS_LOGGED_IN,isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }






}

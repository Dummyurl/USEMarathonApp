package ru.use.marathon.models.answers;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Created by ilyas on 21-Jun-18.
 */

public class AbstractAnswer  implements Serializable {

    Context context;
    SharedPreferences shPrefs;
    SharedPreferences.Editor editor;

    public static final String KEY_TAG = "answer_tag";
    public static final String KEY_RIGHT_ANSWER = "right_answer";

    public AbstractAnswer(Context context) {
        this.context = context;
        shPrefs = context.getSharedPreferences("abstract_answer",Context.MODE_PRIVATE);
        editor = shPrefs.edit();
    }

    public String getTag(){
        return shPrefs.getString(KEY_TAG,"");
    }

    public boolean isRight(){
        return shPrefs.getInt(KEY_RIGHT_ANSWER,-1) == 1;
    }

    public void saveRadioBtn(int[] answers){

        int ra = 0;
        for (int answer : answers) if (answer != 1) ra = 1;

        editor.putInt(KEY_RIGHT_ANSWER,ra);

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < answers.length; i++) {
            str.append(answers[i]).append(",");
        }
        editor.putString(KEY_TAG,"radio");
        editor.putString("rbtn_answers", str.toString());
        editor.commit();
    }


    public int[] getRadioBtn(){

        String savedString = shPrefs.getString("rbtn_answers", "");
        StringTokenizer st = new StringTokenizer(savedString, ",");
        int[] savedList = new int[10];
        for (int i = 0; i < 10; i++) {
            savedList[i] = Integer.parseInt(st.nextToken());
        }
        return savedList;
    }

    public void clearData(){
        editor.remove(KEY_TAG);
        editor.remove(KEY_RIGHT_ANSWER);
        editor.commit();
    }


    public void saveET(String right_answer,String user_answer){
        String _ra = right_answer.toLowerCase();
        String _ua = user_answer.toLowerCase();
        int ra = (_ra.equals(_ua) ? 1 : 0);
        editor.putInt(KEY_RIGHT_ANSWER,ra);

        editor.putString(KEY_TAG,"edit");
        editor.putString("et_right_answer",_ra);
        editor.putString("et_user_answer",_ua);
        editor.commit();
    }


    public String[] getET(){
        String[] data = new String[2];
        data[0] = shPrefs.getString("et_right_answer","");
        data[1] = shPrefs.getString("et_user_answer","");
        return data;
    }


}

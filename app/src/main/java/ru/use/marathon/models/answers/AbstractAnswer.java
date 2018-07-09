package ru.use.marathon.models.answers;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * Created by ilyas on 21-Jun-18.
 */

public class AbstractAnswer implements Serializable {

    Context context;
    SharedPreferences shPrefs;
    SharedPreferences.Editor editor;

    public static final String KEY_TAG = "answer_tag";
    public static final String KEY_PAGE = "page";
    public static final String TAG_CHECKBOX = "check";
    public static final String TAG_RADIO_BUTTON = "radio";
    public static final String TAG_TEXT = "edit";
    public static final String KEY_RIGHT_ANSWER = "right_answer";

    public AbstractAnswer(Context context) {
        this.context = context;
        shPrefs = context.getSharedPreferences("abstract_answer", Context.MODE_PRIVATE);
        editor = shPrefs.edit();
    }

    public String getTag() {
        return shPrefs.getString(KEY_TAG, "");
    }

    public boolean isRight() {
        return shPrefs.getInt(KEY_RIGHT_ANSWER, -1) == 1;
    }

    public void saveRadioBtn(int[] answers) {

        int ra = 0;
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i < answers.length; i++) {
            if (answers[i] == 1) {
                a.add(1);
            }
        }

        ra = (a.size() > 0 ? 0 : 1);

        editor.putInt(KEY_RIGHT_ANSWER, ra);

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < answers.length; i++) {
            str.append(answers[i]).append(",");
        }
        editor.putString(KEY_TAG, TAG_RADIO_BUTTON);
        editor.putString("rbtn_answers", str.toString());
        editor.commit();
    }


    public int[] getRadioBtn() {

        String savedString = shPrefs.getString("rbtn_answers", "");
        StringTokenizer st = new StringTokenizer(savedString, ",");
        int[] savedList = new int[10];
        for (int i = 0; i < 10; i++) savedList[i] = 10;

        for (int i = 0; i < 10; i++) {
            savedList[i] = Integer.parseInt(st.nextToken());
        }
        return savedList;
    }

    public void clearData() {
        editor.remove(KEY_TAG);
        editor.remove(KEY_RIGHT_ANSWER);
        editor.commit();
    }

    public void savePage(int page){
        editor.putInt(KEY_PAGE,page);
        editor.commit();
    }

    public void saveET(String right_answer, String user_answer) {
        editor.putString(KEY_TAG, TAG_TEXT);
        String _ra = right_answer.toLowerCase();
        String _ua = user_answer.toLowerCase();
        int ra = (_ra.equals(_ua) ? 1 : 0);
        editor.putInt(KEY_RIGHT_ANSWER, ra);

        editor.putString("et_right_answer", _ra);
        editor.putString("et_user_answer", _ua);
        editor.commit();
    }


    public String[] getET() {
        String[] data = new String[2];
        data[0] = shPrefs.getString("et_right_answer", "");
        data[1] = shPrefs.getString("et_user_answer", "");
        return data;
    }


    public void saveCB(List<Integer> right_answers, List<Integer>  answers) {
        editor.putString(KEY_TAG, TAG_CHECKBOX);

        if(right_answers.size() == answers.size()){
            int c = 0;
            for (int i = 0; i < answers.size(); i++) {
                if(Objects.equals(right_answers.get(i), answers.get(i))) c++;
                editor.putInt(KEY_RIGHT_ANSWER, c == answers.size() ? 1 : 0);
            }
        }else{
            editor.putInt(KEY_RIGHT_ANSWER,0);
        }

        StringBuilder str = new StringBuilder();
        for (int answer : answers) str.append(answer).append(",");
        editor.putString("cb_answers", str.toString());
        editor.commit();

    }

    public int[] getCB(){

        String savedString = shPrefs.getString("cb_answers", "");
        StringTokenizer st = new StringTokenizer(savedString, ",");
        int[] savedList = new int[10];
        for (int i = 0; i < 10; i++) savedList[i] = 10;
        for (int i = 0; i < 10; i++) {
            savedList[i] = Integer.parseInt(st.nextToken());
        }
        return savedList;
    }

}

package ru.use.marathon.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;
import ru.use.marathon.utils.InternetConnectionListener;

/**
 * Created by ilyas on 04-Aug-18.
 */

public class AbstractActivity extends AppCompatActivity implements InternetConnectionListener {


    public static final int STUDENT = 0;
    public static final int TEACHER = 1;

    Student student;
    Teacher teacher;

    public HashMap<String, String> user_data;
    ProgressDialog dialog;
    AlertDialog.Builder builder;
    AlertDialog adialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppController) getApplication()).setInternetConnectionListener(this);

        student = new Student(this);
        teacher = new Teacher(this);

        if (userType() == STUDENT) user_data = student.getData();
        else if (userType() == TEACHER) user_data = teacher.getData();

    }
    @Override
    public void onPause() {
        super.onPause();
        ((AppController) getApplication()).removeInternetConnectionListener();
    }


    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean success(Response<JsonObject> response){
        JsonObject js = response.body();
        return (js != null) && js.has("success") && (js.get("success").getAsInt() > 0);
    }

    public int userType() {
        if (student.isLoggedIn() && !teacher.isLoggedIn()) {
            return 0;
        } else if (!student.isLoggedIn() && teacher.isLoggedIn()) {
            return 1;
        } else {
            return -1;
        }
    }

    public int subject() {
        if(student.getSubject() == -1){
            return 1;
        }else{
            return student.getSubject();
        }
    }

    public void setSubject(int subject) {
        student.setSubject(subject);
    }

    public String name() {
        int ut = userType();
        if (ut == STUDENT) {
            return user_data.get(student.KEY_NAME);
        } else if (ut == TEACHER) {
            return user_data.get(teacher.KEY_NAME);
        }
        return "null";
    }


    public String email() {
        int ut = userType();
        if (ut == STUDENT) {
            return user_data.get(student.KEY_EMAIL);
        } else if (ut == TEACHER) {
            return user_data.get(teacher.KEY_EMAIL);
        }
        return "null";
    }

    public void showInfoDialog(String title, String message) {

        if (adialog == null) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            adialog = builder.create();
            adialog.show();
        } else {
            builder = null;
            adialog = null;
        }
    }

    public void showLoadDialog(Context c, String title, String message) {
        dialog = new ProgressDialog(c);
        dialog.setTitle(title);
        dialog.setIndeterminate(true);
        dialog.setMessage(message);
        dialog.setProgress(0);
        dialog.show();
    }

    public void closeLoadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onInternetUnavailable() {
        showInfoDialog("Подключение к сети","Нет подлючения к интернету. Проверьте подключение или повторите позднее");
    }

    String ellipsize(String input, int maxLength) {
        if (input == null || input.length() < maxLength) {
            return input;
        }
        return input.substring(0, maxLength) + "...";
    }




    public List<String> convertStringToList(String data) {
        String a[] = data.substring(1, data.length() - 1).split(",");
        List<String> res = new ArrayList<>(a.length);
        for (int i = 0; i < a.length; i++) {
            res.add(a[i]);
        }
        return res;
    }
    public List<Integer> convertStringListToIntList(List<String> data){
        List<Integer> result = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            result.add(Integer.valueOf(data.get(i).trim()));
        }
        return result;
    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}

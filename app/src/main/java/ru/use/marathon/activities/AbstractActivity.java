package ru.use.marathon.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import ru.use.marathon.AppController;
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
}

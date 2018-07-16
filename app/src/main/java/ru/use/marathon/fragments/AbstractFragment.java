package ru.use.marathon.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.HashMap;

import ru.use.marathon.activities.LoginActivity;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;

/**
 * Created by ilyas on 14-Jul-18.
 */

public class AbstractFragment extends Fragment {

    public static final int STUDENT = 0;
    public static final int TEACHER = 1;

    public Teacher teacher;
    public Student student;
    public HashMap<String, String> user_data;
    ProgressDialog  dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teacher = new Teacher(getActivity().getApplicationContext());
        student = new Student(getActivity().getApplicationContext());

        if (userType() == STUDENT) user_data = student.getData();
        else if (userType() == TEACHER) user_data = teacher.getData();
    }

    public void logout(){
        int ut = userType();
        if(ut == TEACHER){
            teacher.login(false);
        }else{
            student.login(false);
        }
        Intent i = new Intent(getActivity(),LoginActivity.class);
        getActivity().startActivity(i);
        getActivity().finish();
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

    public int user_id() {
        int ut = userType();
        if (ut == STUDENT) {
            return Integer.valueOf(user_data.get(student.KEY_ID));
        } else if (ut == TEACHER) {
            return Integer.valueOf(user_data.get(teacher.KEY_ID));
        }
        return -1;
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

    //todo set teacher image
    public String image() {
        int ut = userType();
        if (ut == STUDENT) {
            return user_data.get(student.KEY_IMAGE);
        } else if (ut == TEACHER) {
            return user_data.get(teacher.KEY_EMAIL);
        }
        return "null";
    }

    public void showInfoDialog(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showLoadDialog(Context c, String title, String message){
        dialog = new ProgressDialog(c);
        dialog.setTitle(title);
        dialog.setIndeterminate(true);
        dialog.setMessage(message);
        dialog.setProgress(0);
        dialog.show();
    }

    public void closeLoadDialog(){
        if(dialog !=null){
            dialog.dismiss();
        }
    }

}

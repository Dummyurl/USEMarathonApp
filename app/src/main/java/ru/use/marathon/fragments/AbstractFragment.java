package ru.use.marathon.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.activities.auth.LoginActivity;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;
import ru.use.marathon.utils.InternetConnectionListener;

/**
 * Created by ilyas on 14-Jul-18.
 */

public class AbstractFragment extends Fragment implements InternetConnectionListener {

    public static final int STUDENT = 0;
    public static final int TEACHER = 1;

    public Teacher teacher;
    public Student student;
    public HashMap<String, String> user_data;
    ProgressDialog dialog;
    AlertDialog.Builder builder;
    AlertDialog adialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppController)getActivity().getApplication()).setInternetConnectionListener(this);


        teacher = new Teacher(getActivity().getApplicationContext());
        student = new Student(getActivity().getApplicationContext());

        if (userType() == STUDENT) user_data = student.getData();
        else if (userType() == TEACHER) user_data = teacher.getData();
    }

    public void logout() {
        int ut = userType();
        if (ut == TEACHER) {
            teacher.login(false);
        } else {
            student.login(false);
        }
        Intent i = new Intent(getActivity(), LoginActivity.class);
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

    public int subject() {
        return student.getSubject();
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

    public void showInfoDialog(String title, String message) {
        if (adialog == null) {
            builder = new AlertDialog.Builder(getActivity());
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
        }else{
            builder = null;
            adialog = null;
        }
    }

    public void showLoadDialog(Context c, String title, String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(c);
            dialog.setTitle(title);
            dialog.setIndeterminate(true);
            dialog.setMessage(message);
            dialog.setProgress(0);
            dialog.show();
        }else{
            dialog = null;
        }
    }

    public void closeLoadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void closeDialog() {
        if (adialog != null) {
            adialog.dismiss();
        }
    }


    public void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void updateRegToken() {
        AppController.getApi().updateRegToken(1, "updateToken", FirebaseInstanceId.getInstance().getToken(), user_id(), userType()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                new Success(response);
//                Toast.makeText(getActivity().getApplicationContext(), success() ? "ok": "not ok", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onInternetUnavailable() {
        showInfoDialog("Подключение к сети","Нет подлючения к интернету. Проверьте подключение или повторите позднее");
    }

    @Override
    public void onPause() {
        super.onPause();
        ((AppController)getActivity().getApplication()).removeInternetConnectionListener();
    }
}

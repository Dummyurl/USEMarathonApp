package ru.use.marathon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.activities.MainActivity;
import ru.use.marathon.activities.RegisterActivity;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.Teacher;

import static ru.use.marathon.models.Success.success;

/**
 * Created by ilyas on 09-Jun-18.
 */

public class LoginStudentFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.s_email_et)
    EditText emailEditText;
    @BindView(R.id.s_password_et)
    EditText passwordEditText;
    @BindView(R.id.s_sign_in_btn)
    Button sign_in;
    @BindView(R.id.s_sign_up_btn)
    TextView sign_up;


    public LoginStudentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_student, container, false);
        unbinder = ButterKnife.bind(this, view);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                AppController.getApi().sign_in(1,"sign_in",0,email,password).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        new Success(response);
                        if(success()){
                            Student student = new Student(getActivity().getApplicationContext(),response);
                            Teacher teacher = new Teacher(getActivity().getApplicationContext());
                            teacher.login(false);
                            String name = student.getName();
                            int ID = student.getID();
                            student.createSession(ID,name,email);
                            startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                            getActivity().finish();

                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Error..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "Bad error..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                i.putExtra("post",0);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

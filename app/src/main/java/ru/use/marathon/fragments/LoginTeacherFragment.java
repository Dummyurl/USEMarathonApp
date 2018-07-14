package ru.use.marathon.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class LoginTeacherFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.t_email_et)
    EditText emailEditText;
    @BindView(R.id.t_password_et)
    EditText passwordEditText;
    @BindView(R.id.t_sign_in_btn)
    Button sign_in;
    @BindView(R.id.t_sign_up_btn)
    TextView sign_up;

    public LoginTeacherFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_teacher, container, false);
        unbinder = ButterKnife.bind(this, view);

        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(!checkEmail() && !checkPassword()) {


                    AppController.getApi().sign_in(1, "sign_in", 1, email, password).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            new Success(response);
                            if (success()) {
                                Teacher teacher = new Teacher(getActivity().getApplicationContext(), response);
                                Student student = new Student(getActivity().getApplicationContext());
                                student.login(false);
                                String name = teacher.getName();
                                int ID = teacher.getID();
                                teacher.createSession(ID, name, email);

                                Intent i = new Intent(getActivity(), MainActivity.class);
                                startActivity(i);
                                getActivity().finish();

                            } else {
                                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                                b.setTitle("Ошибка");
                                b.setMessage("Проверьте введенные данные.");
                                b.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(getActivity().getApplicationContext(), "Bad error..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                i.putExtra("post",1);
                startActivity(i);
            }
        });


        return view;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    public boolean checkEmail(){
        String text = emailEditText.getText().toString().trim();
        if (text.isEmpty() || !isValidEmail(text)) {
            emailEditText.setError("Неверный email");
            requestFocus(emailEditText);
            return false;
        }else{
            return false;
        }
    }

    public boolean checkPassword(){
        String password = passwordEditText.getText().toString().trim();
        if(password.length() <= 5){
            passwordEditText.setError("Пароль должен содержать минимум 5 символов");
            requestFocus(passwordEditText);
            return true;
        }else{
            return false;
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.t_email_et:
                    checkEmail();
                    break;
                case R.id.t_password_et:
                    checkPassword();
                    break;
            }
        }

    }


}

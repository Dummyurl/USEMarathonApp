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
import ru.use.marathon.activities.auth.ForgotPasswordActivity;
import ru.use.marathon.activities.auth.GoogleSignInActivity;
import ru.use.marathon.activities.MainActivity;
import ru.use.marathon.activities.auth.RegisterActivity;
import ru.use.marathon.activities.auth.VkAuthActivity;
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
    @BindView(R.id.s_fogt_btn)
    TextView fogt;
    @BindView(R.id.s_vk_up_btn)
    Button vk_up;
    @BindView(R.id.s_google_up_btn)
    Button google_up;



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
                login();
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
        fogt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ForgotPasswordActivity.class);
                i.putExtra("post",0);
                startActivity(i);
            }
        });

        vk_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), VkAuthActivity.class);
                i.putExtra("post",0);
                startActivity(i);
            }
        });
        google_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), GoogleSignInActivity.class);
                i.putExtra("post",0);
                startActivity(i);
            }
        });


        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));
        return view;
    }


    public void login(){


        final String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!checkEmail() && !checkPassword()) {


            AppController.getApi().sign_in(1, "sign_in", 0, email, password).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    new Success(response);
                    if (success()) {
                        Student student = new Student(getActivity().getApplicationContext(), response);
                        Teacher teacher = new Teacher(getActivity().getApplicationContext());
                        teacher.login(false);
                        String name = student.getName();
                        int ID = student.getID();
                        student.createSession(ID, name, email, student.getImage(), student.getTeacher_id(), student.getTests_counter(),
                                student.getTest_time(), student.getAnswersCounter(), student.getAnswersWrongCounter());

                        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                        getActivity().finish();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Неверный пароль");
                        builder.setMessage("Введен неверный пароль. Попробуйте еще раз..");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                        positiveButton.setTextColor(getResources().getColor(R.color.vk_black));
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getActivity().getApplicationContext(), "Bad error..", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
                case R.id.s_email_et:
                    checkEmail();
                    break;
                case R.id.s_password_et:
                    checkPassword();
                    break;
            }
        }

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

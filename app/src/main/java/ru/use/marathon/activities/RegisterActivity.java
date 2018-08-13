package ru.use.marathon.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.Preference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.fragments.StartQuestionFragment;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.Teacher;

import static ru.use.marathon.models.Success.success;

public class RegisterActivity extends AbstractActivity {

    @BindView(R.id.name_et)
    EditText nameEditText;
    @BindView(R.id.email_et)
    EditText emailEditText;
    @BindView(R.id.password_et)
    EditText passwordEditText;
    @BindView(R.id.sign_up_btn)
    Button sign_up_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        final int post = getIntent().getIntExtra("post",-1);

        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        nameEditText.addTextChangedListener(new MyTextWatcher(nameEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(checkEmail() || !checkPassword() || !validateName()) {

                    AppController.getApi().sign_up(1, "sign_up", post, name, email, password).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            new Success(response);
                            if (success()) {
                                if (post == 1) {
                                    Teacher t = new Teacher(getApplicationContext(), response);
                                    t.createSession(t.getID(), name, email);
                                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(i);
                                } else {
                                    Student s = new Student(getApplicationContext(), response);
                                    s.createFirstSession(s.getID(), name, email); // todo if sign up, image will crush
                                    Intent i = new Intent(RegisterActivity.this, StartQuestionsActivity.class);
                                    startActivity(i);
                                }


                            } else {
                                AlertDialog.Builder b = new AlertDialog.Builder(RegisterActivity.this);
                                b.setTitle("Ошибка");
                                b.setMessage("Произошла непредвиденная ошибка. Повторите действие позднее");
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
                            Toast.makeText(RegisterActivity.this, "Big problem ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Введите все данные", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private boolean validateName() {
        if (nameEditText.getText().toString().trim().isEmpty()) {
            requestFocus(nameEditText);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    public boolean checkEmail(){
        String text = emailEditText.getText().toString().trim();
        if (text.isEmpty() || !isValidEmail(text)) {
            emailEditText.setError("Неверный email");
            requestFocus(emailEditText);
            return false;
        }else{
            return true;
        }
    }

    public boolean checkPassword(){
        String password = passwordEditText.getText().toString().trim();
        if(password.length() <= 5){
            passwordEditText.setError("Пароль должен содержать минимум 5 символов");
            requestFocus(passwordEditText);
            return false;
        }else{
            return true;
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
                case R.id.email_et:
                    checkEmail();
                    break;
                case R.id.password_et:
                    checkPassword();
                    break;
                case R.id.name_et:
                    validateName();
                    break;
            }
        }

    }
}

package ru.use.marathon.activities.auth;
/**
 * Created by Marat on 24-July-18.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.activities.AbstractActivity;
import ru.use.marathon.activities.MainActivity;
import ru.use.marathon.models.RestoreCode;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;

import static ru.use.marathon.models.RestoreCode.code;


public class ForgotPasswordActivity extends AbstractActivity {

    public static final String TAG = ForgotPasswordActivity.class.getSimpleName();
//    @BindView(R.id.name_et)
//    EditText nameEditText;
    @BindView(R.id.enter_btn)
    Button ent_btn;
    @BindView(R.id.again_btn)
    Button again_btn;
//    @BindView(R.id.gift_et)
//    EditText gift_et;
    @BindView(R.id.first_c)
    EditText firs_c;
    @BindView(R.id.first_c2)
    EditText firs_c2;
    @BindView(R.id.first_c3)
    EditText firs_c3;
    @BindView(R.id.first_c4)
    EditText firs_c4;


    @BindView(R.id.tv)
    TextView tv_tv;
    @BindView(R.id.email_et)
    EditText emailEditText;
    @BindView(R.id.password_et)
    EditText passwordEditText;
    @BindView(R.id.forgotpass_btn)
    Button fogt_btn;
    String str="";
    int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        ButterKnife.bind(this);
        final int post = getIntent().getIntExtra("post",-1);

        //nameEditText.addTextChangedListener(new MyTextWatcher(nameEditText));
        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));
        //firs_c.addTextChangedListener(new MyTextWatcher(firs_c));



        fogt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // final String name = nameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                    AppController.getApi().sendPHPMail(1, "sendPHPMail", email).enqueue(new Callback<JsonObject>() {

                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            new RestoreCode(response);
                            if (success(response)) {
                                code=code();
                                new CountDownTimer(122000, 1000) {

                                    public void onTick(long l) {
                                        tv_tv.setText("Времени осталось : \n" + l/1000);
                                    }

                                    public void onFinish() {
                                        fogt_btn.setText("Отправить повторно");
                                        fogt_btn.setVisibility(View.VISIBLE);
                                        tv_tv.setText("Время истекло");

                                    }
                                }.start();



                                emailEditText.setVisibility(View.GONE);
                                passwordEditText.setVisibility(View.GONE);
                                fogt_btn.setVisibility(View.GONE);
                                tv_tv.setVisibility(View.VISIBLE);
//                                gift_et.setVisibility(View.VISIBLE);
                                ent_btn.setVisibility(View.VISIBLE);


                            } else {
                                AlertDialog.Builder b = new AlertDialog.Builder(ForgotPasswordActivity.this);
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
                            Log.d(TAG, "onFailure: " + t.getMessage());

                            Toast.makeText(ForgotPasswordActivity.this, "Big problem ", Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        });



        ent_btn.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view) {
//                String Cod = gift_et.getText().toString();
                String Cod = "1";
                if(code == Integer.valueOf(Cod)){
               // final String name = nameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                    AppController.getApi().restorePassword(1, "restorePassword", email, post,  password).enqueue(new Callback<JsonObject>() {

                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            new RestoreCode(response);

                            if (success(response)) {
                                if (post == 1) {
                                    Teacher teacher = new Teacher(getApplicationContext(), response);
                                    Student student = new Student(getApplicationContext());
                                    student.login(false);
                                    String name = teacher.getName();
                                    int ID = teacher.getID();
                                    teacher.createSession(ID, name, email);

                                    Intent i = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Student student = new Student(getApplicationContext(), response);
                                    Teacher teacher = new Teacher(getApplicationContext());
                                    teacher.login(false);
                                    String name = student.getName();
                                    int ID = student.getID();
                                    student.createSession(ID, name, email, student.getImage(), student.getTeacher_id(), student.getTests_counter(),
                                            student.getTest_time(), student.getAnswersCounter(), student.getAnswersWrongCounter());

                                    Intent i = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            } else {
                                AlertDialog.Builder b = new AlertDialog.Builder(ForgotPasswordActivity.this);
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
                            Toast.makeText(ForgotPasswordActivity.this, "Big problem ", Toast.LENGTH_SHORT).show();
                        }
                    });

            }}
        });


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
            return false;
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
            }
        }

    }
}

package ru.use.marathon.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.models.CityShow;
import ru.use.marathon.models.RegionShow;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.Teacher;

import static ru.use.marathon.models.Success.success;

public class RegisterActivity extends AbstractActivity {

    ArrayList<String> regions = new ArrayList<>();
    ArrayList<String> cities = new ArrayList<>();
    SpinnerDialog regions_sd;
    SpinnerDialog cities_sd;


    @BindView(R.id.region_btn)
    Button region_btn;
    @BindView(R.id.city_btn)
    Button city_btn;
    @BindView(R.id.city_false_btn)
    Button city_false_btn;
    @BindView(R.id.checkBox)
    CheckBox checkbox;
    @BindView(R.id.phone_number_et)
    EditText phone_et;
    @BindView(R.id.name_et)
    EditText nameEditText;
    @BindView(R.id.email_et)
    EditText emailEditText;
    @BindView(R.id.password_et)
    EditText passwordEditText;
    @BindView(R.id.sign_up_btn)
    Button sign_up_btn;

    int ID;
    int counter = 0;
    boolean isAgree = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);


        AppController.getApi().regionshow(1, "regionshow", 1).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                RegionShow rs = new RegionShow(response);
                for (int i = 0; i < rs.getData().size(); i++) {
                    regions.add(rs.name(i));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showInfoDialog("Ошибка!", "Произошла ошибка при загрузке регионов. Повторите позднее.");
            }
        });


        region_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ID != 0) {
                    cities.removeAll(cities);
                    city_btn.setVisibility(View.GONE);
                    city_false_btn.setVisibility(View.VISIBLE);
                    counter = 0;


                }

                regions_sd = new SpinnerDialog(RegisterActivity.this, regions, "Select item", R.style.DialogAnimations_SmileWindow, "Close");
                regions_sd.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        ID = position + 1;
                        counter++;
                        region_btn.setText(item);

                        AppController.getApi().cityshow(1, "cityshow", ID).enqueue(new Callback<JsonObject>() {

                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                CityShow rs = new CityShow(response);
                                for (int i = 0; i < rs.getData().size(); i++) {
                                    cities.add(rs.name(i));


                                }
                                city_false_btn.setVisibility(View.GONE);
                                city_btn.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                showInfoDialog("Ошибка!", "Произошла ошибка при загрузке городов. Повторите позднее.");
                            }
                        });

                    }
                });
                regions_sd.showSpinerDialog();


            }

        });


        city_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cities_sd = new SpinnerDialog(RegisterActivity.this, cities, "Select item", R.style.DialogAnimations_SmileWindow, "Close");
                cities_sd.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        Toast.makeText(RegisterActivity.this, item + "  " + position + 1 + "", Toast.LENGTH_SHORT).show();
                        city_btn.setText(item);
                        counter++;
                    }
                });
                cities_sd.showSpinerDialog();


            }
        });


        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAgree = checkbox.isChecked();
            }
        });

        String link = "Я принимаю условия <a href=\"https://cordi.space/exam/agreement.html\">Пользовательского соглашения</a>";
        checkbox.setText(Html.fromHtml(link));

        final int post = getIntent().getIntExtra("post", -1);

        nameEditText.addTextChangedListener(new MyTextWatcher(nameEditText));
        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));
        phone_et.addTextChangedListener(new MyTextWatcher(phone_et));


        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                final String num = phone_et.getText().toString();
                if (checkEmail() && checkPassword() && validateName() && checkPhoneNumber() && isAgree && counter == 2) {


                    AppController.getApi().sign_up(1, "sign_up", post, name, email, password, num).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            new Success(response);
                            Toast.makeText(RegisterActivity.this, num, Toast.LENGTH_SHORT).show();
                            if (success(response)) {
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
                } else {
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


    public boolean checkEmail() {
        String text = emailEditText.getText().toString().trim();
        if (text.isEmpty() || !isValidEmail(text)) {
            emailEditText.setError("Неверный email");
            requestFocus(emailEditText);
            return false;
        } else {
            return true;
        }
    }


    public boolean checkPassword() {
        String password = passwordEditText.getText().toString().trim();
        if (password.length() <= 5) {
            passwordEditText.setError("Пароль должен содержать минимум 5 символов");
            requestFocus(passwordEditText);
            return false;
        } else {
            return true;
        }
    }

    public boolean checkPhoneNumber() {

        if (phone_et.length() == 11) {
            return true;
        } else {
            phone_et.setError("Номер должен содержать 11 цифр");
            requestFocus(phone_et);
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
                case R.id.email_et:
                    checkEmail();
                    break;
                case R.id.password_et:
                    checkPassword();
                    break;
                case R.id.name_et:
                    validateName();
                    break;
                case R.id.phone_number_et:
                    checkPhoneNumber();
                    break;
            }
        }

    }
}

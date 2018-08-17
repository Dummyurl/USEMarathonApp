package ru.use.marathon.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
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
import ru.use.marathon.models.RegionSHow;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.Teacher;
import static ru.use.marathon.models.Success.success;
public class RegisterActivity extends AbstractActivity  {

    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> cityS = new ArrayList<>();
    SpinnerDialog spinnerDialog;
    SpinnerDialog spinnerDialog2;


    @BindView(R.id.region_btn)
    Button region_btn;
    @BindView(R.id.city_btn)
    Button city_btn;
    @BindView(R.id.city_false_btn)
    Button city_false_btn;
    @BindView(R.id.checkBox)
    CheckBox checkbox;
    @BindView(R.id.pupo_et)
    EditText phone_et;
    @BindView(R.id.name_et)
    EditText nameEditText;
    @BindView(R.id.email_et)
    EditText emailEditText;
    @BindView(R.id.password_et)
    EditText passwordEditText;
    @BindView(R.id.sign_up_btn)
    Button sign_up_btn;
    @BindView(R.id.spiner)
    Spinner spiner;
    JsonArray regioin;
    JsonObject jsoni;
    int id_subject;
    int IDI;
    int ctIDI;
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        String[] datax = {"Русский", "Математика (База)","Математика (Профиль)" };

        ButterKnife.bind(this);





        AppController.getApi().regionshow(1, "regionshow", 1).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                RegionSHow rs = new RegionSHow(response);
                for (int i=0;i<rs.getData().size();i++){
                    items.add(rs.name(i));





                }}
                @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }});









        region_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IDI != 0){
                    cityS.removeAll(cityS);
                    city_btn.setVisibility(View.GONE);
                    city_false_btn.setVisibility(View.VISIBLE);
                    counter=0;


                }

                       // spinnerDialog=new SpinnerDialog(MainActivity.this,items,"Select or Search City",R.style.DialogAnimations_SmileWindow,"Close Button Text");
                        spinnerDialog =new SpinnerDialog(RegisterActivity.this,items,"Select item",R.style.DialogAnimations_SmileWindow,"Close");
                        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                //                Toast.makeText(RegisterActivity.this, item + "  " + position+1+"", Toast.LENGTH_SHORT).show();
                                IDI=position+1;
                                counter++;
                                region_btn.setText(item );
/////////////города

                                AppController.getApi().cityshow(1, "cityshow", IDI).enqueue(new Callback<JsonObject>() {

                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        CityShow rs = new CityShow(response);
                                        for (int i=0;i<rs.getData().size();i++){
                                            cityS.add(rs.name(i));






                                        }

                                        city_false_btn.setVisibility(View.GONE);
                                        city_btn.setVisibility(View.VISIBLE);}

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        Toast.makeText(RegisterActivity.this,"в чем то проблемка", Toast.LENGTH_SHORT).show();
                                    }});



                                //////////////////kone4
                            }
                        });
                        spinnerDialog.showSpinerDialog();




                    }

            });



            city_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {






                    // spinnerDialog=new SpinnerDialog(MainActivity.this,items,"Select or Search City",R.style.DialogAnimations_SmileWindow,"Close Button Text");
                    spinnerDialog2 =new SpinnerDialog(RegisterActivity.this,cityS,"Select item",R.style.DialogAnimations_SmileWindow,"Close");
                    spinnerDialog2.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                           // Toast.makeText(RegisterActivity.this, item + "  " + position+1+"", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(RegisterActivity.this, "Selected : "+item, Toast.LENGTH_SHORT).show();
                            city_btn.setText(item );
                            counter++;
                            ctIDI=position+1;
                        }
                    });
                    spinnerDialog2.showSpinerDialog();



                }
            });




        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mylink.com"));
                startActivity(browserIntent);
            }});




        Spannable spans = new SpannableString("Я принимаю условия Пользовательского соглашения");
        spans.setSpan(new ForegroundColorSpan(Color.BLUE), 18, 47, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkbox.setText(spans);
        final int post = getIntent().getIntExtra("post",-1);

        nameEditText.addTextChangedListener(new MyTextWatcher(nameEditText));
        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));
        phone_et.addTextChangedListener(new MyTextWatcher(phone_et));





        if(post == 1) {
            spiner.setVisibility(View.VISIBLE);

            // адаптер
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datax);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spiner.setAdapter(adapter1);
            // заголовок
            spiner.setPrompt("Предмет");
            // выделяем элемент
          //  spiner.setSelection(2);
            // устанавливаем обработчик нажатия
            spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    id_subject = position+1;
                   // id_subject = (String)parent.getItemAtPosition(position);

                    // показываем позиция нажатого элемента
                  //  Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getBaseContext(), id_subject , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });


        }





        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                final String num = phone_et.getText().toString();
                if(checkEmail() && checkPassword() && validateName() && checkPhoneN() && checkbox.isChecked() && counter==2) {


                    AppController.getApi().sign_up(1, "sign_up", post, name, email, password,num,IDI,ctIDI,id_subject).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            new Success(response);
                            Toast.makeText(RegisterActivity.this, num, Toast.LENGTH_SHORT).show();
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
    public boolean checkPhoneN(){

        if(phone_et.length() == 11){
            return true;
        }else{
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
            }
        }

    }
}

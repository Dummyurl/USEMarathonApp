package ru.use.marathon.activities.auth;
/**
 * Created by Marat on 24-July-18.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.Objects;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.activities.MainActivity;
import ru.use.marathon.activities.StartQuestionsActivity;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.Teacher;

import static ru.use.marathon.models.Success.success;

public class VkAuthActivity extends AppCompatActivity {
    final Context context = this;
    private static final String TAG = "vkauth";


    private String[] scope = new String[]{VKScope.FRIENDS, VKScope.EMAIL, VKScope.MESSAGES};

    private String vktoken;
    String firs_name;
    String phone_number;
    String vkemail;
    String city_home;
    String vkpassword;
    private  int b=0;
    int id_subject = 0;
    String country;
    private FirebaseAuth mAuth;
    DatabaseReference databasePersons;
    private int poste;
    String vkuser_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vkauth);
        VKSdk.login(this, scope);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final int post = getIntent().getIntExtra("post", -1);
        poste = post;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(final VKAccessToken res) {

                vktoken = res.accessToken + "vk";
                vkpassword = res.userId+"edway";
                vkuser_id ="https://vk.com/id" + res.userId;

          //      Toast.makeText(VkAuthActivity.this, vkuser_id, Toast.LENGTH_SHORT).show();


//                Toast.makeText(VkAuthActivity.this, "Все оке", Toast.LENGTH_SHORT).show();
                VKApi.users().get().executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKApiUser user = ((VKList<VKApiUser>) response.parsedModel).get(0);

                        firs_name = user.first_name;
                        vkemail = res.email;


                   //     Toast.makeText(VkAuthActivity.this, (CharSequence) user.photo, Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_EMAIL, vkemail);
                        editor.putString(APP_PREFERENCES_NAME,firs_name );
                        editor.apply();
//                        Toast.makeText(VkAuthActivity.this, vkemail + "toPlustTO", Toast.LENGTH_SHORT).show();

                        if (vkemail == null) {
                            //TODO Все отловы данных делай на английском языке полным предложением и не забывай про DEBUG
                            //Toast.makeText(VkAuthActivity.this, "с вк не то", Toast.LENGTH_SHORT).show();
                            finish();
                           // alertdial();
                        }

                            signinSql();



                    }
                });


                final VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "contacts"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKApiUserFull user = ((VKList<VKApiUserFull>) response.parsedModel).get(0);
                        phone_number = user.mobile_phone;
                        city_home = String.valueOf(user.city);
                        country = String.valueOf(user.country);
                        if (!Objects.equals(country, "Россия")) {
//                            Toast.makeText(context, country + "  ты не раша", Toast.LENGTH_SHORT).show();

                            //TODO в файле Constants есть boolean DEBUG. Используй его чтобы тебе было удобно при отладке.
                            //TODO ставишь if(DEBUG) к тостам, логам, чтобы видеть данные самому, а потом при релизе просто не будет видно юзерам.
                        }


                    }
                });





            }

            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
//                Toast.makeText(VkAuthActivity.this, "Все не оке", Toast.LENGTH_SHORT).show();
                finish();

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    private void alertdial() {
//        //TODO я про это говорю в классе GoogleSignInActivity
//
//        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
//        LayoutInflater li = LayoutInflater.from(context);
//        View promptsView = li.inflate(R.layout.vkalertdialog, null);
//
//
//        //Создаем AlertDialog
//        android.app.AlertDialog.Builder mDialogBuilder = new android.app.AlertDialog.Builder(context);
//        mDialogBuilder.setTitle("Введите email!");
//        //Настраиваем prompt.xml для нашего AlertDialog:
//        mDialogBuilder.setView(promptsView);
//
//
//        if (poste == 1) {
//            mDialogBuilder.setTitle("Выберите свой предмет");
//            final String[] datax = {"Русский", "Математика (База)", "Математика (Профиль)"};
//            final Spinner spiner = (Spinner) promptsView.findViewById(R.id.spiner);
//            spiner.setVisibility(View.VISIBLE);
//
//
//            // адаптер
//            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datax);
//            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//            spiner.setAdapter(adapter1);
//            // заголовок
//            spiner.setPrompt("Предмет");
//            // выделяем элемент
//            //  spiner.setSelection(2);
//            // устанавливаем обработчик нажатия
//            spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,
//                                           int position, long id) {
//                    id_subject = position + 1;
//                    // id_subject = (String)parent.getItemAtPosition(position);
//
//                    // показываем позиция нажатого элемента
//                    //  Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
//                    //Toast.makeText(getBaseContext(), id_subject , Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> arg0) {
//                }
//            });
//
//
//        }
//
//
//        //Настраиваем отображение поля для ввода текста в открытом диалоге:
//        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
//        if (vkemail != null) {
//            userInput.setVisibility(View.GONE);
//        }
//
//        //Настраиваем сообщение в диалоговом окне:
//        mDialogBuilder
//                .setCancelable(false)
//                .setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                //Вводим текст и отображаем в строке ввода на основном экране:
//                                vkemail = userInput.getText().toString();
//                              //  signIn(vkemail, vkpassword);
//                                signinSql();
//
//                            }
//                        })
//                .setNegativeButton("Отмена",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//        //Создаем AlertDialog:
//        android.app.AlertDialog alertDialog = mDialogBuilder.create();
//
//        //и отображаем его:
//        alertDialog.show();
//        Button negativeButton = ((AlertDialog)alertDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
//        Button positiveButton = ((AlertDialog)alertDialog).getButton(DialogInterface.BUTTON_POSITIVE);
//        positiveButton.setTextColor(getResources().getColor(R.color.vk_black));
//        negativeButton.setTextColor(getResources().getColor(R.color.vk_black));
//        userInput.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
//
//    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
////                            Toast.makeText(VkAuthActivity.this, "Authentication failed.",
////                                    Toast.LENGTH_SHORT).show();
//                            signIn(vkemail, vkpassword);
//
//
//                        }
//
//                    }
//                });
    }

//    private void signIn(String email, String password) {
//        Log.d(TAG, "signIn:" + email);
//
//        if (!validateForm()) {
//            return;
//        }
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
////                            Toast.makeText(VkAuthActivity.this, "Authentication failed.",
////                                    Toast.LENGTH_SHORT).show();
//                            createAccount(vkemail, vkpassword);
//                        }
//
//                        if (!task.isSuccessful()) {
////                            Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
//                            createAccount(vkemail, vkpassword);
//                        }
//
//                    }
//                });
//    }

    private void signOut() {
        mAuth.signOut();
    }

    private void sendEmailVerification() {

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(VkAuthActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(VkAuthActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;


        if (vkemail == null) {
            valid = false;
        }


        if (vkpassword == null) {
            valid = false;
        }

        return valid;
    }
    public boolean ischeck_email(String email) {
    int a=0;
        AppController.getApi().check_email(1, "check_email", poste , email ).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                new Success(response);
                if(!success()) {
                    finish();
                    Toast.makeText(context, vkemail+'\n' +"занят", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context, "email занят либо невалидный", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(VkAuthActivity.this, "Попробуйте другой email ", Toast.LENGTH_SHORT).show();

            }
        });
            return true;

    }

    private void signupSql() {
        final String name = firs_name;
        final String email = vkemail;
        final String style_type = "vk";
        final String num = phone_number;

    if(ischeck_email(vkemail)) {
        AppController.getApi().sign_up(1, "sign_up", poste, name, email, "0", num, "0", city_home, id_subject, style_type, vkuser_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                new Success(response);
//                    Toast.makeText(VkAuthActivity.this, num, Toast.LENGTH_SHORT).show();
                if (success()) {
                    if (poste == 1) {
                        Teacher t = new Teacher(getApplicationContext(), response);
                        t.createSession(t.getID(), name, email);
                        Intent i = new Intent(VkAuthActivity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Student s = new Student(getApplicationContext(), response);
                        s.createFirstSession(s.getID(), name, email); // todo if sign up, image will crush
                        Intent i = new Intent(VkAuthActivity.this, StartQuestionsActivity.class);
                        startActivity(i);
                        finish();
                    }


                } else {
                    android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(VkAuthActivity.this);
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
                Toast.makeText(VkAuthActivity.this, "Big problem ", Toast.LENGTH_SHORT).show();
            }
        });


    }
    }




    private void signinSql() {
        final String email = vkemail;
        String password = vkpassword;

        if (vkemail != null ) {


            AppController.getApi().sign_in(1, "sign_in", poste, email, "0","vk").enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    new Success(response);
                    if (success() && poste == 1) {
                        Teacher teacher = new Teacher(getApplicationContext(), response);
                        Student student = new Student(getApplicationContext());
                        student.login(false);
                        String name = teacher.getName();
                        int ID = teacher.getID();
                        teacher.createSession(ID, name, email);

                        Intent i = new Intent(VkAuthActivity.this, MainActivity.class);
                        startActivity(i);


                    }
                    if (success() && poste == 0) {
                        Student student = new Student(getApplicationContext(), response);
                        Teacher teacher = new Teacher(getApplicationContext());
                        teacher.login(false);
                        String name = student.getName();
                        int ID = student.getID();
                        student.createSession(ID, name, email, student.getImage(), student.getTeacher_id(), student.getTests_counter(),
                                student.getTest_time(), student.getAnswersCounter(), student.getAnswersWrongCounter());

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        

                    } else {
                        signupSql();
                        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(VkAuthActivity.this);
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
                    Toast.makeText(getApplicationContext(), "Bad error..", Toast.LENGTH_SHORT).show();
                    signupSql();
                }
            });
        }
    }




//    public boolean checkEmail() {
//
//
//        AppController.getApi().check_email(1, "check_email", 0, vkemail).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                new Success(response);
//                // Toast.makeText(VkAuthActivity.this, num, Toast.LENGTH_SHORT).show();
//                if (success()) {
//                    b = 1;
//                    Toast.makeText(context, b, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(VkAuthActivity.this, "Big problem ", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//      //  Toast.makeText(context, "suda doiiilo", Toast.LENGTH_SHORT).show();
//        if(b==1){
//            Toast.makeText(context, "VSE NORME", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        else {
//        return false;}
//        }

    public static final String APP_PREFERENCES = "student";
    public static final String APP_PREFERENCES_EMAIL = "email"; // email
    public static final String APP_PREFERENCES_NAME = "NAME"; // AGE
    //public static final String APP_PREFERENCES_PHOTO = "PHOTO"; // PHOTO
  //  public static final Image APP_PREFERENCES_PHOTO;
    SharedPreferences mSettings;




}

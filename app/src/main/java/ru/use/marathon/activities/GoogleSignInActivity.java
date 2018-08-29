

package ru.use.marathon.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class GoogleSignInActivity extends AbstractActivity implements
        View.OnClickListener {
    final Context context = this;

    @BindView(R.id.prompt_button)
    Button button;


    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> cityS = new ArrayList<>();
    SpinnerDialog spinnerDialog;
    SpinnerDialog spinnerDialog2;
    int id_subject=0;
    int IDI;

    int counter = 0;
    int ctIDI;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        ButterKnife.bind(this);



        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        signIn();


        AppController.getApi().regionshow(1, "regionshow", 1).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                RegionSHow rs = new RegionSHow(response);
                for (int i = 0; i < rs.getData().size(); i++) {
                    items.add(rs.name(i));


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(GoogleSignInActivity.this, "Big problem ", Toast.LENGTH_SHORT).show();
            }
        });


    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    private String first_name, googleemail, city, phone_number;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                first_name = account.getGivenName();
                googleemail = account.getEmail();
                Toast.makeText(this, first_name, Toast.LENGTH_SHORT).show();
                signinSql();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {


            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

        } else {

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void alertdial() {
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);


        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final Button region = (Button) promptsView.findViewById(R.id.region_btn);
        final Button city = (Button) promptsView.findViewById(R.id.city_btn);
        final Button city_false = (Button) promptsView.findViewById(R.id.city_false_btn);
        final EditText userInput1 = (EditText) promptsView.findViewById(R.id.input_text1);

        if (poste == 1) {

            final String[] datax = {"Русский", "Математика (База)", "Математика (Профиль)"};
            final Spinner spiner = (Spinner) promptsView.findViewById(R.id.spiner);
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
                    id_subject = position + 1;
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


        region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IDI != 0) {
                    cityS.removeAll(cityS);
                    city.setVisibility(View.GONE);
                    city_false.setVisibility(View.VISIBLE);
                    counter = 0;


                }

                // spinnerDialog=new SpinnerDialog(MainActivity.this,items,"Select or Search City",R.style.DialogAnimations_SmileWindow,"Close Button Text");
                spinnerDialog = new SpinnerDialog(GoogleSignInActivity.this, items, "Select item", R.style.DialogAnimations_SmileWindow, "Close");
                spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        //                Toast.makeText(RegisterActivity.this, item + "  " + position+1+"", Toast.LENGTH_SHORT).show();
                        IDI = position + 1;
                        counter++;
                        region.setText(item);
/////////////города

                        AppController.getApi().cityshow(1, "cityshow", IDI).enqueue(new Callback<JsonObject>() {

                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                CityShow rs = new CityShow(response);
                                for (int i = 0; i < rs.getData().size(); i++) {
                                    cityS.add(rs.name(i));


                                }

                                city_false.setVisibility(View.GONE);
                                city.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Toast.makeText(GoogleSignInActivity.this, "в чем то проблемка", Toast.LENGTH_SHORT).show();
                            }
                        });


                        //////////////////kone4
                    }
                });
                spinnerDialog.showSpinerDialog();


            }

        });


        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // spinnerDialog=new SpinnerDialog(MainActivity.this,items,"Select or Search City",R.style.DialogAnimations_SmileWindow,"Close Button Text");
                spinnerDialog2 = new SpinnerDialog(GoogleSignInActivity.this, cityS, "Select item", R.style.DialogAnimations_SmileWindow, "Close");
                spinnerDialog2.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        // Toast.makeText(RegisterActivity.this, item + "  " + position+1+"", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(RegisterActivity.this, "Selected : "+item, Toast.LENGTH_SHORT).show();
                        city.setText(item);
                        counter++;
                        ctIDI = position + 1;
                    }
                });
                spinnerDialog2.showSpinerDialog();


            }
        });

        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder.setTitle("Заполните поля!");
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                phone_number = userInput1.getText().toString();
                                signupSql();


                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        //и отображаем его:
        alertDialog.show();


    }

    private int poste;

    private void signupSql() {
        final String name = first_name;
        final String email = googleemail;
        final String style_type = "google";
        final String num = phone_number;
        if (counter == 2 && phone_number.length() == 11) {
            final int post = getIntent().getIntExtra("post", -1);
            poste = post;
            AppController.getApi().sign_up(1, "sign_up", post, name, email, "0", num, String.valueOf(IDI), String.valueOf(ctIDI), 0, style_type).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    new Success(response);
                    Toast.makeText(GoogleSignInActivity.this, num, Toast.LENGTH_SHORT).show();
                    if (success()) {
                        if (post == 1) {
                            Teacher t = new Teacher(getApplicationContext(), response);
                            t.createSession(t.getID(), name, email);
                            Intent i = new Intent(GoogleSignInActivity.this, MainActivity.class);
                            startActivity(i);
                            signOut();
                        } else {
                            Student s = new Student(getApplicationContext(), response);
                            s.createFirstSession(s.getID(), name, email); // todo if sign up, image will crush
                            Intent i = new Intent(GoogleSignInActivity.this, StartQuestionsActivity.class);
                            startActivity(i);
                            signOut();
                        }


                    } else {
                        AlertDialog.Builder b = new AlertDialog.Builder(GoogleSignInActivity.this);
                        b.setTitle("Ошибка");
                        b.setMessage("Произошла непредвиденная ошибка. Повторите действие позднее");
                        b.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });


                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(GoogleSignInActivity.this, "Big problem ", Toast.LENGTH_SHORT).show();

                }
            });


        } else {
//            Toast.makeText(context, counter, Toast.LENGTH_SHORT).show();
            Toast.makeText(GoogleSignInActivity.this, "Введите все данные", Toast.LENGTH_SHORT).show();
            alertdial();

        }
    }


    private void signinSql() {

        String password = "0";
        final String email = googleemail;
        //!checkEmail()

        if (email != null) {

            AppController.getApi().sign_in(1, "sign_in", poste, email, "0").enqueue(new Callback<JsonObject>() {
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

                        Intent i = new Intent(GoogleSignInActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        signOut();

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
                        finish();

                        signOut();


                    } else {
                        alertdial();
                        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(GoogleSignInActivity.this);
                        b.setTitle("Ошибка");
                        b.setMessage("Проверьте введенные данные.");
                        b.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Bad error..", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean checkEmail() {
        String text = googleemail;
        if (text == null || !isValidEmail(text)) {


            return false;
        } else {
            return true;
        }
    }


    public boolean checkPhoneN(String phone_et) {

        if (phone_et.length() == 11) {
            return true;
        } else {

            return false;
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }  else if (i == R.id.disconnect_button) {
            revokeAccess();
        }
    }
}
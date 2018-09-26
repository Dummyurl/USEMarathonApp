

package ru.use.marathon.activities.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.activities.AbstractActivity;
import ru.use.marathon.activities.MainActivity;
import ru.use.marathon.activities.StartQuestionsActivity;
import ru.use.marathon.models.CityShow;
import ru.use.marathon.models.RegionShow;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.Teacher;

import static ru.use.marathon.Constants.DEBUG;


public class GoogleSignInActivity extends AbstractActivity implements
        View.OnClickListener {
    final Context context = this;


    ArrayList<String> items = new ArrayList<>();
    @BindView(R.id.prompt_button)
    Button button;
    ArrayList<String> cityS = new ArrayList<>();
    SpinnerDialog spinnerDialog;
    SpinnerDialog spinnerDialog2;
    int id_subject = 0;
    int IDI;
    String googlepass;
    int counter = 0;
    int ctIDI;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        ButterKnife.bind(this);


        findViewById(R.id.sign_in_button).setOnClickListener(this);

        findViewById(R.id.disconnect_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        signIn();


        AppController.getApi().regionshow(1, "regionshow", 1).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                RegionShow rs = new RegionShow(response);
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


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private String first_name, googleemail, city, phone_number;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                first_name = account.getGivenName();
                googleemail = account.getEmail();
                googlepass = account.getIdToken() + "google";
                signinSql();
                if (DEBUG) Toast.makeText(this, first_name, Toast.LENGTH_SHORT).show();

            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
                finish();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

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

                        hideProgressDialog();
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {

        mAuth.signOut();

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {

        mAuth.signOut();

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
        //TODO Сделай вот это все отдельным классом, чтобы 2 раза не писать кучу кода

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

        mDialogBuilder.setView(promptsView);

        final Button region = (Button) promptsView.findViewById(R.id.region_btn);
        final Button city = (Button) promptsView.findViewById(R.id.city_btn);
        final Button city_false = (Button) promptsView.findViewById(R.id.city_false_btn);
        final EditText userInput1 = (EditText) promptsView.findViewById(R.id.input_text1);

        if (poste == 1) {

            final String[] datax = {"Русский", "Математика (База)", "Математика (Профиль)"};
            final Spinner spiner = (Spinner) promptsView.findViewById(R.id.spiner);
            spiner.setVisibility(View.VISIBLE);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datax);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spiner.setAdapter(adapter1);
            // заголовок
            spiner.setPrompt("Предмет");
            // выделяем элемент
            // устанавливаем обработчик нажатия
            spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    id_subject = position + 1;

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

                spinnerDialog = new SpinnerDialog(GoogleSignInActivity.this, items, "Select item", R.style.DialogAnimations_SmileWindow, "Close");
                spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        //                Toast.makeText(RegisterActivity.this, item + "  " + position+1+"", Toast.LENGTH_SHORT).show();
                        IDI = position + 1;
                        counter++;
                        region.setText(item);

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

                    }
                });
                spinnerDialog.showSpinerDialog();


            }

        });


        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                spinnerDialog2 = new SpinnerDialog(GoogleSignInActivity.this, cityS, "Select item", R.style.DialogAnimations_SmileWindow, "Close");
                spinnerDialog2.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        city.setText(item);
                        counter++;
                        ctIDI = position + 1;
                    }
                });
                spinnerDialog2.showSpinerDialog();


            }
        });
        mDialogBuilder.setTitle("Заполните поля!");
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                phone_number = userInput1.getText().toString();
                                signupSql();


                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                signOut();
                                finish();
                            }
                        });


        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
        Button negativeButton = ((AlertDialog) alertDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        Button positiveButton = ((AlertDialog) alertDialog).getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.vk_black));
        negativeButton.setTextColor(getResources().getColor(R.color.vk_black));
        userInput1.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);


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
            AppController.getApi().sign_up(1, "sign_up", post, name, email, googlepass, num, String.valueOf(IDI), String.valueOf(ctIDI), 0, style_type,"").enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    new Success(response);
                    Toast.makeText(GoogleSignInActivity.this, num, Toast.LENGTH_SHORT).show();
                    if (success(response)) {
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


    public void ischeck_email(String email) {

        AppController.getApi().check_email(1, "check_email", poste , email ).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                new Success(response);

                if (success(response)) {
                    signupSql();

                }
                else {
                    finish();
                    Toast.makeText(context, "email занят либо невалидный", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(GoogleSignInActivity.this, "Попробуйте другой email ", Toast.LENGTH_SHORT).show();

            }
        });


    }



    private void signinSql() {

        String password = googlepass;
        final String email = googleemail;
        if (email != null) {

            AppController.getApi().sign_in(1, "sign_in", poste, email, password, "google").enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    new Success(response);
                    if (success(response) && poste == 1) {
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
                    if (success(response) && poste == 0) {
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
                        Toast.makeText(GoogleSignInActivity.this, "alertdial", Toast.LENGTH_SHORT).show();
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
                    ischeck_email(googleemail);

                }
            });
        }
    }





    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.disconnect_button) {
            revokeAccess();
        }
    }
}
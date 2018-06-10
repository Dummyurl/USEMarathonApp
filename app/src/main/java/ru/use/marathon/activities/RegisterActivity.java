package ru.use.marathon.activities;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.Teacher;

import static ru.use.marathon.models.Success.success;

public class RegisterActivity extends AppCompatActivity {

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

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                AppController.getApi().sign_up(1,"sign_up",post,name,email,password).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        new Success(response);
                        if(success()){
                            if(post == 1){
                                Teacher t = new Teacher(getApplicationContext(),response);
                                t.createSession(t.getID(),name,email);
                            }else{
                                Student s = new Student(getApplicationContext(),response);
                                s.createSession(s.getID(),name,email);
                            }

                            Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(RegisterActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Big problem ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

package ru.use.marathon.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.user_id)
    TextView user_id;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.post)
    TextView post;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.logout)
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final Student student = new Student(this);
        final Teacher teacher = new Teacher(this);

        if(student.isLoggedIn() && !teacher.isLoggedIn()){

            //student
           HashMap<String,String> data = student.getData();
           user_id.setText(data.get(student.KEY_ID));
           name.setText(data.get(student.KEY_NAME));
           email.setText(data.get(student.KEY_EMAIL));
           post.setText("Student");
        }else if(!student.isLoggedIn() && teacher.isLoggedIn()) {
            //teacher

            HashMap<String,String> data = teacher.getData();
            user_id.setText(data.get(teacher.KEY_ID));
            name.setText(data.get(teacher.KEY_NAME));
            email.setText(data.get(teacher.KEY_EMAIL));
            post.setText("Teacher");

        }else{
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student.login(false);
                teacher.login(false);
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}

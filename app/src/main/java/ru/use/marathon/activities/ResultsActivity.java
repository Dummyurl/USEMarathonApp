package ru.use.marathon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.Student;

public class ResultsActivity extends AbstractActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.result_return_btn)
    Button return_btn;
    @BindView(R.id.result_score)
    TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Результат");

        Student student = new Student(this);
        HashMap<String,String> stu = student.getData();
        String all_tests = stu.get(student.KEY_TESTS_COUNTER);
        String right = stu.get(student.KEY_ANSWERS_COUNTER);
        String wrong = stu.get(student.KEY_WRONG_ANSWERS_COUNTER);
        Float time = Float.valueOf(stu.get(student.KEY_TESTS_TIME)); //todo examine error



        score.setText(right + " правильных из " + all_tests);

        student.deleteStat();

        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultsActivity.this,MainActivity.class));
                finish();
            }
        });
    }

}

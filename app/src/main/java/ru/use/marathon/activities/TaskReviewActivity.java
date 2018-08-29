package ru.use.marathon.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.Tests.TestCollection;
import ru.use.marathon.models.Tests.TestsViewModel;

public class TaskReviewActivity extends AbstractActivity {

    @BindView(R.id.text_review)
    TextView tv;

    int answer_type;
    int subject;
    int topic_id;
    double time;
    String content;
    String content_html;
    String content_image;
    List<String> answers;
    List<String> right_answers;
    List<String> user_answers;
    String ranswers,uanswers,aanswers;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_review);
        ButterKnife.bind(this);


        answer_type = getIntent().getIntExtra("answer_type",-1);
        subject = getIntent().getIntExtra("subject",-1);
        topic_id = getIntent().getIntExtra("topic_id",-1);
        time = getIntent().getDoubleExtra("time",-1.0);
        content = getIntent().getStringExtra("content");
        content_html = getIntent().getStringExtra("content_html");
        content_image = getIntent().getStringExtra("content_image");

        ranswers = getIntent().getStringExtra("right_answers");
        uanswers = getIntent().getStringExtra("user_answers");
        aanswers = getIntent().getStringExtra("answers");

         answers = convertStringToList(aanswers);
         right_answers =convertStringToList(ranswers);
         user_answers = convertStringToList(uanswers);

         String data = "TEST COLLECTION DATA " +
                 "\n ANSWERS:" + answers.toString() +
                 "\n RIGHT_ANSWERS: " + right_answers.toString() +
                 "\n USER_ANSWERS: " + user_answers +
                 "\n ANSWER_TYPE: " + answer_type +
                 "\n TIME: " + time +
                 "\n SUBJECT: " + subject +
                 "\n TOPIC ID: " + topic_id +
                 "\n CONTENT: " + content;

         tv.setText(data);

    }
}

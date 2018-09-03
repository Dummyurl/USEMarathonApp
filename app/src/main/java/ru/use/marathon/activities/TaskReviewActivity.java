package ru.use.marathon.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.models.Tests.TestCollection;
import ru.use.marathon.models.Tests.TestsViewModel;

import static ru.use.marathon.Constants.DEBUG;

public class TaskReviewActivity extends AbstractActivity {

    public static final String TAG = TaskReviewActivity.class.getSimpleName();

    @BindView(R.id.text_review)
    TextView tv;
    @BindView(R.id.review_content)
    TextView tv_content;
    @BindView(R.id.review_answer_container)
    LinearLayout container;

    int answer_type;
    int task_number;
    int subject;
    int topic_id;
    double time;
    String content;
    String content_html;
    String content_image;
    List<String> answers;
    List<String> right_answers;
    List<String> user_answers;
    String ranswers, uanswers, aanswers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_review);
        ButterKnife.bind(this);

        answer_type = getIntent().getIntExtra("answer_type", -1);
        task_number = getIntent().getIntExtra("task_num", -1);
        subject = getIntent().getIntExtra("subject", -1);
        topic_id = getIntent().getIntExtra("topic_id", -1);
        time = getIntent().getDoubleExtra("time", -1.0);
        content = getIntent().getStringExtra("content");
        content_html = getIntent().getStringExtra("content_html");
        content_image = getIntent().getStringExtra("content_image");

        ranswers = getIntent().getStringExtra("right_answers");
        uanswers = getIntent().getStringExtra("user_answers");
        aanswers = getIntent().getStringExtra("answers");

        if (DEBUG) Log.d(TAG, "onCreate: Intent Data: " +
                "\n ans_type:" + answer_type +
                "\n sbj: " + subject +
                "\n topic: " + topic_id +
                "\n time: " + time +
                "\n content: " + content +
                "\n content_image: " + content_image +
                "\n content_html: " + content_html +
                "\n ra: " + ranswers +
                "\n ua: " + uanswers +
                "\n a: " + aanswers
        );

        answers = convertStringToList(aanswers);
        right_answers = convertStringToList(ranswers);
        user_answers = convertStringToList(uanswers);

        tv.append(String.valueOf(task_number));
        try {
            getSupportActionBar().setTitle("Детали задания");
        }catch (NullPointerException e){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }tv_content.setText(content);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);

        if (answer_type == Constants.CHECK_BOX_TYPE) {

            TextView tv = new TextView(this);
            tv.setText("Ответы: \n");
            tv.setTypeface(boldTypeface);
            container.addView(tv);

            for (int i = 0; i < answers.size(); i++) {
                TextView answer = new TextView(this);
                answer.setText(answers.get(i));
                answer.setPadding(0, 2, 0, 2);

                container.addView(answer);
            }

            TextView tv1 = new TextView(this);
            tv1.setText("\nПравильные ответы: \n");
            tv1.setTypeface(boldTypeface);
            container.addView(tv1);

            for (int i = 0; i < right_answers.size(); i++) {
                TextView answer = new TextView(this);
                int index = Integer.valueOf(right_answers.get(i).trim()) - 1;
                answer.setText(answers.get(index));
                container.addView(answer);
            }

            TextView tv2 = new TextView(this);
            tv2.setText("\nВаши ответы: \n");
            tv2.setTypeface(boldTypeface);
            container.addView(tv2);

            for (int i = 0; i < user_answers.size(); i++) {
                TextView answer = new TextView(this);
                int index = Integer.valueOf(user_answers.get(i).trim());
                answer.setText(answers.get(index));
                container.addView(answer);
            }


        } else if (answer_type == Constants.RADIO_BUTTON_TYPE) {

            TextView tv = new TextView(this);
            tv.setText("Ответы: \n");
            tv.setTypeface(boldTypeface);
            container.addView(tv);

            for (int i = 0; i < answers.size(); i++) {
                TextView answer = new TextView(this);
                answer.setText(answers.get(i));
                answer.setPadding(0, 2, 0, 2);

                container.addView(answer);
            }

            TextView tv1 = new TextView(this);
            tv1.setText(" \nПравильный ответ: \n");
            tv1.setTypeface(boldTypeface);
            container.addView(tv1);

            TextView ra_tv = new TextView(this);
            int index = Integer.valueOf(right_answers.get(0).trim()) - 1;
            ra_tv.setText(answers.get(index));
            container.addView(ra_tv);

            TextView tv2 = new TextView(this);
            tv2.setText("\nВаш ответ: \n");
            tv2.setTypeface(boldTypeface);
            container.addView(tv2);

            TextView answer = new TextView(this);
            int n = Integer.valueOf(user_answers.get(0).trim());
            answer.setText(answers.get(n));
            container.addView(answer);


        } else if (answer_type == Constants.TEXT_TYPE) {
            // right answers instead of answers
            TextView tv1 = new TextView(this);
            tv1.setText("\nПравильный ответ: \n");
            tv1.setTypeface(boldTypeface);
            container.addView(tv1);

            TextView ra_tv = new TextView(this);
            ra_tv.setText(right_answers.get(0));
            container.addView(ra_tv);

            TextView tv2 = new TextView(this);
            tv2.setText("\nВаш ответ: \n");
            tv2.setTypeface(boldTypeface);
            container.addView(tv2);

            TextView answer = new TextView(this);
            answer.setText(user_answers.get(0));
            container.addView(answer);
        }


    }
}

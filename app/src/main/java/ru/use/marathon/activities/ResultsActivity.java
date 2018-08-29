package ru.use.marathon.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.models.Tests.TestCollection;
import ru.use.marathon.models.Tests.TestsViewModel;

import static ru.use.marathon.Constants.DEBUG;

public class ResultsActivity extends AbstractActivity  implements View.OnClickListener{

    public static final String TAG = ResultsActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.result_return_btn)
    Button return_btn;
    @BindView(R.id.result_score)
    TextView score;
    @BindView(R.id.result_pie)
    PieChart mChart;
    @BindView(R.id.result_table)
    TableLayout result_table;

    TestsViewModel testsViewModel;

    int rowAmount= 0;
    List<TestCollection> testCollectionList;

    List<TableRow> tableRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Результат");


        final int[] right_answers_count = {0};
        final int[] total_answers = new int[1];


        testsViewModel = ViewModelProviders.of(this).get(TestsViewModel.class);
        testsViewModel.getAllTests().observe(this, new Observer<List<TestCollection>>() {
            @Override
            public void onChanged(@Nullable List<TestCollection> testCollections) {
                testCollectionList = testCollections;
                rowAmount = testCollections.size();
                tableRows = new ArrayList<>(rowAmount);

                StringBuilder data = new StringBuilder("SavedData: \n ");

                for (int i = 0; i < testCollections.size(); i++) {
                    TestCollection tc = testCollections.get(i);
                    data.append("Content: ").append(tc.getContent()).append("\n");
                    data.append("answer_type: ").append(tc.getAnswer_type()).append("\n");
                    data.append("Answers: ").append(tc.getAnswers()).append("\n");
                    data.append("User answers: ").append(tc.getUser_answers()).append("\n");
                    data.append("Right answers: ").append(tc.getRight_answers()).append("\n");
                    data.append("\n\n");

                    if(DEBUG){
                        Log.d(TAG, "TEST COLLECTION DATA ["+i+"]: " +
                                "\n ANSWERS:" + tc.getAnswers() +
                                "\n RIGHT_ANSWERS: " + tc.getRight_answers() +
                                "\n USER_ANSWERS: " + tc.getUser_answers() +
                                "\n ANSWER_TYPE: " + tc.getAnswer_type() +
                                "\n TIME: " + tc.getTime());
                    }
                    List<String> answers = convertStringToList(tc.getAnswers());
                    List<String> right_answers = convertStringToList(tc.getRight_answers());
                    List<String> user_answers = convertStringToList(tc.getUser_answers());

                    if (isAnswerRight(tc, right_answers, user_answers)) {
                        right_answers_count[0]++;
                    }

                    total_answers[0]++;
                }
                if (DEBUG) {
//                    Log.d(TAG, "onChanged: \n" + data);
                    score.setVisibility(View.VISIBLE);
                    score.setText("");
                }

                initPie(total_answers[0] - right_answers_count[0], right_answers_count[0]);
                initTable(testCollectionList);

            }
        });


        final int[] clicked = new int[1];
        final View.OnClickListener mListener = new View.OnClickListener() {

            public void onClick(View v) {
                int id = v.getId();
                clicked[0] = id - 1000;

                for (int i = 0; i < rowAmount; i++) {
                    TestCollection tc = testCollectionList.get(i);
                    if(i == clicked[0]){
                        Intent intent = new Intent(ResultsActivity.this,TaskReviewActivity.class);
                        intent.putExtra("answer_type",tc.getAnswer_type());
                        intent.putExtra("subject",tc.getSubject());
                        intent.putExtra("time",tc.getTime());
                        intent.putExtra("topic_id",tc.getTopic_id());
                        intent.putExtra("content",tc.getContent());
                        intent.putExtra("content_html",tc.getContent_html());
                        intent.putExtra("content_image",tc.getContent_image());
                        intent.putExtra("answers",tc.getAnswers());
                        intent.putExtra("right_answers",tc.getRight_answers());
                        intent.putExtra("user_answers",tc.getUser_answers());

                        startActivity(intent);
                    }
                }

            }
        };


        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testsViewModel.deleteAll();
                startActivity(new Intent(ResultsActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private boolean isAnswerRight(TestCollection testCollection, List<String> right_answers, List<String> user_answers) {
        boolean right = false;
        if (testCollection.getAnswer_type() == Constants.CHECK_BOX_TYPE) {
            List<Integer> ra = convertStringListToIntList(right_answers);
            List<Integer> ua = convertStringListToIntList(user_answers);
            Collections.sort(ra);
            Collections.sort(ua);

            int counter = 0;
            if (ra.size() == ua.size()) {
                for (int i = 0; i < ua.size(); i++) if(ra.get(i) -1 == ua.get(i)) counter++;
                if(counter==ra.size()) right = true;
            }

        } else if (testCollection.getAnswer_type() == Constants.RADIO_BUTTON_TYPE) {
            List<Integer> ra = convertStringListToIntList(right_answers);
            List<Integer> ua = convertStringListToIntList(user_answers);
            Collections.sort(ra);
            Collections.sort(ua);

            if(ra.get(0) - 1 == ua.get(0)) right = true;

        } else if (testCollection.getAnswer_type() == Constants.TEXT_TYPE) {
            String r = right_answers.get(0);
            String u = user_answers.get(0);
            if (u.contains(r)) right = true;
        }
        return right;
    }


    private void initTable(final List<TestCollection> testCollections) {

        for (int i = 0; i < testCollections.size(); i++) {

            String right_answers[] = testCollections.get(i).getRight_answers().substring(1, testCollections.get(i).getRight_answers().length() - 1)
                    .split(",");
            String answers[] = testCollections.get(i).getAnswers().substring(1, testCollections.get(i).getAnswers().length() - 1).split(",");
            String user_answers[] = testCollections.get(i).getUser_answers().substring(1, testCollections.get(i).getUser_answers().length() - 1).split(",");

            int[] ra = new int[right_answers.length];
            int[] ua = new int[user_answers.length];

            //todo make more profitable

            if (testCollections.get(i).getAnswer_type() != Constants.TEXT_TYPE) {
                for (int j = 0; j < right_answers.length; j++) {
                    ra[j] = Integer.valueOf(right_answers[j].trim());
                }
                for (int j = 0; j < user_answers.length; j++) {
                    ua[j] = Integer.valueOf(user_answers[j].trim());
                }

                TableRow row = new TableRow(this);
                row.setPadding(10, 10, 10, 10);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(params);
                row.setId(1000 + i);
                //adding id
                TextView id_tv = new TextView(this);
                id_tv.setText(String.valueOf(testCollections.get(i).getTask_number()));
                id_tv.setTextColor(Color.BLACK);
                id_tv.setLayoutParams(params);
                id_tv.setGravity(Gravity.CENTER);
                row.addView(id_tv);

                //adding right answer

                TextView right_tv = new TextView(this);
                if (ra.length > 1) {
                    StringBuilder text = new StringBuilder();
                    for (int j = 0; j < ra.length; j++)
                        text.append(answers[ra[j] - 1]).append(";");
                    String t = ellipsize(text.toString(), 20);
                    right_tv.setText(t);
                } else if (ra.length == 1) {
                    String t = ellipsize(answers[ra[0] - 1], 20);
                    right_tv.setText(t);
                }
                right_tv.setTextColor(Color.BLACK);
                right_tv.setLayoutParams(params);
                right_tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                right_tv.setMaxLines(1);
                right_tv.setGravity(Gravity.CENTER);
                row.addView(right_tv);

                //adding user answer

                boolean pass = true;
                for (int j = 0; j < user_answers.length; j++) {
                    if (!user_answers[j].equals(""))
                        if (Integer.valueOf(user_answers[j].trim()) == -1) {
                            pass = false;
                        }
                }
                if (pass) {
                    TextView user_tv = new TextView(this);
                    if (ra.length > 1) {
                        StringBuilder text = new StringBuilder();
                        for (int j = 0; j < ua.length; j++)
                            text.append(answers[ua[j]]).append(";");
                        String t = ellipsize(text.toString(), 20);
                        user_tv.setText(t);
//                    user_tv.setText(Arrays.toString(ua));
                    } else if (ra.length == 1) {
                        String t = ellipsize(answers[ua[0]], 20);
                        user_tv.setText(t);
                    }

                    user_tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                    user_tv.setMaxLines(1);
                    if (isAnswerRight(testCollections.get(i), convertStringToList(testCollections.get(i).getRight_answers()), convertStringToList(testCollections.get(i).getUser_answers()))) {
                        user_tv.setTextColor(Color.GREEN);
                    } else {
                        user_tv.setTextColor(Color.RED);
                    }

                    user_tv.setLayoutParams(params);
                    user_tv.setGravity(Gravity.CENTER);
                    row.addView(user_tv);
                }
                result_table.addView(row);
                tableRows.add(row);

            } else {

                TableRow row = new TableRow(this);
                row.setPadding(10, 10, 10, 10);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(params);
                row.setId(1000 + i);
                //adding id
//                final int task_number = testCollections.get(i).getTask_number();
                TextView id_tv = new TextView(this);
                id_tv.setText(String.valueOf(testCollections.get(i).getTask_number()));
                id_tv.setTextColor(Color.BLACK);
                id_tv.setLayoutParams(params);
                id_tv.setGravity(Gravity.CENTER);
                row.addView(id_tv);

                //adding right answer
                TextView right_tv = new TextView(this);
                right_tv.setText(right_answers[0]);
                right_tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                right_tv.setMaxLines(1);
                right_tv.setTextColor(Color.BLACK);
                right_tv.setLayoutParams(params);
                right_tv.setGravity(Gravity.CENTER);
                row.addView(right_tv);

                //adding user answer

                TextView user_tv = new TextView(this);
                user_tv.setText(user_answers[0]);
                user_tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                user_tv.setMaxLines(1);
                user_tv.setLayoutParams(params);
                user_tv.setGravity(Gravity.CENTER);

                if (isAnswerRight(testCollections.get(i), convertStringToList(testCollections.get(i).getRight_answers()), convertStringToList(testCollections.get(i).getUser_answers()))) {
                    user_tv.setTextColor(Color.GREEN);
                } else {
                    user_tv.setTextColor(Color.RED);
                }
                row.addView(user_tv);
                result_table.addView(row);
                tableRows.add(row);
            }
        }
    }

    private void initPie(int val, int right_answers_count) {

        mChart.setDrawHoleEnabled(false);
        mChart.setRotationAngle(0);
        mChart.getDescription().setEnabled(false);

        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setRotationEnabled(true);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        if (val > 1 && right_answers_count > 1) {
            entries.add(new PieEntry((float) val, "Неверные ответы"));
            entries.add(new PieEntry((float) right_answers_count, "Верные ответы"));
        } else if (val == 1 && right_answers_count == 1) {
            entries.add(new PieEntry((float) val, "Неверный ответ"));
            entries.add(new PieEntry((float) right_answers_count, "Верный ответ"));
        }else{
            entries.add(new PieEntry((float) val, "Неверных ответов"));
            entries.add(new PieEntry((float) right_answers_count, "Верных ответов"));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setValueTextSize(24);
        dataSet.setValueTextColor(Color.WHITE);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        mChart.setData(data);
        mChart.invalidate();

        mChart.getLegend().setEnabled(false);


        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);
    }

    @Override
    protected void onStop() {
        testsViewModel.deleteAll();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        testsViewModel.deleteAll();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int clicked = view.getId();

    }
}

package ru.use.marathon.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.models.Tests.TestCollection;
import ru.use.marathon.models.Tests.TestsViewModel;

import static ru.use.marathon.Constants.DEBUG;

public class ResultsActivity extends AbstractActivity {

    public static final String TAG = ResultsActivity.class.getSimpleName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.result_return_btn)
    Button return_btn;
    @BindView(R.id.result_score)
    TextView score;
    @BindView(R.id.result_pie)
    PieChart mChart;
    @BindView(R.id.result_table)
    TableLayout result_table;

    TestsViewModel testsViewModel;

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
                StringBuilder data = new StringBuilder("SavedData: \n ");

                for (int i = 0; i < testCollections.size(); i++) {
                    TestCollection tc = testCollections.get(i);
                    data.append("Content: ").append(tc.getContent()).append("\n");
                    data.append("answer_type: ").append(tc.getAnswer_type()).append("\n");
                    data.append("Answers: ").append(tc.getAnswers()).append("\n");
                    data.append("User answers: ").append(tc.getUser_answers()).append("\n");
                    data.append("Right answers: ").append(tc.getRight_answers()).append("\n");
                    data.append("\n\n");
                    String right_answers[] = tc.getRight_answers().substring(1, tc.getRight_answers().length() - 1)
                            .split(",");
                    String answers[] = tc.getAnswers().substring(1,tc.getAnswers().length() - 1).split(",");
                    String user_answers[] = tc.getUser_answers().substring(1,tc.getUser_answers().length() - 1).split(",");

                    if(isAnswerRight(tc,right_answers,answers,user_answers)){
                        right_answers_count[0]++;
                    }

                    total_answers[0]++;
                }
                if(DEBUG){
                    Log.d(TAG, "onChanged: \n" + data);
//                    score.setVisibility(View.VISIBLE);
                    score.setText(String.valueOf(total_answers[0]) + " \n" + String.valueOf(right_answers_count[0]));
                }

                initPie(total_answers[0]-right_answers_count[0] ,right_answers_count[0]);

                initTable(testCollections);
            }
        });



        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testsViewModel.deleteAll();
                startActivity(new Intent(ResultsActivity.this,MainActivity.class));
                finish();

            }
        });
    }

    private boolean isAnswerRight(TestCollection testCollection, String[] right_answers, String[] answers, String[] user_answers) {
        boolean right = false;
        if(testCollection.getAnswer_type() == Constants.CHECK_BOX_TYPE){
            int counter = 0;
            for (String right_answer : right_answers) {
                for (String user_answer : user_answers) {
                    if (right_answer.toLowerCase().equals(user_answer.toLowerCase())) counter++;
                }
            }
            if(counter == right_answers.length)
                right = true;
        }else if(testCollection.getAnswer_type() == Constants.RADIO_BUTTON_TYPE || testCollection.getAnswer_type() == Constants.TEXT_TYPE){
            if(right_answers[0].toLowerCase().equals(user_answers[0].toLowerCase())){
                right = true;
            }
        }
        return right;
    }


    private void initTable( List<TestCollection> testCollections) {

        for (int i = 0; i < testCollections.size(); i++) {

            String right_answers[] = testCollections.get(i).getRight_answers().substring(1, testCollections.get(i).getRight_answers().length() - 1)
                    .split(",");
            String answers[] = testCollections.get(i).getAnswers().substring(1,testCollections.get(i).getAnswers().length() - 1).split(",");
            String user_answers[] = testCollections.get(i).getUser_answers().substring(1,testCollections.get(i).getUser_answers().length() - 1).split(",");

            int[] ra = new int[right_answers.length];
            int[] ua = new int[user_answers.length];

            //todo make more profitable

            if(testCollections.get(i).getAnswer_type() != Constants.TEXT_TYPE){
                for (int j = 0; j < right_answers.length; j++) {
                    ra[j] = Integer.valueOf(right_answers[j].trim());
                }
                for (int j = 0; j < user_answers.length; j++){
                    ua[j] = Integer.valueOf(user_answers[j].trim());
                }


                TableRow row = new TableRow(this);
                row.setPadding(5,5,5,5);
                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                //adding id
//                row.setGravity(Gravity.CENTER);
                final int task_num = testCollections.get(i).getTask_number();
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ResultsActivity.this, "clicked task number " +  String.valueOf(task_num), Toast.LENGTH_SHORT).show();
                    }
                });
                TextView id_tv = new TextView(this);
                id_tv.setText(String.valueOf(testCollections.get(i).getTask_number()));
                id_tv.setTextColor(Color.BLACK);
                row.addView(id_tv);


                //adding right answer

                TextView right_tv = new TextView(this);
                if(ra.length > 1){
                    StringBuilder text = new StringBuilder();
                    for (int j = 0; j < ra.length; j++)
                        text.append(answers[ra[j]-1]).append(";");
                    right_tv.setText(text);
                }else if(ra.length == 1) right_tv.setText(answers[ra[0]]);
                right_tv.setTextColor(Color.BLACK);
                row.addView(right_tv);

                //adding user answer

                TextView user_tv = new TextView(this);
                if(ra.length > 1){
                    StringBuilder text = new StringBuilder();
                    for (int j = 0; j < ua.length; j++)
                        text.append(answers[ua[j]]).append(";");
                    user_tv.setText(text);
                }else if(ra.length == 1)
                    user_tv.setText(answers[ua[0]]);

                if(isAnswerRight(testCollections.get(i),right_answers,answers,user_answers)){
                    user_tv.setTextColor(Color.GREEN);
                }else{
                    user_tv.setTextColor(Color.RED);
                }
                row.addView(user_tv);

                result_table.addView(row);


            }else{

                TableRow row = new TableRow(this);
                row.setPadding(5,5,5,5);
                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                //adding id
                final int task_num = testCollections.get(i).getTask_number();
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ResultsActivity.this, "clicked task number " +  String.valueOf(task_num), Toast.LENGTH_SHORT).show();
                    }
                });
                TextView id_tv = new TextView(this);
                id_tv.setText(String.valueOf(testCollections.get(i).getTask_number()));
                id_tv.setTextColor(Color.BLACK);
                row.addView(id_tv);

                //adding right answer
                TextView right_tv = new TextView(this);
                right_tv.setText(right_answers[0]);
                right_tv.setEllipsize(TextUtils.TruncateAt.END);
                right_tv.setMaxLines(1);
                right_tv.setTextColor(Color.BLACK);
                row.addView(right_tv);

                //adding user answer

                TextView user_tv = new TextView(this);
                user_tv.setText(user_answers[0]);
                user_tv.setEllipsize(TextUtils.TruncateAt.END);
                user_tv.setMaxLines(1);
                if(isAnswerRight(testCollections.get(i),right_answers,answers,user_answers)){
                    user_tv.setTextColor(Color.GREEN);
                }else{
                    user_tv.setTextColor(Color.RED);
                }
                row.addView(user_tv);

                result_table.addView(row);

            }


        }

    }

    private void initPie(int val,int right_answers_count) {

        mChart.setDrawHoleEnabled(false);
        mChart.setRotationAngle(0);
        mChart.getDescription().setEnabled(false);

        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setRotationEnabled(true);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry((float) val,"Wrong answers"));
        entries.add(new PieEntry((float) right_answers_count, "Right answers"));
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
}

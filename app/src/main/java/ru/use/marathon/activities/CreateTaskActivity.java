package ru.use.marathon.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.adapters.tasks_creation.TaskAnswersAdapter;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.TaskCreation.QUResponse;
import ru.use.marathon.models.TaskCreation.TaskAnswer;
import ru.use.marathon.models.topics.Topics;

import static ru.use.marathon.models.Success.success;

public class CreateTaskActivity extends AbstractActivity {
    public static final String TAG = CreateTaskActivity.class.getSimpleName();
    //QU PART
    @BindView(R.id.task_content_et) EditText content_et;
    @BindView(R.id.task_numbers) Spinner task_numbers_spinner;
    @BindView(R.id.tasks_topics_spinner) Spinner task_topics_spinner;
    @BindView(R.id.task_answer_type) Spinner task_answer_type_spinner;
    @BindView(R.id.goto_answers_btn) Button goto_answers;

    //Radio and Checkbox buttons
    @BindView(R.id.rc_layout) LinearLayout rc_layout;
    @BindView(R.id.tasks_answer_et) EditText answers_et;
    @BindView(R.id.add_answer_btn) Button add_answer;
    @BindView(R.id.answer_container) RecyclerView answersRecyclerView;

    //Right answers
    @BindView(R.id.ra_layout) LinearLayout ra_layout;
    @BindView(R.id.task_ra_et) EditText right_answer_et;
    @BindView(R.id.add_ra_btn) Button add_right_answer;
    @BindView(R.id.ra_rv) RecyclerView rightAnswerRecyclerView;

    //save
    @BindView(R.id.task_save_btn) Button save;

    //task numbers
    int[] task_numbers;
    int server_task_number;

    //task topics
    String[] topics_content;
    int server_topic_id;

    //answer type
    int server_answer_type;

    //server QU_ID
    int qu_id;

    boolean btn_trigger = false;

    //answers
    TaskAnswersAdapter adapter;
    List<TaskAnswer> answerList;

    //right_answer
    TaskAnswersAdapter adapter1;
    List<TaskAnswer> rightAnswerList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);



        initTaskNumbers();
        initTaskTopics();
        initAnswerType();

        initAnswersBtn();
        initAnswersSection();

        initRA();
        initSaveBtn();

    }

    private void initSaveBtn() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(answerList.size() < 1 && rightAnswerList.size() < 1 && !btn_trigger){
                    Toast.makeText(CreateTaskActivity.this, "Введите поля ответов", Toast.LENGTH_SHORT).show();
                }else{
                    ProgressDialog d = new ProgressDialog(CreateTaskActivity.this);
                    d.setMessage("Ответы загружаются..");
                    d.setProgress(0);
                    d.show();

                    for (int i = 0; i < answerList.size(); i++) {
                        final int finalI = i;
                        AppController.getApi().add_qu_ans(1,"add_qu_ans",qu_id,answerList.get(i).getContent()).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                if(success(response)) Log.e(TAG, "onResponse Answers Queue: [" + String.valueOf(finalI) + "]" + (success(response)?" ok":" not ok"));
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                    }

                    for (int i = 0; i < rightAnswerList.size(); i++) {
                        final int finalI = i;
                        AppController.getApi().add_qu_right_ans(1,"add_qu_right_ans",qu_id,rightAnswerList.get(i).getContent()).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                new Success(response);
                                if(success(response)) Log.e(TAG, "onResponse Right Answers Queue: [" + String.valueOf(finalI) + "]" + (success(response)?" ok":" not ok"));
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                    }
                    d.dismiss();


                }

                AlertDialog.Builder b = new AlertDialog.Builder(CreateTaskActivity.this);
                b.setTitle("Создать еще?");
                b.setMessage("");
                b.setPositiveButton("Да!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        content_et.setText("");
                        rightAnswerList.clear();
                        answerList.clear();
                        adapter.notifyDataSetChanged();
                        adapter1.notifyDataSetChanged();
                        rc_layout.setVisibility(View.GONE);
                        ra_layout.setVisibility(View.GONE);
                        btn_trigger = false;
                        dialogInterface.dismiss();
                    }
                });
                b.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = b.create();
                dialog.show();
            }
        });
    }

    private void initRA() {
        rightAnswerList = new ArrayList<>();
        rightAnswerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new TaskAnswersAdapter(rightAnswerList,this);
        rightAnswerRecyclerView.setAdapter(adapter1);
        add_right_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = right_answer_et.getText().toString().trim();
                rightAnswerList.add(new TaskAnswer(text));
                adapter1.notifyDataSetChanged();
            }
        });
    }

    private void initAnswersSection() {
        answerList = new ArrayList<>();
        answersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAnswersAdapter(answerList,this);
        answersRecyclerView.setAdapter(adapter);

        add_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = answers_et.getText().toString().trim();
                answerList.add(new TaskAnswer(answer));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initAnswersBtn() {
        rc_layout.setVisibility(View.GONE);
        ra_layout.setVisibility(View.GONE);

        goto_answers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_trigger = true;
                AlertDialog.Builder b = new AlertDialog.Builder(CreateTaskActivity.this);
                b.setTitle("Подтвердить действие");
                b.setMessage("После вы больше не сможете изменить содержимое, тему и тип ответов задания.");
                b.setPositiveButton("продолжить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        initQUCreation();

                        if ((server_answer_type == Constants.RADIO_BUTTON_TYPE) || (server_answer_type == Constants.CHECK_BOX_TYPE)) {
                            rc_layout.setVisibility(View.VISIBLE);
                            ra_layout.setVisibility(View.VISIBLE);
                        }else{
                            rc_layout.setVisibility(View.GONE);
                            answerList.clear();
                            answerList.add(new TaskAnswer("n"));
                            ra_layout.setVisibility(View.VISIBLE);
                        }

                    }
                });
                b.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = b.create();
                dialog.show();
            }
        });
    }
    private void initQUCreation() {
        final String content = content_et.getText().toString().trim();

        AppController.getApi().create_qu(1,"create_qu","Русский",server_topic_id,server_task_number,server_answer_type).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                QUResponse quResponse = new QUResponse(response);
                qu_id = quResponse.qu_id();
                if(qu_id  != -1){
                    //ok
                    AppController.getApi().add_qu_content(1,"add_qu_content",qu_id,content).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            new Success(response);
                            if(success(response))
                                Toast.makeText(CreateTaskActivity.this, "oh yeah", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                }else{
                    //not ok
                    Toast.makeText(CreateTaskActivity.this, "проблемы с созданием скелета задания.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void initAnswerType() {
        String[] answer_type = new String[]{"Выбор единственного ответа среди возможных", "Выбор нескольких ответов среди возможных","Ввод ответа вручную"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, answer_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        task_answer_type_spinner.setAdapter(adapter);
        task_answer_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                server_answer_type = i;
                Toast.makeText(CreateTaskActivity.this, "pos:" + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void initTaskTopics() {
        AppController.getApi().get_topics(1,"get_topics",subject()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final Topics topics = new Topics(response);
                topics_content = new String[topics.size()];
                for (int i = 0; i < topics.size(); i++) {
                    topics_content[i] = topics.getContent(i);
                }
                Log.d(TAG, "initTaskTopics: " + Arrays.toString(topics_content));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateTaskActivity.this, android.R.layout.simple_spinner_item, topics_content);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                task_topics_spinner.setAdapter(adapter);
                task_topics_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(CreateTaskActivity.this, "Selected: " + i +"\n id:" +topics.getID(i), Toast.LENGTH_SHORT).show();
                        server_topic_id = topics.getID(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CreateTaskActivity.this, "Ошибка! Попробуйте зайти позже", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initTaskNumbers() {
        task_numbers = new int[26];
        for (int i = 0; i < task_numbers.length; i++) {
            task_numbers[i] = i+1;
        }
        Log.d(TAG, "initTaskNumbers: " + Arrays.toString(task_numbers));

        String[] tn = new String[26];
        for (int i = 0; i < tn.length; i++) {
            tn[i] = String.valueOf(task_numbers[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tn);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        task_numbers_spinner.setAdapter(adapter);

        task_numbers_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                server_task_number = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



}

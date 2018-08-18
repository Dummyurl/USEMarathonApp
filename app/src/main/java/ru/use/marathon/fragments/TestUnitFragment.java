package ru.use.marathon.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.activities.FeedContentActivity;
import ru.use.marathon.activities.ResultsActivity;
import ru.use.marathon.activities.TestsActivity;
import ru.use.marathon.models.Collection;
import ru.use.marathon.models.Collections;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.answers.AbstractAnswer;
import ru.use.marathon.models.answers.StudentAnswers;

import static ru.use.marathon.models.Success.success;


public class TestUnitFragment extends AbstractFragment {

    public static final String TAG = TestUnitFragment.class.getSimpleName();
    Collection collection;
    int page, max_page,cn;
    int answer_type;
    Student student;

    @BindView(R.id.content_text)
    TextView content_tv;

    @BindView(R.id.content_image)
    ImageView content_image;

    @BindView(R.id.content_html)
    WebView content_web;

    @BindView(R.id.answers_radio_group)
    RadioGroup rg_container;
    @BindView(R.id.show_hint_btn)
    Button show_hint_btn;
    @BindView(R.id.hint_txt)
    TextView hint_txt;

    @BindView(R.id.answers_container)
    LinearLayout answers_container;

    @BindView(R.id.save_answer_btn)
    Button save_ans_btn;

    //ANSWERS SECTION
    List<String> answers;
    RadioButton[] rg_answers;
    CheckBox[] cb_answers;
    EditText enter_answer_et;
    StudentAnswers studentAnswers;
    AbstractAnswer abstractAnswer;
    ViewPager parentView;

    //STOPWATCH
    @BindView(R.id.stopwatch)
    TextView stopwatch_txt;
    private int seconds = 0;
    private boolean startRun;


    public TestUnitFragment() {
    }

    public static TestUnitFragment newInstance(int page, int pages_amount, int cn) {
        TestUnitFragment fragment = new TestUnitFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putInt("max_page", pages_amount);
        args.putInt("cn",cn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        page = getArguments().getInt("page");
        max_page = getArguments().getInt("max_page");
        cn = getArguments().getInt("cn");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tests_activity, container, false);
        ButterKnife.bind(this, view);

        student = new Student(getActivity().getApplicationContext());

        parentView = (ViewPager) getActivity().findViewById(R.id.tests_viewpager_container);
        Collections collections = new Collections(getActivity().getApplicationContext());
        collection = collections.getCollection();

        startRun = true;

        studentAnswers = new StudentAnswers(getActivity().getApplicationContext());

        initHint();
        setupUI();
        initStopwatch();


        save_ans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abstractAnswer = new AbstractAnswer(getActivity().getApplicationContext());
                abstractAnswer.savePage(page);

                Student student = new Student(getActivity().getApplicationContext());
                HashMap<String, String> sdata = student.getStatistics();

                boolean isCorrectAnswer = false;

                List<String> ra = collection.getRightAnswers(page);

                if(ra.get(0).equals("")){
                    if (parentView.getCurrentItem() == max_page - 1) {
                        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                        b.setTitle("Завершить тест?");
                        b.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent i1 = new Intent(getActivity(),ResultsActivity.class);
                                startActivity(i1);
                                getActivity().finish();
                                dialogInterface.dismiss();
                            }
                        });
                        b.setCancelable(false);
                        AlertDialog d = b.create();
                        d.show();
                    } else {
                        parentView.setCurrentItem(parentView.getCurrentItem() + 1);

                    }
                    return;
                }else{



                    if (answer_type == Constants.RADIO_BUTTON_TYPE) {

                        RadioButton radioButton = (RadioButton) rg_container.findViewById(rg_container.getCheckedRadioButtonId());
                        int i = rg_container.indexOfChild(radioButton);
                        Log.i("RADIO_BTN_INDEX", String.valueOf(i));
                        Log.i("RA_INDEX", String.valueOf(ra.get(0)));

                        int rai = Integer.valueOf(ra.get(0));
                        int[] ram = new int[answers.size()];
                        for (int j = 0; j < ram.length; j++) ram[j] = 0;
                        for (int j = 0; j < ram.length; j++) {
                            if (j == rai - 1) ram[j] = 1;
                            if (j == i) ram[j] = -1;
                        }
                        abstractAnswer.saveRadioBtn(ram);

                        Log.i("RADIO_BTN_ARRAY", Arrays.toString(ram));


                        HashMap<String, String> stats = student.getStatistics();
                        int tests_counter = Integer.parseInt(stats.get(student.KEY_TESTS_COUNTER));
                        int answer_counter = Integer.parseInt(stats.get(student.KEY_ANSWERS_COUNTER));
                        int answer_wrong_counter = Integer.parseInt(stats.get(student.KEY_WRONG_ANSWERS_COUNTER));
                        double seconds_stat = Double.parseDouble(stats.get(student.KEY_TESTS_TIME));

                        if (abstractAnswer.isRight()) {
                            isCorrectAnswer = true;
                            sendSolved();
                            student.setStatistics(tests_counter + 1, seconds_stat + seconds, answer_counter + 1, answer_wrong_counter);
                        } else {
                            isCorrectAnswer = false;
                            Toast.makeText(getActivity().getApplicationContext(), "не правильно!", Toast.LENGTH_SHORT).show();
                            student.setStatistics(tests_counter + 1, seconds_stat + seconds, answer_counter, answer_wrong_counter + 1);
                        }

                    } else if (answer_type == Constants.TEXT_TYPE) {

                        abstractAnswer.saveET(ra.get(0), enter_answer_et.getText().toString());

                        HashMap<String, String> stats = student.getStatistics();
                        int tests_counter = Integer.parseInt(stats.get(student.KEY_TESTS_COUNTER));
                        int answer_counter = Integer.parseInt(stats.get(student.KEY_ANSWERS_COUNTER));
                        int answer_wrong_counter = Integer.parseInt(stats.get(student.KEY_WRONG_ANSWERS_COUNTER));
                        double seconds_stat = Double.parseDouble(stats.get(student.KEY_TESTS_TIME));
                        if (abstractAnswer.isRight()) {
                            isCorrectAnswer = true;
                            sendSolved();
                            student.setStatistics(tests_counter + 1, seconds_stat+seconds, answer_counter + 1, answer_wrong_counter);
                        } else {
                            isCorrectAnswer = false;
                            student.setStatistics(tests_counter + 1, seconds_stat+seconds, answer_counter, answer_wrong_counter + 1);
                        }
                    } else if (answer_type == Constants.CHECK_BOX_TYPE) {
                        int count = answers_container.getChildCount();
                        View v = null;
                        List<Integer> uam = new ArrayList<>();
                        List<Integer> ram = new ArrayList<>();

                        for (int i = 0; i < count; i++) {
                            v = answers_container.getChildAt(i);
                            CheckBox cb = (CheckBox) v;
                            if (cb.isChecked()) uam.add(i);
                        }
                        for (int i = 0; i < ra.size(); i++) ram.add(Integer.valueOf(ra.get(i)) - 1);

                        abstractAnswer.saveCB(ram, uam);

                        HashMap<String, String> stats = student.getStatistics();
                        int tests_counter = Integer.parseInt(stats.get(student.KEY_TESTS_COUNTER));
                        int answer_counter = Integer.parseInt(stats.get(student.KEY_ANSWERS_COUNTER));
                        int answer_wrong_counter = Integer.parseInt(stats.get(student.KEY_WRONG_ANSWERS_COUNTER));
                        double seconds_stat = Double.parseDouble(stats.get(student.KEY_TESTS_TIME));

                        if (abstractAnswer.isRight()) {
                            isCorrectAnswer = true;
                            sendSolved();
                            student.setStatistics(tests_counter + 1, seconds_stat+seconds, answer_counter + 1, answer_wrong_counter);
                        } else {
                            isCorrectAnswer = false;
                            student.setStatistics(tests_counter + 1, seconds_stat+seconds, answer_counter, answer_wrong_counter + 1);
                        }
                    }
                    sendStatToServer(isCorrectAnswer);
                    startRun = false;
                    seconds = 0;


                    if (parentView.getCurrentItem() == max_page - 1) {
                        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                        b.setTitle("Завершить тест?");
                        b.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent i1 = new Intent(getActivity(),ResultsActivity.class);
                                startActivity(i1);
                                getActivity().finish();
                                dialogInterface.dismiss();
                            }
                        });
                        b.setCancelable(false);
                        AlertDialog d = b.create();
                        d.show();
                    } else {
                        parentView.setCurrentItem(parentView.getCurrentItem() + 1);

                    }

            }

            }
        });

        return view;
    }


    private void sendSolved(){
        AppController.getApi().setSolvedTopic(1,"setSolvedTopic",collection.getSubject(page),user_id(),collection.getTopic(page))
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        new Success(response);
                        if(success()){
                            Toast.makeText(getActivity(), "topic_solved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }

    private void sendStatToServer(boolean isCorrectAnswer) {
        HashMap<String,String> user_data = student.getData();
        int id = Integer.parseInt(user_data.get(student.KEY_ID));
        int c = isCorrectAnswer? 1 : 0;
        AppController.getApi().sendStat(1,"setStats",subject(),id,collection.getId(page),seconds,c,0).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



    }

    private void initStopwatch() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format("%02d:%02d", minutes, secs);

                stopwatch_txt.setText(time);

                if (startRun) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });

    }

    private void setupUI() {

        content_web.setWebViewClient(new MyBrowser());
        content_web.getSettings().setLoadsImagesAutomatically(true);
        content_web.getSettings().setDomStorageEnabled(true);
        content_web.getSettings().setJavaScriptEnabled(true);

        if (collection != null) {
            ((TestsActivity) getActivity())
                    .setActionBarTitle("Задание № " + collection.getTaskNumber(page))
            ;

            if(!collection.getContent(page).equals("")){
                content_tv.setVisibility(View.VISIBLE);
                content_tv.setText(collection.getContent(page));
            }
            if(!collection.getContentImage(page).equals("")){
                content_image.setVisibility(View.VISIBLE);
                Picasso.get().load(collection.getContentImage(page)).into(content_image);
            }
            if(!collection.getContentHtml(page).equals("")){
                content_web.setVisibility(View.VISIBLE);
                content_web.loadUrl(collection.getContentHtml(page));
            }

            answers = collection.getAnswers(page);
            answer_type = collection.getAnswerType(page);


            if(!answers.get(0).equals("")){


                if (answer_type == Constants.RADIO_BUTTON_TYPE) {

                    answers_container.setVisibility(View.GONE);
                    rg_answers = new RadioButton[answers.size()];
                    for (int i = 0; i < answers.size(); i++) {
                        rg_answers[i] = new RadioButton(getActivity().getApplicationContext());
                        rg_answers[i].setTextColor(Color.BLACK);
                        //todo correct radiobtn
                        rg_answers[i].setText(answers.get(i));
                        rg_container.addView(rg_answers[i]);
                    }

                } else if (answer_type == Constants.CHECK_BOX_TYPE) {

                    rg_container.setVisibility(View.GONE);
                    cb_answers = new CheckBox[answers.size()];
                    for (int i = 0; i < answers.size(); i++) {
                        cb_answers[i] = new CheckBox(getActivity().getApplicationContext());
                        cb_answers[i].setTextColor(Color.BLACK);
                        cb_answers[i].setText(answers.get(i));
                        answers_container.addView(cb_answers[i]);
                    }

                } else if (answer_type == Constants.TEXT_TYPE) {

                    rg_container.setVisibility(View.GONE);
                    enter_answer_et = new EditText(getActivity().getApplicationContext());
                    enter_answer_et.setTextColor(Color.BLACK);
                    enter_answer_et.setHint("Введите ответ");
                    answers_container.addView(enter_answer_et);
                }
            }else{
                save_ans_btn.setVisibility(View.GONE);
                show_hint_btn.setVisibility(View.GONE);
                stopwatch_txt.setVisibility(View.GONE);

                if(page == max_page - 1){
                    save_ans_btn.setVisibility(View.VISIBLE);
                    save_ans_btn.setText("Завершить");

                }
            }
        }

    }

    private void initHint() {
        final boolean[] flag = {false};
        show_hint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hint_txt.setText("Не доступно.");
                if (!flag[0]) {
                    hint_txt.setVisibility(View.VISIBLE);
                    flag[0] = true;
                } else {
                    hint_txt.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    public interface OnDataPass {
        public void onDataPass(AbstractAnswer data, int page);
    }

    OnDataPass dataPasser;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("abstract_answer", abstractAnswer);
        outState.putString("a", "a");
        Log.d("TestsFragment", "onSaveInstanceState " + page);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(AbstractAnswer data, int page) {
        dataPasser.onDataPass(data, page);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TestsFragment", "onPause " + page);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TestsFragment", "onDestroyView " + page);

        if (abstractAnswer != null) {
            passData(abstractAnswer, page);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("TestsFragment", "onStart " + page);
    }

    @Override
    public void onResume() {
        super.onResume();


        Log.d("TestsFragment", "onResume " + page);
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

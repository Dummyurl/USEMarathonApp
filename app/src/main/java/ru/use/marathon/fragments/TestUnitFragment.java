package ru.use.marathon.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.activities.ResultsActivity;
import ru.use.marathon.activities.TestsActivity;
import ru.use.marathon.models.Collection;
import ru.use.marathon.models.Collections;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Success;
import ru.use.marathon.models.Tests.TestCollection;
import ru.use.marathon.models.Tests.TestFragmentModel;
import ru.use.marathon.models.Tests.TestsViewModel;
import ru.use.marathon.utils.CounterClass;
import ru.use.marathon.utils.Stopwatch;

import static ru.use.marathon.Constants.DEBUG;
import static ru.use.marathon.models.Success.success;


public class TestUnitFragment extends AbstractFragment {

    public static final String TAG = TestUnitFragment.class.getSimpleName();
    Collection collection;
    int page, max_page, cn;
    int answer_type;
    Student student;
    TestFragmentModel testsModel;

    TestsViewModel testsViewModel;
    TestCollection.TestCollectionBuilder testsBuilder;

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
        args.putInt("cn", cn);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            timeStart();
        } else {
            timeStop();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tests_activity, container, false);
        ButterKnife.bind(this, view);
        testsViewModel = ViewModelProviders.of(getActivity()).get(TestsViewModel.class);
        testsModel = new TestFragmentModel();

        student = new Student(getActivity().getApplicationContext());

        parentView = getActivity().findViewById(R.id.tests_viewpager_container);
        Collections collections = new Collections(getActivity().getApplicationContext());
        collection = collections.getCollection();


        initHint();
        setupUI();
        initStopwatch();


        save_ans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSaveAnswer();
            }
        });

        return view;
    }

    private void initSaveAnswer() {
        testsModel.updateAnswer(page, true);

        boolean isCorrectAnswer = false;
        List<String> ra = collection.getRightAnswers(page);

        if (ra.get(0).equals("")) {

            if (parentView.getCurrentItem() == max_page - 1) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Завершить тест?");
                b.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent i1 = new Intent(getActivity(), ResultsActivity.class);
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
        } else {

            if (answer_type == Constants.RADIO_BUTTON_TYPE) {

                RadioButton radioButton = rg_container.findViewById(rg_container.getCheckedRadioButtonId());
                int i = rg_container.indexOfChild(radioButton); // i - user answer

                List<String> uanswer = new ArrayList<>();
                uanswer.add(String.valueOf(i));
                testsBuilder.userAnswers(uanswer);

                if (DEBUG) Log.i("RADIO_BTN_INDEX", String.valueOf(i));
                if (DEBUG) Log.i("RA_INDEX", String.valueOf(ra.get(0)));

                int right_answer = Integer.valueOf(String.valueOf(collection.getRightAnswers(page).get(0)));

                if (right_answer - 1 == i) {
                    isCorrectAnswer = true;
                    if (DEBUG)
                        Toast.makeText(getActivity().getApplicationContext(), "правильно!", Toast.LENGTH_SHORT).show();
                    if (!DEBUG) sendSolved();
                } else {
                    isCorrectAnswer = false;
                    if (DEBUG)
                        Toast.makeText(getActivity().getApplicationContext(), "не правильно!", Toast.LENGTH_SHORT).show();
                }

            } else if (answer_type == Constants.TEXT_TYPE) {

                List<String> uanswer = new ArrayList<>();
                uanswer.add(enter_answer_et.getText().toString());
                testsBuilder.userAnswers(uanswer);

                if (enter_answer_et.getText().toString().toLowerCase().equals(collection.getRightAnswers(page).get(0))) {
                    if (DEBUG)
                        Toast.makeText(getActivity().getApplicationContext(), "правильно!", Toast.LENGTH_SHORT).show();
                    isCorrectAnswer = true;
                    if (!DEBUG) sendSolved();
                } else {
                    if (DEBUG)
                        Toast.makeText(getActivity().getApplicationContext(), "не правильно!", Toast.LENGTH_SHORT).show();
                    isCorrectAnswer = false;
                }
            } else if (answer_type == Constants.CHECK_BOX_TYPE) {

                int count = answers_container.getChildCount();
                View v = null;
                List<Integer> uam = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    v = answers_container.getChildAt(i);
                    CheckBox cb = (CheckBox) v;
                    if (cb.isChecked()) uam.add(i);
                }

                List<String> uanswers = new ArrayList<>(uam.size());
                for (Integer item : uam) {
                    uanswers.add(String.valueOf(item));
                }
                testsBuilder.userAnswers(uanswers);

                if (isCheckBoxAnswerRight(collection.getRightAnswers(page), uam)) {
                    isCorrectAnswer = true;
                    if (!DEBUG) sendSolved();
                    if (DEBUG)
                        Toast.makeText(getActivity().getApplicationContext(), "правильно!", Toast.LENGTH_SHORT).show();

                } else {
                    if (DEBUG)
                        Toast.makeText(getActivity().getApplicationContext(), "не правильно!", Toast.LENGTH_SHORT).show();
                    isCorrectAnswer = false;
                }
            }
            testsBuilder.time(seconds);

            testsBuilder.build();
            TestCollection collection = testsBuilder.build();
            testsViewModel.insert(collection);

            if (!DEBUG) sendStatToServer(isCorrectAnswer);

            if (parentView.getCurrentItem() == max_page - 1) {

                for (int i = 0; i < testsModel.answeredSize(); i++) {
                    if (testsModel.isAnswered(page)) {
                        if (DEBUG) Log.d(TAG, "this page is answered");
                    } else {
                        if (DEBUG) Log.d(TAG, "this page is NOT answered");
                    }
                }

                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Завершить тест?");
                b.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent i1 = new Intent(getActivity(), ResultsActivity.class);
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

                for (int i = 0; i < testsModel.answeredSize(); i++) {
                    if (testsModel.isAnswered(page)) {
                        if (DEBUG) Log.d(TAG, "this page is answered");
                    } else {
                        if (DEBUG) Log.d(TAG, "this page is NOT answered");
                    }
                }
            }

            timeReset();

        }


    }

    public boolean isCheckBoxAnswerRight(List<String> right_answers, List<Integer> answers) {
        if (right_answers.size() == answers.size()) {
            int c = 0, res = 0;
            for (int i = 0; i < answers.size(); i++) {
                if (Objects.equals(Integer.valueOf(right_answers.get(i)), answers.get(i))) c++;
                res = c == answers.size() ? 1 : 0;
            }
            return res == 1;
        }
        return false;
    }

    private void sendSolved() {
        AppController.getApi().setSolvedTopic(1, "setSolvedTopic", collection.getSubject(page), user_id(), String.valueOf(collection.getTopic(page)))
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        new Success(response);
                        if (success()) {
                            if (DEBUG)
                                Toast.makeText(getActivity(), "topic_solved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }

    private void sendStatToServer(boolean isCorrectAnswer) {
        HashMap<String, String> user_data = student.getData();
        int id = Integer.parseInt(user_data.get(Student.KEY_ID));
        int c = isCorrectAnswer ? 1 : 0;
        AppController.getApi().sendStat(1, "setStats", subject(), id, collection.getId(page), seconds, c, collection.getTopic(page)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void initStopwatch() {

        if(!DEBUG) stopwatch_txt.setVisibility(View.GONE);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format("%d:%02d:%02d", hours, minutes, secs);

                stopwatch_txt.setText(time);

                if (startRun) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void timeStart() {
        startRun = true;
    }

    private void timeStop() {
        startRun = false;
    }

    private void timeReset() {
        startRun = false;
        seconds = 0;
    }


    private void setupUI() {

        content_web.setWebViewClient(new MyBrowser());
        content_web.getSettings().setLoadsImagesAutomatically(true);
        content_web.getSettings().setDomStorageEnabled(true);
        content_web.getSettings().setJavaScriptEnabled(true);

        testsBuilder = new TestCollection.TestCollectionBuilder();

        if (collection != null) {
            ((TestsActivity) getActivity())
                    .setActionBarTitle("Задание № " + collection.getTaskNumber(page))
            ;
            testsBuilder.taskNumber(collection.getTaskNumber(page));
            testsBuilder.topicId(collection.getTopic(page));
            testsBuilder.rightAnswers(collection.getRightAnswers(page));
            testsBuilder.subject(subject());

            testsModel.setAnswer(page, false);

            if (!collection.getContent(page).equals("")) {
                content_tv.setVisibility(View.VISIBLE);
                content_tv.setText(collection.getContent(page));
                testsBuilder.content(collection.getContent(page));
            }
            if (!collection.getContentImage(page).equals("")) {
                content_image.setVisibility(View.VISIBLE);
                Picasso.get().load(collection.getContentImage(page)).into(content_image);
                testsBuilder.contentImage(collection.getContentImage(page));
            }
            if (!collection.getContentHtml(page).equals("")) {
                content_web.setVisibility(View.VISIBLE);
                content_web.loadUrl(collection.getContentHtml(page));
                testsBuilder.contentHtml(collection.getContentHtml(page));

            }

            answers = collection.getAnswers(page);
            testsBuilder.answers(collection.getAnswers(page));
            answer_type = collection.getAnswerType(page);
            testsBuilder.answerType(collection.getAnswerType(page));

            if (DEBUG)
                Toast.makeText(getActivity(), "Right answers: \n" + collection.getRightAnswers(page).toString(), Toast.LENGTH_SHORT).show();

            if (!answers.get(0).equals("")) {


                if (answer_type == Constants.RADIO_BUTTON_TYPE) {

                    answers_container.setVisibility(View.GONE);
                    rg_answers = new RadioButton[answers.size()];
                    for (int i = 0; i < answers.size(); i++) {
                        rg_answers[i] = (RadioButton) View.inflate(getActivity().getApplicationContext(), R.layout.factory_radio_button, null);
                        rg_answers[i].setTextColor(Color.BLACK);
                        rg_answers[i].setText(answers.get(i));
                        rg_answers[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                testsModel.updateAnswer(page, b);
                            }
                        });
                        rg_container.addView(rg_answers[i]);
                    }

                } else if (answer_type == Constants.CHECK_BOX_TYPE) {

                    rg_container.setVisibility(View.GONE);
                    cb_answers = new CheckBox[answers.size()];
                    for (int i = 0; i < answers.size(); i++) {
                        cb_answers[i] = (CheckBox) View.inflate(getActivity().getApplicationContext(), R.layout.factory_check_box, null);
                        cb_answers[i].setTextColor(Color.BLACK);
                        cb_answers[i].setText(answers.get(i));
                        cb_answers[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                testsModel.updateAnswer(page, b);
                            }
                        });
                        answers_container.addView(cb_answers[i]);
                    }

                } else if (answer_type == Constants.TEXT_TYPE) {

                    rg_container.setVisibility(View.GONE);
                    enter_answer_et = (EditText) View.inflate(getActivity().getApplicationContext(), R.layout.factory_edit_text, null);
                    enter_answer_et.setTextColor(Color.BLACK);
                    enter_answer_et.setHint("Введите ответ");
                    answers_container.addView(enter_answer_et);
                    enter_answer_et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            testsModel.updateAnswer(page, true);
                            testsBuilder.userAnswersText(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }
            } else {
                save_ans_btn.setVisibility(View.GONE);
                show_hint_btn.setVisibility(View.GONE);
                stopwatch_txt.setVisibility(View.GONE);

                if (page == max_page - 1) {
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

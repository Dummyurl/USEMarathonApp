package ru.use.marathon.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.models.Collection;
import ru.use.marathon.models.Collections;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.answers.AbstractAnswer;
import ru.use.marathon.models.answers.StudentAnswers;


public class TestsActivityFragment extends Fragment {


    Collection collection;
    int page;
    int answer_type;

    @BindView(R.id.content_text)
    TextView content_tv;
    @BindView(R.id.answers_radio_group)
    RadioGroup rg_container;
    @BindView(R.id.show_hint_btn) Button show_hint_btn;
    @BindView(R.id.hint_txt) TextView hint_txt;

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

    public TestsActivityFragment() {
    }

    public static TestsActivityFragment newInstance(int page, Collection collection) {
        TestsActivityFragment fragment = new TestsActivityFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tests_activity, container, false);
        ButterKnife.bind(this, view);


        page = getArguments().getInt("page");
        Collections collections = new Collections(getActivity().getApplicationContext());
        collection = collections.getCollection();

        studentAnswers = new StudentAnswers(getActivity().getApplicationContext());

        int p = page + 1;
        ((TestsActivity) getActivity())
                .setActionBarTitle("Задание № " + p);

        final boolean[] flag = {false};
        show_hint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hint_txt.setText("Не доступно.");
                if(!flag[0]){
                    hint_txt.setVisibility(View.VISIBLE);
                    flag[0] = true;
                }else{
                    hint_txt.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (collection != null) {

            content_tv.setText(collection.getContent(page));
            answers = collection.getAnswers(page);
            answer_type = collection.getAnswerType(page);

            if (answer_type == Constants.RADIO_BUTTON_TYPE) {

                answers_container.setVisibility(View.GONE);
                rg_answers = new RadioButton[answers.size()];
                for (int i = 0; i < answers.size(); i++) {
                    rg_answers[i] = new RadioButton(getActivity().getApplicationContext());
                    rg_answers[i].setText(answers.get(i));
                    rg_container.addView(rg_answers[i]);
                }

            } else if (answer_type == Constants.CHECK_BOX_TYPE) {

                rg_container.setVisibility(View.GONE);
                cb_answers = new CheckBox[answers.size()];
                for (int i = 0; i < answers.size(); i++) {
                    cb_answers[i] = new CheckBox(getActivity().getApplicationContext());
                    cb_answers[i].setText(answers.get(i));
                    answers_container.addView(cb_answers[i]);
                }

            } else if (answer_type == Constants.TEXT_TYPE) {

                rg_container.setVisibility(View.GONE);
                enter_answer_et = new EditText(getActivity().getApplicationContext());
                enter_answer_et.setHint("Введите ответ");
                answers_container.addView(enter_answer_et);
            }
        }

        save_ans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AbstractAnswer abstractAnswer = new AbstractAnswer(getActivity().getApplicationContext());
                Student student = new Student(getActivity().getApplicationContext());
                HashMap<String,String> sdata =  student.getStatistics();


                List<String> ra = collection.getRightAnswers(page);

                //todo make checkbox type
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

                    if(abstractAnswer.isRight()){
                        student.setStatistics(Integer.valueOf(sdata.get(student.KEY_TESTS_COUNTER)) + 1,
                                              Double.valueOf(sdata.get(student.KEY_TESTS_TIME)) + 1.354,
                                Integer.valueOf(sdata.get(student.KEY_ANSWERS_COUNTER)) + 1,
                                Integer.valueOf(sdata.get(student.KEY_WRONG_ANSWERS_COUNTER)));
                    } else{
                        student.setStatistics(Integer.valueOf(sdata.get(student.KEY_TESTS_COUNTER)) + 1,
                                Double.valueOf(sdata.get(student.KEY_TESTS_TIME)) + 1.354,
                                Integer.valueOf(sdata.get(student.KEY_ANSWERS_COUNTER)),
                                Integer.valueOf(sdata.get(student.KEY_WRONG_ANSWERS_COUNTER)) + 1);
                    }

                } else if (answer_type == Constants.TEXT_TYPE) {

                    abstractAnswer.saveET(ra.get(0),enter_answer_et.getText().toString());
                    if(abstractAnswer.isRight()){
                        student.setStatistics(Integer.valueOf(sdata.get(student.KEY_TESTS_COUNTER)) + 1,
                                Double.valueOf(sdata.get(student.KEY_TESTS_TIME)) + 1.354,
                                Integer.valueOf(sdata.get(student.KEY_ANSWERS_COUNTER)) + 1,
                                Integer.valueOf(sdata.get(student.KEY_WRONG_ANSWERS_COUNTER)));
                    } else{
                        student.setStatistics(Integer.valueOf(sdata.get(student.KEY_TESTS_COUNTER)) + 1,
                                Double.valueOf(sdata.get(student.KEY_TESTS_TIME)) + 1.354,
                                Integer.valueOf(sdata.get(student.KEY_ANSWERS_COUNTER)),
                                Integer.valueOf(sdata.get(student.KEY_WRONG_ANSWERS_COUNTER)) + 1);
                    }

                }

                passData(abstractAnswer);
            }
        });

        return view;
    }


    public interface OnDataPass {
        public void onDataPass(AbstractAnswer data);
    }

    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(AbstractAnswer data) {
        dataPasser.onDataPass(data);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity().getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
    }
}

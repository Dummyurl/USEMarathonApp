package ru.use.marathon.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.StartQuestion;


public class StartQuestionFragment extends Fragment {

    public static final String TAG = StartQuestionFragment.class.getSimpleName();

    @BindView(R.id.sq_title) TextView title;
    @BindView(R.id.sq_container_layout) LinearLayout container_layout;
    @BindView(R.id.main_layout) LinearLayout main_layout;
    @BindView(R.id.sq_container) RadioGroup radioGroupcontainer;
    @BindView(R.id.sq_welcome_layout) LinearLayout welcome_layout;
    @BindView(R.id.sq_description) TextView desc;

    int page,color;
    StartQuestion question;
    Bundle bundle;

    public interface AnswerListener {
        public void onAnswerFetched(int index, int position);
    }

    AnswerListener answerListener;

    public StartQuestionFragment() {
    }

    public static StartQuestionFragment newInstance(int page, int color, StartQuestion question) {

        StartQuestionFragment fragment = new StartQuestionFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putInt("color", color);
        args.putSerializable("question",question);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("page", 0);
        color = getArguments().getInt("color",0);
        question = (StartQuestion) getArguments().getSerializable("question");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_question_slide, container, false);
        ButterKnife.bind(this,view);

        main_layout.setBackgroundResource(color);

        if(page == 0){
            welcome_layout.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);

            desc.setText(getResources().getString(R.string.start_description));
            desc.setTextSize(18);
        }else{
            welcome_layout.setVisibility(View.GONE);
            container_layout.setVisibility(View.VISIBLE);
            if(question != null){
                title.setText(question.getQuestion());
                for (int i = 0; i < question.getAnswers().length; i++) {
                    RadioButton btn = new RadioButton(getActivity().getApplicationContext());
                    btn.setText(question.getAnswers()[i]);
                    btn.setId(i+1);
                    btn.setTextColor(Color.WHITE);
                    radioGroupcontainer.addView(btn);
                }
            }
        }
        answerListener = new AnswerListener() {
            @Override
            public void onAnswerFetched(int index, int position) {

            }
        };

        radioGroupcontainer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    answerListener.onAnswerFetched(page,radioGroup.indexOfChild(checkedRadioButton));
                }
            }
        });


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int checked_id = radioGroupcontainer.getCheckedRadioButtonId();
        if(checked_id != -1){
            outState.putInt("checked",checked_id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            int checked_id = savedInstanceState.getInt("checked");
            if(checked_id != -1) {
                radioGroupcontainer.check(checked_id);
            }

            Log.d(TAG, "onActivityCreated: checked id" + String.valueOf(checked_id));
        }
    }


}

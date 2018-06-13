package ru.use.marathon.activities;

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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.models.Collection;


public class TestsActivityFragment extends Fragment {


    Collection collection;
    int page;
    int answer_type;

    @BindView(R.id.content_text)
    TextView content_tv;
    @BindView(R.id.answers_radio_group)
    RadioGroup rg_container;

    @BindView(R.id.answers_container)
    LinearLayout answers_container;

    @BindView(R.id.save_answer_btn)
    Button save_ans_btn;

    //ANSWERS SECTION
    List<String> answers;
    RadioButton[] rg_answers;
    CheckBox[] cb_answers;
    EditText enter_answer_et;

    public TestsActivityFragment() {
    }

    public static TestsActivityFragment newInstance(int page, Collection collection) {
        TestsActivityFragment fragment = new TestsActivityFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putSerializable("collection", collection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tests_activity, container, false);
        ButterKnife.bind(this, view);

        page = getArguments().getInt("page");
        collection = (Collection) getArguments().getSerializable("collection");

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
                enter_answer_et.setHint("Enter answer here..");
                answers_container.addView(enter_answer_et);
            }
        }

        save_ans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<String> ra = collection.getRightAnswers(page);
                if (answer_type == Constants.RADIO_BUTTON_TYPE) {
//                    RadioButton d = (RadioButton) view.findViewById(rg_container.getCheckedRadioButtonId());

                    RadioButton radioButton =(RadioButton) rg_container.findViewById(rg_container.getCheckedRadioButtonId());
//                    radioButton.setTextColor(getResources(R.color.colorAccent));
                    int i = rg_container.indexOfChild(radioButton);
                    Log.i("RADIO_BTN_INDEX",String.valueOf(i));

                    Log.i("RA_INDEX",String.valueOf(ra.get(0)));

                    if(ra.get(0).equals(String.valueOf(i+1))){
                        Toast.makeText(getActivity().getApplicationContext(), "Right answer!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                    }

                } else if (answer_type == Constants.CHECK_BOX_TYPE) {

//                    List<String> ra_answers = new ArrayList<>();
//                    for (int i = 0; i < ra.size(); i++) {
//                        int j = Integer.valueOf(ra.get(i)) - 1;
//                        for (int k = 0; k < answers.size(); k++) {
//                            if(k == j) ra_answers.add(answers.get(k));
//                        }
//                    }

                } else if (answer_type == Constants.TEXT_TYPE) {

                }
            }
        });

        return view;
    }
}

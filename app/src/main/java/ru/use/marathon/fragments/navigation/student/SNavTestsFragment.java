package ru.use.marathon.fragments.navigation.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.use.marathon.R;
import ru.use.marathon.activities.StartQuestionsActivity;
import ru.use.marathon.activities.TestsByCollectionsActivity;
import ru.use.marathon.activities.TopicsActivity;
import ru.use.marathon.fragments.AbstractFragment;

import static ru.use.marathon.Constants.DEBUG;

/**
 * Created by ilyas on 10-Jun-18.
 */

public class SNavTestsFragment extends AbstractFragment {

    @BindView(R.id.by_variants)
    Button by_vars;

    @BindView(R.id.by_topics)
    Button by_topics;

    @BindView(R.id.tests_layout)
    LinearLayout layout;


    Unbinder unbinder;

    public SNavTestsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_nav_tests, container, false);
        unbinder = ButterKnife.bind(this,view);

        by_vars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TestsByCollectionsActivity.class));
            }
        });

        by_topics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TopicsActivity.class));
            }
        });

        if(DEBUG){
            Button btn = new Button(getActivity().getApplicationContext());
            btn.setText("TEST_START_QUESTIONS");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), StartQuestionsActivity.class));
                }
            });
            layout.addView(btn);

        }


        return view;
    }
}

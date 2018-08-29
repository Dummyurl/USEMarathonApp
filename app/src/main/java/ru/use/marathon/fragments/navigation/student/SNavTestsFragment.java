package ru.use.marathon.fragments.navigation.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.use.marathon.R;
import ru.use.marathon.activities.TestsByCollectionsActivity;
import ru.use.marathon.activities.TopicsActivity;

/**
 * Created by ilyas on 10-Jun-18.
 */

public class SNavTestsFragment extends Fragment {

    @BindView(R.id.by_variants)
    Button by_vars;

    @BindView(R.id.by_topics)
    Button by_topics;


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

        return view;
    }
}

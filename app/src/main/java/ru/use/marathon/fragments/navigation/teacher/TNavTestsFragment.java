package ru.use.marathon.fragments.navigation.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.activities.CreateTaskActivity;
import ru.use.marathon.fragments.AbstractFragment;

/**
 * Created by ilyas on 14-Jul-18.
 */

public class TNavTestsFragment extends AbstractFragment {

    @BindView(R.id.create_task_btn)
    Button btn;

    public TNavTestsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_tests,container,false);
        ButterKnife.bind(this,view);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateTaskActivity.class));
            }
        });

        return view;
    }
}

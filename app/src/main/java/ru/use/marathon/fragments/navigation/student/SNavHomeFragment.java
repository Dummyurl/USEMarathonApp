package ru.use.marathon.fragments.navigation.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.use.marathon.R;
import ru.use.marathon.activities.LoginActivity;
import ru.use.marathon.models.Student;

/**
 * Created by ilyas on 10-Jun-18.
 */

public class SNavHomeFragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.user_id)
    TextView user_id_tv;
    @BindView(R.id.user_email)
    TextView user_email_tv;
    @BindView(R.id.user_name)
    TextView user_name_tv;
    @BindView(R.id.logout)
    Button logout;

    public SNavHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_nav_home, parent, false);
        unbinder = ButterKnife.bind(this, view);

        final Student s = new Student(getActivity().getApplicationContext());
        HashMap<String, String> data = s.getData();
        user_id_tv.setText(data.get("id"));
        user_email_tv.setText(data.get("email"));
        user_name_tv.setText(data.get("name"));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s.login(false);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
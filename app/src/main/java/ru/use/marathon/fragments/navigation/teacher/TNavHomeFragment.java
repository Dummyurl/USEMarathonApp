package ru.use.marathon.fragments.navigation.teacher;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import ru.use.marathon.models.Teacher;

/**
 * Created by ilyas on 14-Jun-18.
 */

public class TNavHomeFragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.user_id)
    TextView user_id_tv;
    @BindView(R.id.user_email)
    TextView user_email_tv;
    @BindView(R.id.user_name)
    TextView user_name_tv;
    @BindView(R.id.logout)
    Button logout;


    public TNavHomeFragment() {
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

        final Teacher s = new Teacher(getActivity().getApplicationContext());
        HashMap<String, String> data = s.getData();
        user_id_tv.setText(data.get("id"));
        user_email_tv.setText(data.get("email"));
        user_name_tv.setText("Welcome, " + data.get("name") + "!");
        user_name_tv.setTextSize(20);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gilroy.otf");
        user_name_tv.setTypeface(font);

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
//        unbinder.unbind();
    }
}

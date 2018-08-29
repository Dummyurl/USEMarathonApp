package ru.use.marathon.fragments.navigation;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.activities.UserProfileActivity;
import ru.use.marathon.adapters.StatisticsAdapter;
import ru.use.marathon.adapters.TeachersStudentsAdapter;
import ru.use.marathon.fragments.AbstractFragment;
import ru.use.marathon.models.StatisticsResponse;
import ru.use.marathon.models.UsersResponse;
import ru.use.marathon.utils.ItemClickSupport;

/**
 * Created by ilyas on 10-Jun-18.
 */

public class ProfileFragment extends AbstractFragment {

    Unbinder unbinder;

    @BindView(R.id.user_image)
    CircleImageView user_image;
    @BindView(R.id.user_id)
    TextView user_id_tv;
    @BindView(R.id.user_email)
    TextView user_email_tv;
    @BindView(R.id.user_name)
    TextView user_name_tv;
    @Nullable
    @BindView(R.id.logout)
    Button logout;

    //STUDENT
    @BindView(R.id.student_relative)
    RelativeLayout student_layout;
    @BindView(R.id.stats_rv)
    RecyclerView stats_rv;
    @BindView(R.id.subjects_spinner)
    Spinner subject_spinner;


    //TEACHER
    @BindView(R.id.teacher_relative)
    RelativeLayout teacher_relative;
    @BindView(R.id.my_students_rv)
    RecyclerView my_students_rv;

    UsersResponse users;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, parent, false);
        unbinder = ButterKnife.bind(this, view);


        user_id_tv.setText(String.valueOf(user_id()));
        user_email_tv.setText(String.valueOf(email()));
        user_name_tv.setText(String.valueOf(name()));

        Picasso.get().load(R.drawable.user_default).into(user_image);

        user_name_tv.setTextSize(28);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gilroy.otf");

        user_email_tv.setTypeface(font);
        user_name_tv.setTypeface(font);

        student_layout.setVisibility(View.GONE);
        teacher_relative.setVisibility(View.GONE);

        if (userType() == STUDENT) {
            student_layout.setVisibility(View.VISIBLE);
            if (subject() != -1) {
                subject_spinner.setSelection(subject() - 1);
            }
            student_initSubjects();
            student_initStudentStats();
        } else if (userType() == TEACHER) {
            teacher_relative.setVisibility(View.VISIBLE);
            initTeacherStudents();
        }


        if (logout != null) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logout();
                }
            });
        }
        return view;
    }

    private void student_initSubjects() {
        subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = i + 1;
                setSubject(index);
                student_initStudentStats();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initTeacherStudents() {

        final ArrayList<String> ids = new ArrayList<>();

        my_students_rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        AppController.getApi().getTeachersStudents(1, "getTeachersStudents", user_id()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                users = new UsersResponse(response);
                if(users.success()){
                    for (int i = 0; i < users.size(); i++) {
                        ids.add(users.getId(i));
                    }
                    TeachersStudentsAdapter adapter = new TeachersStudentsAdapter(users, getActivity().getApplicationContext());
                    my_students_rv.setAdapter(adapter);

                    ItemClickSupport.addTo(my_students_rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            String id = ids.get(position);
                            Intent i = new Intent(getActivity(), UserProfileActivity.class);
                            i.putExtra("user_id", id);
                            i.putExtra("utype", 0);
                            startActivity(i);
                        }

                    });

                }else{
                    TextView tv = new TextView(getActivity().getApplicationContext());
                    tv.setText("У вас нет учеников, или произошла ошибка.");
                    tv.setTextColor(Color.RED);
                    teacher_relative.addView(tv);
                    teacher_relative.setGravity(RelativeLayout.CENTER_VERTICAL);
                    teacher_relative.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showInfoDialog("Ошибка!", "Сообщение: " + t.getMessage());
            }
        });


    }

    private void student_initStudentStats() {

        stats_rv.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 3));

            AppController.getApi().getStats(1, "getStats", subject(), String.valueOf(user_id())).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    StatisticsResponse response1 = new StatisticsResponse(response);
                    Double[] stats = new Double[3];
                    stats[0] = response1.totalTests();
                    stats[1] = response1.averageTime();
                    stats[2] = response1.wrongPercent();
                    String[] names = new String[]{"Всего заданий решено", "Среднее время решения", "Процент ошибок"};
                    StatisticsAdapter adapter = new StatisticsAdapter(stats, names);
                    stats_rv.setAdapter(adapter);

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    showInfoDialog("Ошибка!", "Сообщение: " + t.getMessage());
                }
            });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

}
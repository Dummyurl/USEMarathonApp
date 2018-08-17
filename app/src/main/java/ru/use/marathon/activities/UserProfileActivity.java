package ru.use.marathon.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.adapters.StatisticsAdapter;
import ru.use.marathon.models.Statistics;
import ru.use.marathon.models.StatisticsResponse;
import ru.use.marathon.models.UserProfile;

public class UserProfileActivity extends AbstractActivity {

    @BindView(R.id.account_image)
    CircleImageView imageView;
    @BindView(R.id.account_name)
    TextView tv_name;
    @BindView(R.id.account_status)
    TextView tv_status;


    //TEACHER
    @BindView(R.id.teacher_layout)
    RelativeLayout teacher_layout;
    @BindView(R.id.account_write)
    Button teacher_write_message_btn;
    @BindView(R.id.account_request_teacher)
    Button teacher_request_btn;

    //STUDENT
    @BindView(R.id.student_layout)
    RelativeLayout student_relative;
    @BindView(R.id.account_statistics_rv)
    RecyclerView stats_rv;
    @BindView(R.id.account_write_message)
    Button student_write_message_btn;
    @BindView(R.id.account_teacher_name)
    TextView teacher_name;
    @BindView(R.id.subjects_spinner)
    Spinner subject_spinner;

    String user_id;
    int user_type;
    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        user_id = getIntent().getStringExtra("user_id");
        user_type = getIntent().getIntExtra("utype", -1);


        //todo set loading alert dialog
        AppController.getApi().getUserInfo(1, "getUserInfo", user_id, user_type).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                userProfile = new UserProfile(response);
                if (!userProfile.image().isEmpty())
                    Picasso.get().load(userProfile.image()).error(R.drawable.user_default).into(imageView);
                tv_name.setText(userProfile.name());

                if (user_type == 0) {
                    initStudent(userProfile);
                } else if (user_type == 1) {
                    initTeacher(userProfile);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



    }
    private void initTeacher(UserProfile userProfile) {
        teacher_layout.setVisibility(View.VISIBLE);
        student_relative.setVisibility(View.GONE);

        tv_status.setText("Teacher");
        teacher_write_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMessagingTeacher();
            }
        });
        teacher_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTeacherRequest();
            }
        });
    }

    private void initStudent(final UserProfile userProfile) {

        teacher_layout.setVisibility(View.GONE);
        student_relative.setVisibility(View.VISIBLE);
        tv_status.setText("Student");
        teacher_name.setText(userProfile.teacher_name());
        teacher_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserProfileActivity.this, "teacher_id:" + userProfile.teacher_id(), Toast.LENGTH_SHORT).show();
            }
        });
        student_write_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMessagingStudent();
            }
        });
        stats_rv.setLayoutManager(new GridLayoutManager(this, 3));
        getStats(1);

        subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = i + 1;
                getStats(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void getStats(int subject) {
        AppController.getApi().getStats(1, "getStats", subject, user_id).enqueue(new Callback<JsonObject>() {
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

            }
        });
    }

    private void initTeacherRequest() {
        Toast.makeText(this, "Requests aren't ready yet", Toast.LENGTH_SHORT).show();
    }

    private void initMessagingTeacher() {
        Toast.makeText(this, "messaging is not ready yet", Toast.LENGTH_SHORT).show();
    }

    private void initMessagingStudent() {
        Toast.makeText(this, "messaging is not ready yet", Toast.LENGTH_SHORT).show();
    }
}

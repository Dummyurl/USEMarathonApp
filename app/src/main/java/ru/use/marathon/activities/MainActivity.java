package ru.use.marathon.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.fragments.navigation.ChatFragment;
import ru.use.marathon.fragments.navigation.FeedFragment;
import ru.use.marathon.fragments.navigation.ProfileFragment;
import ru.use.marathon.fragments.navigation.student.SNavTestsFragment;
import ru.use.marathon.fragments.navigation.TheoryFragment;
import ru.use.marathon.fragments.navigation.teacher.TNavHomeFragment;
import ru.use.marathon.fragments.navigation.teacher.TNavTestsFragment;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;
import ru.use.marathon.utils.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bnv;

    BroadcastReceiver mRegistrationBroadcastReceiver;

    ArrayList<String> menu = new ArrayList<>();

    String NAV_TAG = "a";
    int user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        NAV_TAG = getIntent().getStringExtra("bnv_tag");


        final Student student = new Student(this);
        final Teacher teacher = new Teacher(this);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(student.isLoggedIn() && !teacher.isLoggedIn()){
            user_type = 0;
            ft.add(R.id.use_container,new FeedFragment()).commit();
        }else if(!student.isLoggedIn() && teacher.isLoggedIn()) {
            user_type = 1;
            ft.add(R.id.use_container,new FeedFragment()).commit();
        }else{
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        BottomNavigationViewHelper.disableShiftMode(bnv);


//
//        if(NAV_TAG.equals("chat")){
//            if(user_type == 0)
//            ft.replace(R.user_id.use_container,new ChatFragment()).commit();
//            //todo complete with teacher
//        }

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(student.isLoggedIn() && !teacher.isLoggedIn()){
                    switch (item.getItemId()) {
                        case R.id.action_feed:
                            transaction.replace(R.id.use_container,new FeedFragment()).commit();
                            break;
                        case R.id.action_tests:
                            transaction.replace(R.id.use_container, new SNavTestsFragment()).commit();
                            break;
                        case R.id.action_theory:
                            transaction.replace(R.id.use_container,new TheoryFragment()).commit();
                            break;
                        case R.id.action_chat:
                            transaction.replace(R.id.use_container,new ChatFragment()).commit();
                            break;
                        case R.id.action_profile:
                            transaction.replace(R.id.use_container, new ProfileFragment()).commit();
                            break;

                    }
                }else if(!student.isLoggedIn() && teacher.isLoggedIn()) {
                    switch (item.getItemId()) {
                        case R.id.action_profile:
                            transaction.replace(R.id.use_container, new ProfileFragment()).commit();
                            break;
                        case R.id.action_feed:
                            transaction.replace(R.id.use_container, new FeedFragment()).commit();
                            break;
                        case R.id.action_tests:
                            transaction.replace(R.id.use_container, new TNavTestsFragment()).commit();
                            break;
                        case R.id.action_theory:
                            transaction.replace(R.id.use_container,new TheoryFragment()).commit();
                            break;
                        case R.id.action_chat:
                            transaction.replace(R.id.use_container,new ChatFragment()).commit();
                            break;
                    }
                }

                return true;

            }
        });

        bnv.setSelectedItemId(R.id.action_feed);

    }

}

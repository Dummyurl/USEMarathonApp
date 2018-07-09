package ru.use.marathon.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import ru.use.marathon.Constants;
import ru.use.marathon.R;
import ru.use.marathon.fragments.navigation.student.SNavChatFragment;
import ru.use.marathon.fragments.navigation.student.SNavFeedFragment;
import ru.use.marathon.fragments.navigation.student.SNavProfileFragment;
import ru.use.marathon.fragments.navigation.student.SNavTestsFragment;
import ru.use.marathon.fragments.navigation.student.SNavTheoryFragment;
import ru.use.marathon.fragments.navigation.teacher.TNavHomeFragment;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;
import ru.use.marathon.utils.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bnv;

    BroadcastReceiver mRegistrationBroadcastReceiver;

    ArrayList<String> menu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final Student student = new Student(this);
        final Teacher teacher = new Teacher(this);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(student.isLoggedIn() && !teacher.isLoggedIn()){

            ft.add(R.id.use_container,new SNavFeedFragment()).commit();
        }else if(!student.isLoggedIn() && teacher.isLoggedIn()) {
            ft.add(R.id.use_container,new TNavHomeFragment()).commit();
        }else{
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        BottomNavigationViewHelper.disableShiftMode(bnv);



        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(student.isLoggedIn() && !teacher.isLoggedIn()){
                    switch (item.getItemId()) {
                        case R.id.action_feed:
                            transaction.replace(R.id.use_container,new SNavFeedFragment()).commit();
                            break;
                        case R.id.action_tests:
                            transaction.replace(R.id.use_container, new SNavTestsFragment()).commit();
                            break;
                        case R.id.action_theory:
                            transaction.replace(R.id.use_container,new SNavTheoryFragment()).commit();
                            break;
                        case R.id.action_chat:
                            transaction.replace(R.id.use_container,new SNavChatFragment()).commit();
                            break;
                        case R.id.action_profile:
                            transaction.replace(R.id.use_container, new SNavProfileFragment()).commit();
                            break;

                    }
                }else if(!student.isLoggedIn() && teacher.isLoggedIn()) {
                    switch (item.getItemId()) {
                        case R.id.action_feed:
                            transaction.replace(R.id.use_container, new TNavHomeFragment()).commit();
                            break;
                        case R.id.action_tests:
                            Toast.makeText(MainActivity.this, "not available", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.action_theory:
                            Toast.makeText(MainActivity.this, "not available", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                return true;

            }
        });

        bnv.setSelectedItemId(R.id.action_feed);

    }

}

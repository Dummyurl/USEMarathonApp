package ru.use.marathon.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

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
    AHBottomNavigation bnv;

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

        if (student.isLoggedIn() && !teacher.isLoggedIn()) {
            user_type = 0;
            ft.add(R.id.use_container, new FeedFragment()).commit();
        } else if (!student.isLoggedIn() && teacher.isLoggedIn()) {
            user_type = 1;
            ft.add(R.id.use_container, new FeedFragment()).commit();
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }


        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Теория", R.drawable.ic_theory_24dp);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Тесты", R.drawable.ic_tests_black_24dp);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Новости", R.drawable.ic_explore_black_24dp);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Чат", R.drawable.ic_chat);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Профиль", R.drawable.ic_account);


        item1.setColorRes(R.color.colorPrimary);
        item2.setColorRes(R.color.colorPrimary);
        item3.setColorRes(R.color.colorPrimary);
        item4.setColorRes(R.color.colorPrimary);
        item5.setColorRes(R.color.colorPrimary);
        bnv.addItem(item1);
        bnv.addItem(item2);
        bnv.addItem(item3);
        bnv.addItem(item4);
        bnv.addItem(item5);
        bnv.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bnv.setCurrentItem(2);
        bnv.setAccentColor(getResources().getColor(R.color.colorAccent));

        bnv.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int y, boolean wasSelected) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (student.isLoggedIn() && !teacher.isLoggedIn()) {
                    switch (y) {
                        case 2:
                            transaction.replace(R.id.use_container, new FeedFragment()).commit();
                            break;
                        case 1:
                            transaction.replace(R.id.use_container, new SNavTestsFragment()).commit();
                            break;
                        case 0:
                            transaction.replace(R.id.use_container, new TheoryFragment()).commit();
                            break;
                        case 3:
                            transaction.replace(R.id.use_container, new ChatFragment()).commit();
                            break;
                        case 4:
                            transaction.replace(R.id.use_container, new ProfileFragment()).commit();
                            break;

                    }
                } else if (!student.isLoggedIn() && teacher.isLoggedIn()) {
                    switch (y) {
                        case 4:
                            transaction.replace(R.id.use_container, new ProfileFragment()).commit();
                            break;
                        case 2:
                            transaction.replace(R.id.use_container, new FeedFragment()).commit();
                            break;
                        case 1:
                            transaction.replace(R.id.use_container, new TNavTestsFragment()).commit();
                            break;
                        case 0:
                            transaction.replace(R.id.use_container, new TheoryFragment()).commit();
                            break;
                        case 3:
                            transaction.replace(R.id.use_container, new ChatFragment()).commit();
                            break;
                    }
                }

                return true;
            }
        });

    }


}

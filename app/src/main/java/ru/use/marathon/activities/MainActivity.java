package ru.use.marathon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.fragments.navigation.student.SNavHomeFragment;
import ru.use.marathon.fragments.navigation.student.SNavTestsFragment;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.Teacher;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dropdown_menu)
    Spinner dropdown_menu_spinner;

    ArrayList<String> menu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        final Student student = new Student(this);
        final Teacher teacher = new Teacher(this);

        if(student.isLoggedIn() && !teacher.isLoggedIn()){

           menu.add("Home");
           menu.add("Tests");
           menu.add("Theory");

        }else if(!student.isLoggedIn() && teacher.isLoggedIn()) {
            //teacher
            menu.add("Home");
            menu.add("Tasks");
            menu.add("Theory");
            menu.add("Live");
            menu.add("Rating");

        }else{
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }


        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //todo make changeable and flexible
        ft.add(R.id.use_container, new SNavTestsFragment());
        ft.commit();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,menu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_menu_spinner.setAdapter(adapter);
        dropdown_menu_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(i == 0) {
                    transaction.replace(R.id.use_container, new SNavHomeFragment()).commit();
                    transaction.addToBackStack(null);
                }else{
                    transaction.replace(R.id.use_container, new SNavTestsFragment()).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}

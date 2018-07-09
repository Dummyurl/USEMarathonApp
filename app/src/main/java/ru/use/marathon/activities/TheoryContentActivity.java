package ru.use.marathon.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;


public class TheoryContentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.theory_item_id)
    TextView theory_item_id;

    int id;
    String title,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory_content);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        id = getIntent().getIntExtra("id",0);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");

        theory_item_id.setText("Theory item ID: " + id + "\n Theory item content: " + content);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

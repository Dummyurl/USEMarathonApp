package ru.use.marathon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.models.Collection;
import ru.use.marathon.models.Collections;


public class TheoryContentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.web_theory_container)
    WebView theory_container;

    @BindView(R.id.goto_tests_btn)
    Button tests_btn;

    int id,topic_id;
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
        topic_id = getIntent().getIntExtra("topics_id",0);

        theory_container.setWebViewClient(new MyBrowser());
        theory_container.getSettings().setLoadsImagesAutomatically(true);
        theory_container.getSettings().setDomStorageEnabled(true);
        theory_container.getSettings().setJavaScriptEnabled(true);
        theory_container.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        theory_container.loadUrl(content);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tests_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppController.getApi().get_collection_by_topics(1,"get_collection_by_topics",topic_id).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Collection c = new Collection(response);
                        Collections collections = new Collections(getApplicationContext());
                        collections.saveCollection(c);
                        Intent view = new Intent(TheoryContentActivity.this,TestsActivity.class);
                        view.putExtra("tag","topics");
                        startActivity(view);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theory_content_menu,menu);
        return true;
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

package ru.use.marathon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.adapters.TopicsAdapter;
import ru.use.marathon.models.Collection;
import ru.use.marathon.models.Collections;
import ru.use.marathon.models.Student;
import ru.use.marathon.models.topics.SolvedTestsByTopics;
import ru.use.marathon.models.topics.Topics;
import ru.use.marathon.utils.ItemClickSupport;

public class TopicsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.by_topics_rv)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_topics);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Student s = new Student(getApplicationContext());

        HashMap<String,String> sdata = s.getData();
        final int user_id = Integer.parseInt(sdata.get(s.KEY_ID));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppController.getApi().get_topics(1,"get_topics",s.getSubject()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final Topics topics = new Topics(response);

                AppController.getApi().get_solved_tests_by_topic(1,"get_solved_by_topic",s.getSubject(),user_id).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        SolvedTestsByTopics byTopics = new SolvedTestsByTopics(response);
                        TopicsAdapter topicsAdapter = new TopicsAdapter(topics,byTopics);
                        recyclerView.setAdapter(topicsAdapter);
                        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                int id = topics.getID(position);
                                initTests(id);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



    }

    private void initTests(int id) {
        AppController.getApi().get_collection_by_topics(1,"get_collection_by_topics",id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Collection c = new Collection(response);
                Collections collections = new Collections(getApplicationContext());
                collections.saveCollection(c);
                Intent view = new Intent(TopicsActivity.this,TestsActivity.class);
                view.putExtra("tag","topics");
                startActivity(view);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}

package ru.use.marathon.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.fragments.TestUnitFragment;
import ru.use.marathon.models.Collection;
import ru.use.marathon.models.Collections;
import ru.use.marathon.models.Tests.TestsViewModel;
import ru.use.marathon.models.answers.AbstractAnswer;
import ru.use.marathon.models.answers.StudentAnswers;
import ru.use.marathon.utils.CounterClass;

public class TestsActivity extends AbstractActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tests_viewpager_container)
    ViewPager viewPager;

    Collection collection;
    int collection_number;
    int tests_amount = 0;
    List<String> answers;
    List<Integer> right_answers;
    String tag = "";
    StudentAnswers studentAnswers;
    List<AbstractAnswer> abstractAnswers;
    ArrayList<Integer> test_numbers;


    TestsViewModel testsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //todo set loading message

        testsViewModel = ViewModelProviders.of(this).get(TestsViewModel.class);

        tag = getIntent().getStringExtra("tag");
        studentAnswers = new StudentAnswers(getApplicationContext());
        abstractAnswers = new ArrayList<>();
        test_numbers = new ArrayList<>();

        if (tag.equals("var")) {
            collection_number = getIntent().getIntExtra("collection_number", 0);
            variants_data();
        } else if (tag.equals("topics")) {
            Collections collections = new Collections(getApplicationContext());
            collection = collections.getCollection();
            tests_amount = collection.size();
            MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), collection, -1);
            viewPager.setAdapter(adapter);
            for (int i = 0; i < tests_amount; i++) {
                test_numbers.add(collection.getTaskNumber(i));
            }
        }

    }

    private void variants_data() {

        AppController.getApi().get_collection(1, "get_collection", subject(), collection_number).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Collections collections = new Collections(getApplicationContext());

                collection = new Collection(response);
                collections.saveCollection(collection);
                tests_amount = collection.size();
                MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), collection, collection_number);
                viewPager.setAdapter(adapter);

                for (int i = 0; i < collection.size(); i++) {
                    test_numbers.add(collection.getTaskNumber(i));
                }

                CounterClass counterClass = CounterClass.initInstance(180000, 1000);
                counterClass.start();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showInfoDialog("Ошибка!", "Не удалось загрузить данные с сервера");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        Collection collection;
        int cn;

        public MyFragmentPagerAdapter(FragmentManager fm, Collection collection, int cn) {
            super(fm);
            this.collection = collection;
            this.cn = cn;
        }


        @Override
        public Fragment getItem(int position) {
            return TestUnitFragment.newInstance(position, tests_amount, cn);
        }

        @Override
        public int getCount() {
            return tests_amount;
        }



    }


}

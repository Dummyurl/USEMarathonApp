package ru.use.marathon.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.models.Collection;

public class TestsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tests_viewpager_container)
    ViewPager viewPager;

    Collection collection;
    int collection_number;
    int tests_amount = 0;
    List<String> answers;
    List<Integer> right_answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //todo set loading message



        collection_number = getIntent().getIntExtra("collection_number",0);

        initData();


    }

    private void initData() {

        AppController.getApi().get_collection(1,"get_collection",collection_number).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                collection = new Collection(response);
                tests_amount = collection.size();
                MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),collection);
                viewPager.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }



    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        Collection collection;

        public MyFragmentPagerAdapter(FragmentManager fm, Collection collection) {
            super(fm);
            this.collection = collection;
        }

        @Override
        public Fragment getItem(int position) {
            return TestsActivityFragment.newInstance(position,collection);
        }

        @Override
        public int getCount() {
            return tests_amount;
        }

    }


}

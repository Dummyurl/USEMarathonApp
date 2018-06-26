package ru.use.marathon.fragments.navigation.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.adapters.TheoryTopicsAdapter;
import ru.use.marathon.models.TheoryTopics;

/**
 * Created by ilyas on 14-Jun-18.
 */

public class SNavTheoryFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.theory_rv)
    RecyclerView theory_rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_theory,container,false);
        ButterKnife.bind(this,view);

        theory_rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        AppController.getApi().get_theory_topics(1,"get_theory_topics").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                TheoryTopics t = new TheoryTopics(response);
                theory_rv.setAdapter(new TheoryTopicsAdapter(t));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

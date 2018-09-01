package ru.use.marathon.fragments.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.activities.TheoryContentActivity;
import ru.use.marathon.adapters.TheoryTopicsAdapter;
import ru.use.marathon.fragments.AbstractFragment;
import ru.use.marathon.models.TheoryTopics;
import ru.use.marathon.utils.ItemClickSupport;

/**
 * Created by ilyas on 14-Jun-18.
 */

public class TheoryFragment extends AbstractFragment {

    Unbinder unbinder;
    @BindView(R.id.theory_rv)
    RecyclerView theory_rv;
    TheoryTopics topics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_theory,container,false);
        ButterKnife.bind(this,view);

        theory_rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), resId);
        theory_rv.setLayoutAnimation(animation);


        AppController.getApi().get_theory_topics(1,"get_theory_topics",subject()).enqueue(new Callback<JsonObject>() { // тут я указываю параметры запроса и формирую его
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // сюда приходит содержимое ответа, response

                topics = new TheoryTopics(response); // парсю пришедший ответ
                theory_rv.setAdapter(new TheoryTopicsAdapter(topics)); // проставляю ответ в список
                runLayoutAnimation(theory_rv);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showInfoDialog("Ошибка  !",t.getMessage());
            }
        });

        ItemClickSupport.addTo(theory_rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(getActivity(), TheoryContentActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtra("id",topics.getId(position));
                i.putExtra("title",topics.getTitle(position));
                i.putExtra("content",topics.getContent(position));
                i.putExtra("topics_id",topics.getIcon(position));
                startActivity(i);
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }
}

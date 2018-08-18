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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.activities.FeedContentActivity;
import ru.use.marathon.adapters.FeedAdapter;
import ru.use.marathon.fragments.AbstractFragment;
import ru.use.marathon.models.Feed;
import ru.use.marathon.utils.ItemClickSupport;

/**
 * Created by ilyas on 02-Jul-18.
 */

public class FeedFragment extends AbstractFragment {


////    @BindView(R.user_id.feed_pb)
//    @BindView(R.user_id.refresh_rv)
//    SwipeRefreshLayout  refreshLayout;

    Unbinder unbinder;
    @BindView(R.id.feed_container)
    RecyclerView recyclerView;

    @BindView(R.id.feed_pb)
    ProgressBar feed_pb;

    @BindView(R.id.feed_error_log)
    TextView feed_error;
    Feed feed;

    public FeedFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        unbinder = ButterKnife.bind(this,view);

        feed_pb.setVisibility(View.VISIBLE);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        initFeed();

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(getActivity(), FeedContentActivity.class);
                i.putExtra("id",feed.getId(position));
                i.putExtra("title",feed.getTitle(position));
                i.putExtra("image",feed.getImage(position));
                i.putExtra("content",feed.getContent(position));
                i.putExtra("created_at",feed.getCreatedAt(position));
                startActivity(i);
            }
        });
//
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                initFeed();
//                refreshLayout.setRefreshing(false);
//            }
//        });


        return view;
    }

    private void initFeed() {

       AppController.getApi().get_all_feed(1,"getAllFeed",0).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                feed = new Feed(response);
                recyclerView.setAdapter(new FeedAdapter(getActivity().getApplicationContext(),feed));
                feed_pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                feed_error.setVisibility(View.VISIBLE);
//                feed_error.setText("Service is not available right now. \n Error: " + t.getMessage());
//                feed_pb.setVisibility(View.GONE);
            }
        });

    }


}

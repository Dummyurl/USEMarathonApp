package ru.use.marathon.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.adapters.CollectionsAdapter;
import ru.use.marathon.models.Collections;
import ru.use.marathon.utils.ItemClickSupport;

public class FavoritesActivity extends AbstractActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collections_container)
    RecyclerView collectionsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        collectionsRecyclerView.setLayoutManager(mLayoutManager);
        int resId = R.anim.grid_layout_animation_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        collectionsRecyclerView.setLayoutAnimation(animation);
        gatherData();

        ItemClickSupport.addTo(collectionsRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent view = new Intent(FavoritesActivity.this,TestsActivity.class);
                view.putExtra("tag","var");
                position++;
                view.putExtra("collection_number",position);
                startActivity(view);
            }
        });

    }

    private void gatherData() {
        AppController.getApi().get_collections(1,"get_collections",subject()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Collections collections = new Collections(response);
                collectionsRecyclerView.setAdapter(new CollectionsAdapter(collections));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showInfoDialog("Ошибка!",t.getMessage());
            }
        });

    }

}

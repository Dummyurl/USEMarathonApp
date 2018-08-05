package ru.use.marathon.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.use.marathon.AppController;
import ru.use.marathon.R;
import ru.use.marathon.models.FeedText;

public class FeedContentActivity extends AbstractActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.header)
    ImageView feed_header_iv;
    @BindView(R.id.feed_content_web) WebView web;
    @BindView(R.id.feed_content_created_at) TextView created_at_tv;

    @BindView(R.id.feed_content_text)
    TextView text_container;

    boolean appBarExpanded;
    Menu collapsedMenu;
    int feed_id;
    String image,title,content,created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_content);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        feed_id = getIntent().getIntExtra("id",0);
        image = getIntent().getStringExtra("image");
        title= getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        created_at = getIntent().getStringExtra("created_at");

        AppController.getApi().getFeedTextById(1,"getTextById",feed_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                FeedText text = new FeedText(response);
                String fotext = "<br> <i>"+ created_at + "</i>";
                web.setWebViewClient(new MyBrowser());
                web.getSettings().setLoadsImagesAutomatically(true);
                web.getSettings().setDomStorageEnabled(true);
                web.getSettings().setJavaScriptEnabled(true);
                web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                web.loadUrl(text.getText());
                created_at_tv.setText(Html.fromHtml(fotext));

                if(!image.isEmpty()) Picasso.get().load(image).resize(500,200).centerCrop().into(feed_header_iv);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });
        collapsingToolbarLayout.setTitle(title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_content_menu, menu);
        collapsedMenu = menu;
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (collapsedMenu != null
                && (!appBarExpanded || collapsedMenu.size() != 1)) {
            //collapsed
//            collapsedMenu.add("Add")
//                    .setIcon(R.drawable.ic_star)
//                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            //expanded
        }
        return super.onPrepareOptionsMenu(collapsedMenu);
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}

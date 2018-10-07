package ru.use.marathon.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.utils.ObservableWebView;

import static ru.use.marathon.Constants.DEBUG;

public class TypeformQuizActivity extends AbstractActivity {

    @BindView(R.id.typeform_form_web)
    WebView webView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.typeform_load_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.typeform_state_info)
    TextView info;


    private String id;
    private String title;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeform_quiz);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        info.setVisibility(View.GONE);

        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        getSupportActionBar().setTitle(title);

        if (!id.isEmpty()) {
            url = "https://edway.typeform.com/to/" + id + "?name=" + name() + "&surname=" + surname() + "&id="+vk_id();
            if(DEBUG) Toast.makeText(this, "Data: id="+id + "?\nname=" + name() + "&\nsurname=" + surname() + "&\nid="+vk_id(), Toast.LENGTH_SHORT).show();
            webView.setWebViewClient(new MyBrowser());
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webView.loadUrl(url);
        } else {
            info.setVisibility(View.VISIBLE);
            info.setText("Проблема при загрузке игротеста, пожалуйста попробуйте перезайти, или попробовать позднее.");
        }
        progressBar.setVisibility(View.GONE);

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

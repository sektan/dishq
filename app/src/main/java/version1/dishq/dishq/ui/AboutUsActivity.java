package version1.dishq.dishq.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.util.Util;


/**
 * Created by dishq on 8/17/16.
 * Package name version1.dishq.dishq.
 */
@SuppressLint("SetJavaScriptEnabled")
public class AboutUsActivity extends BaseActivity {
    TextView aboutUs, terms, privacyPolicy, Blog;
    WebView wv1;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        aboutUs = (TextView) findViewById(R.id.aboutus);
        terms = (TextView) findViewById(R.id.terms);
        privacyPolicy = (TextView) findViewById(R.id.Privacypolicy);
        Blog = (TextView) findViewById(R.id.Blog);
        aboutUs.setTypeface(Util.opensansregular);
        terms.setTypeface(Util.opensansregular);
        privacyPolicy.setTypeface(Util.opensansregular);
        Blog.setTypeface(Util.opensansregular);
        wv1 = (WebView) findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl("http://dishq.in");
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wv1.loadUrl("http://dishq.in");
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wv1.loadUrl("http://www.dishq.in/privacy");
            }
        });
        Blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wv1.loadUrl("http://dishq.in/blog/");
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wv1.loadUrl("http://www.dishq.in/terms");
            }
        });

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                JSONObject prop = new JSONObject();
                try {
                    prop.put("Screen", "about");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

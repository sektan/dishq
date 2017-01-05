package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import version1.dishq.dishq.R;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 29-12-2016.
 * Package name version1.dishq.dishq.
 */

public class RecipeFragment extends Fragment {

    private WebView wvRecipe;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        wvRecipe = (WebView) v.findViewById(R.id.recipe_webview);
        wvRecipe.setWebViewClient(new webBrowser());
        wvRecipe.getSettings().setLoadsImagesAutomatically(true);
        wvRecipe.getSettings().setJavaScriptEnabled(true);
        wvRecipe.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvRecipe.loadUrl(Util.getRecipeUrl());
        progressDialog.dismiss();
        return v;
    }

    private class webBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

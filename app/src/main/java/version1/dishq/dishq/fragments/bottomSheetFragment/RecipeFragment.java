package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import version1.dishq.dishq.R;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 29-12-2016.
 * Package name version1.dishq.dishq.
 */

public class RecipeFragment extends Fragment {

    private WebView wvRecipe;
    private RelativeLayout rlRecipe;
    private TextView recipeDishName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        wvRecipe = (WebView) v.findViewById(R.id.recipe_webview);
        rlRecipe = (RelativeLayout) v.findViewById(R.id.rl_recipe);
        recipeDishName = (TextView) v.findViewById(R.id.recipe_dish_name);

        recipeDishName.setText(Util.getRecipeDishName());
        Picasso.with(DishqApplication.getContext())
                .load(Util.getRecipeDishPhoto())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        rlRecipe.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        String url = Util.getRecipeUrl();
        wvRecipe.setWebViewClient(new webBrowser());
        wvRecipe.getSettings().setLoadsImagesAutomatically(true);
        wvRecipe.getSettings().setJavaScriptEnabled(true);
        wvRecipe.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvRecipe.loadUrl(Util.getRecipeUrl());

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

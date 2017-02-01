package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import version1.dishq.dishq.R;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 29-12-2016.
 * Package name version1.dishq.dishq.
 */

public class RecipeFragment extends Fragment {

    private ProgressBar progressBar;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        RelativeLayout rlNoRecipe = (RelativeLayout) v.findViewById(R.id.rl_no_recipe);
        TextView viewRecipe = (TextView) v.findViewById(R.id.view_recipe);
        viewRecipe.setTypeface(Util.opensansregular);
        Button loadInBrowser = (Button) v.findViewById(R.id.button_recipe);
        loadInBrowser.setTypeface(Util.opensansregular);
        TextView noRecipeText = (TextView) v.findViewById(R.id.no_recipe_text);
        noRecipeText.setTypeface(Util.opensansregular);
        progressBar = (ProgressBar) v.findViewById(R.id.recipe_bsf_progress);
        progressBar.setVisibility(View.VISIBLE);
        if (Util.getRecipeUrl() == null) {
            progressBar.setVisibility(View.GONE);
            rlNoRecipe.setVisibility(View.VISIBLE);
            loadInBrowser.setVisibility(View.GONE);
            viewRecipe.setVisibility(View.GONE);
        } else {
            rlNoRecipe.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            loadInBrowser.setVisibility(View.VISIBLE);
            viewRecipe.setVisibility(View.VISIBLE);
            loadInBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_VIEW);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setData(Uri.parse(Util.getRecipeUrl()));
                    startActivity(shareIntent);
                }
            });

        }
        return v;
    }


}

package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.content.Context;
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
    private RecyclerView recyclerView;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    private RecipeAdapter recipeAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recipe_bsf_recyclerview);
        RelativeLayout rlNoRecipe = (RelativeLayout) v.findViewById(R.id.rl_no_recipe);
        TextView noRecipeText = (TextView) v.findViewById(R.id.no_recipe_text);
        noRecipeText.setTypeface(Util.opensansregular);
        progressBar = (ProgressBar) v.findViewById(R.id.recipe_bsf_progress);
        progressBar.setVisibility(View.VISIBLE);
        if(Util.getRecipeUrl() == null) {
            progressBar.setVisibility(View.GONE);
            rlNoRecipe.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            rlNoRecipe.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recipeAdapter = new RecipeAdapter(getActivity());
            recyclerView.setAdapter(recipeAdapter);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
            progressBar.setVisibility(View.GONE);
            setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        }
        return v;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }

    public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeWebviewAdapter> {

        Context context;
        public RecipeAdapter(Context context) {
            this.context = context;
        }

        @Override
        public RecipeWebviewAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.recipe_webview, viewGroup, false);
            return new RecipeWebviewAdapter(itemView);
        }

        @Override
        public void onBindViewHolder(RecipeWebviewAdapter holder, int position) {

            holder.wvRecipe.setWebViewClient(new webBrowser());
            holder.wvRecipe.getSettings().setLoadsImagesAutomatically(true);
            holder.wvRecipe.getSettings().setJavaScriptEnabled(true);
            holder.wvRecipe.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            holder.wvRecipe.loadUrl(Util.getRecipeUrl());

        }

        private class webBrowser extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public class RecipeWebviewAdapter extends RecyclerView.ViewHolder {

            private WebView wvRecipe;

            public RecipeWebviewAdapter(View view) {
                super(view);
                wvRecipe = (WebView) view.findViewById(R.id.recipe_webview);
            }

        }
    }
}

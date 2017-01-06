package version1.dishq.dishq.ui;

import android.os.Bundle;

import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;

/**
 * Created by dishq on 05-01-2017.
 * Package name version1.dishq.dishq.
 */

public class FavouritesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        fetchDishFavourites();
    }

    private void fetchDishFavourites() {

    }
}

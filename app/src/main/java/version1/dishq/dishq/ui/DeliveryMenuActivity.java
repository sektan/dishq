package version1.dishq.dishq.ui;

import android.os.Bundle;

import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DeliveryMenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_menu);
        fetchDeliveryMenuInfo();
        setTags();
    }

    protected void setTags() {

    }

    private void fetchDeliveryMenuInfo() {

    }
}

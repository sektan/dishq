package version1.dishq.dishq.tastePrefFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment4 extends Fragment implements View.OnClickListener {

    public static TastePrefFragment4 newInstance(String text) {
        TastePrefFragment4 f = new TastePrefFragment4();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onClick(View view) {

    }
}

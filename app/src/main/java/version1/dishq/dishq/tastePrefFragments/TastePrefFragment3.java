package version1.dishq.dishq.tastePrefFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.wefika.flowlayout.FlowLayout;

import version1.dishq.dishq.modals.FavCuisinesModal;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment3 extends Fragment implements View.OnClickListener {

    private FavCuisinesModal favCuisinesModal;
    private FlowLayout favCuisineContainer;

    public static TastePrefFragment3 newInstance(String text) {
        TastePrefFragment3 f = new TastePrefFragment3();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onClick(View view) {

    }
}

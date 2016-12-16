package version1.dishq.dishq.tastePrefFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wefika.flowlayout.FlowLayout;

import version1.dishq.dishq.R;
import version1.dishq.dishq.modals.HomeCuisinesModal;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment2 extends Fragment implements View.OnClickListener{

    private HomeCuisinesModal homeCuisinesModal;
    private FlowLayout homeCuisineContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taste_pref_first, container, false);
        setTags(v);

        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        homeCuisineContainer = (FlowLayout) view.findViewById(R.id.home_cuisine_container);
    }

    public static TastePrefFragment2 newInstance(String text) {
        TastePrefFragment2 f = new TastePrefFragment2();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onClick(View view) {

    }
}

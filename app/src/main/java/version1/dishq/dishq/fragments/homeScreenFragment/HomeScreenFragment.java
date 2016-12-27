package version1.dishq.dishq.fragments.homeScreenFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import version1.dishq.dishq.R;
import version1.dishq.dishq.modals.DishDataModal;
import version1.dishq.dishq.server.Response.DishDataInfo;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 27-12-2016.
 * Package name version1.dishq.dishq.
 */

public class HomeScreenFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_BACKGROUND_IMAGE = "background_image";
    private RelativeLayout rlHomeScreen;

    public HomeScreenFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HomeScreenFragment newInstance(int sectionNumber) {
        HomeScreenFragment fragment = new HomeScreenFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        rlHomeScreen = (RelativeLayout) rootView.findViewById(R.id.rl_home_screen);
        for(DishDataModal modal : Util.dishDataModals){
            String imageUrl = modal.getDishPhoto().get(0);
            try {
                URL url = new URL(imageUrl);
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                BitmapDrawable ob = new BitmapDrawable(getResources(), image);
                rlHomeScreen.setBackground(ob);
            }catch (IOException e) {
                Log.d("Error", "Error caught");
            }

        }
        return rootView;
    }
}

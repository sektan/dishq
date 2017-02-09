package version1.dishq.dishq.custom;

/**
 * Created by dishq on 07-02-2017.
 * Package name version1.dishq.dishq.
 */

public class OnClickFilterCallbacks {


    public interface OnClickMoodFilterItemCallback {
        void onItemClicked(int position, boolean isAnyItemSelected);
    }

    public interface OnClickFoodFilterItemCallback {
        void onItemClicked(int position, boolean isAnyItemSelected);
    }


}

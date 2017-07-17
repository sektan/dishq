package version1.dishq.dishq;

/**
 * Created by tania sekhon on 30/12/16.
 */

public class OnClickCallbacks {

    public interface OnClickMoodFilterItemCallback {
        void onItemClicked(int position, boolean isAnyItemSelected);
    }

    public interface OnClickQuickFilterItemCallback {
        void onItemClicked(int position, boolean isAnyItemSelected);
    }
}

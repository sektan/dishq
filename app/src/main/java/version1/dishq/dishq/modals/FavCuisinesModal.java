package version1.dishq.dishq.modals;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class FavCuisinesModal {

    private String favCuisClassName;
    private Boolean favCuisCurrentSelect;
    private int favCuisEntityId;
    private String favCuisName;

    public FavCuisinesModal(String favCuisClassName, Boolean favCuisCurrentSelect, int favCuisEntityId,
                            String favCuisName) {
        this.favCuisClassName = favCuisClassName;
        this.favCuisCurrentSelect = favCuisCurrentSelect;
        this.favCuisEntityId = favCuisEntityId;
        this.favCuisName = favCuisName;
    }

    public String getFavCuisClassName() {
        return favCuisClassName;
    }

    public void setFavCuisClassName(String favCuisClassName) {
        this.favCuisClassName = favCuisClassName;
    }

    public Boolean getFavCuisCurrentSelect() {
        return favCuisCurrentSelect;
    }

    public void setFavCuisCurrentSelect(Boolean favCuisCurrentSelect) {
        this.favCuisCurrentSelect = favCuisCurrentSelect;
    }

    public int getFavCuisEntityId() {
        return favCuisEntityId;
    }

    public void setFavCuisEntityId(int favCuisEntityId) {
        this.favCuisEntityId = favCuisEntityId;
    }

    public String getFavCuisName() {
        return favCuisName;
    }

    public void setFavCuisName(String favCuisName) {
        this.favCuisName = favCuisName;
    }
}

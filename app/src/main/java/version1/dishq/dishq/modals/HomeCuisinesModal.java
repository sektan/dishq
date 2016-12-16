package version1.dishq.dishq.modals;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class HomeCuisinesModal {

    private String homeCuisClassName;
    private Boolean homeCuisCurrentSelect;
    private int homeCuisEntityId;
    private String homeCuisName;

    public HomeCuisinesModal(String homeCuisClassName, Boolean homeCuisCurrentSelect, int homeCuisEntityId,
                             String homeCuisName) {
        this.homeCuisClassName = homeCuisClassName;
        this.homeCuisCurrentSelect = homeCuisCurrentSelect;
        this.homeCuisEntityId = homeCuisEntityId;
        this.homeCuisName = homeCuisName;
    }

    public String getHomeCuisClassName() {
        return homeCuisClassName;
    }

    public void setHomeCuisClassName(String homeCuisClassName) {
        this.homeCuisClassName = homeCuisClassName;
    }

    public Boolean getHomeCuisCurrentSelect() {
        return homeCuisCurrentSelect;
    }

    public void setHomeCuisCurrentSelect(Boolean homeCuisCurrentSelect) {
        this.homeCuisCurrentSelect = homeCuisCurrentSelect;
    }

    public int getHomeCuisEntityId() {
        return homeCuisEntityId;
    }

    public void setHomeCuisEntityId(int homeCuisEntityId) {
        this.homeCuisEntityId = homeCuisEntityId;
    }

    public String getHomeCuisName() {
        return homeCuisName;
    }

    public void setHomeCuisName(String homeCuisName) {
        this.homeCuisName = homeCuisName;
    }
}

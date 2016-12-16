package version1.dishq.dishq.modals;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class AllergyModal {
    private String allergyClassName;
    private Boolean allergyCurrentlySelect;
    private int allergyEntityId;
    private String allergyName;
    private int allergyFoodChoice;

    public AllergyModal(String allergyClassName, Boolean allergyCurrentlySelect, int allergyEntityId, String allergyName, int allergyFoodChoice) {
        this.allergyClassName = allergyClassName;
        this.allergyCurrentlySelect = allergyCurrentlySelect;
        this.allergyEntityId = allergyEntityId;
        this.allergyName=allergyName;
        this.allergyFoodChoice = allergyFoodChoice;
    }

    public String getAllergyClassName() {
        return allergyClassName;
    }

    public void setAllergyClassName(String allergyClassName) {
        this.allergyClassName = allergyClassName;
    }

    public Boolean getAllergyCurrentlySelect() {
        return allergyCurrentlySelect;
    }

    public void setAllergyCurrentlySelect(Boolean allergyCurrentlySelect) {
        this.allergyCurrentlySelect = allergyCurrentlySelect;
    }
    public int getAllergyEntityId() {
        return allergyEntityId;
    }

    public void setAllergyEntityId(int allergyEntityId) {
        this.allergyEntityId = allergyEntityId;
    }

    public String getAllergyName() {
        return allergyName;
    }

    public int getAllergyFoodChoice() {
        return allergyFoodChoice;
    }

    public void setAllergyFoodChoice(int allergyFoodChoice) {
        this.allergyFoodChoice = allergyFoodChoice;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
//        if(o instanceof AllergyModal){
//            AllergyModal allergyModel = (AllergyModal) o;
//            return allergyModel.allergyEntityId.equals(this.allergyEntityId);
//        }
        return super.equals(o);
    }
}

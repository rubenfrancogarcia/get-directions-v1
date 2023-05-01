package garcia.ruben.personal_project.services.location;

import garcia.ruben.personal_project.entities.Location;
import garcia.ruben.personal_project.pojos.location.LocationPojo;

public interface GoogleMapsLocationsInterface {

    void saveLocation(LocationPojo locationPojo);

    boolean checkIfLocationSaved(LocationPojo locationPojo);

    public Location updateLocation();
}

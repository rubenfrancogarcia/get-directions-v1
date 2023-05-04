package garcia.ruben.personal_project.services.location;

import garcia.ruben.personal_project.entities.Location;
import garcia.ruben.personal_project.pojos.location.DirectionsPojo;
import garcia.ruben.personal_project.pojos.location.LocationPojo;
import garcia.ruben.personal_project.pojos.users.UserDataPojo;

public interface GoogleMapsLocationsInterface {

    void saveLocation(LocationPojo locationPojo);

    boolean checkIfLocationSaved(LocationPojo locationPojo);

    void getDirectionsWithRecommendations(DirectionsPojo directionsPojo);


}

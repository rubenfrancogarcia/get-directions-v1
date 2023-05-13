package garcia.ruben.personal_project.services.location;

import com.google.maps.model.DirectionsResult;
import garcia.ruben.personal_project.pojos.location.DirectionsPojo;
import garcia.ruben.personal_project.pojos.location.LocationPojo;

public interface GoogleMapsLocationsInterface {

    void saveLocation(LocationPojo locationPojo);

    Boolean checkIfLocationSaved(LocationPojo locationPojo);

    DirectionsResult getDirectionsWithRecommendations(DirectionsPojo directionsPojo);


}

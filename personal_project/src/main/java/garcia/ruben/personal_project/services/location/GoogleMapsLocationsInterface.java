package garcia.ruben.personal_project.services.location;

import com.google.maps.DirectionsApiRequest;
import garcia.ruben.personal_project.pojos.location.DirectionsPojo;
import garcia.ruben.personal_project.pojos.location.LocationPojo;

public interface GoogleMapsLocationsInterface {

    void saveLocation(LocationPojo locationPojo);

    Boolean checkIfLocationSaved(LocationPojo locationPojo);

    DirectionsApiRequest getDirectionsWithRecommendations(DirectionsPojo directionsPojo);


}

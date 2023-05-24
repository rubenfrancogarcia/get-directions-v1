package garcia.ruben.personal_project.pojos.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleMapsDirectionsServiceRequest {

    private String destination;

    private String origin;

    private String travelMode;

    private boolean optimizeWaypoints;

    private DirectionsWaypoint[] waypoints;
}

package garcia.ruben.personal_project.pojos.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.maps.model.PlacesSearchResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleRenderDirectionsPOJO {
    GoogleMapsDirectionsServiceRequest googleMapsDirectionsServiceRequest;

    ArrayList<?> placesInfo;
}

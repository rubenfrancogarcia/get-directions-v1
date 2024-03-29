package garcia.ruben.personal_project.pojos.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleRenderDirectionsPOJO {
    GoogleMapsDirectionsServiceRequest googleMapsDirectionsServiceRequest;

    List<Object> placesInfo;
}

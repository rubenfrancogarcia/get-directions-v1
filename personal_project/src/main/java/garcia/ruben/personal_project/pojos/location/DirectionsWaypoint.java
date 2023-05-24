package garcia.ruben.personal_project.pojos.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectionsWaypoint {
    private String location;
    private boolean stopover;
}

package garcia.ruben.personal_project.pojos.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectionsPojo {
    private Double startingLatitude;

    private Double startingLongitude;
    private String destination;
    private String startingPoint;
    private String username;
    private int numberOfWaypoints = 3;
}

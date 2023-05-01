package garcia.ruben.personal_project.pojos.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationPojo implements Serializable {
    private int id;

    private String googleMapsPlaceId;

    private Double latitude;

    private Double longitude;

    private String origin;

    private String destination;

    private List<String> types;
}

package garcia.ruben.personal_project.entities;

import com.google.maps.model.LatLng;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bounds {
    private LatLng northEast;

    private LatLng southWest;
}

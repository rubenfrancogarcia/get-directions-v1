package garcia.ruben.personal_project.entities;

import com.google.maps.model.LatLng;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
public class Bounds {
    private LatLng northEastBounds;

    private LatLng southWestBounds;
}

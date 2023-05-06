package garcia.ruben.personal_project.entities;

import com.google.maps.model.Bounds;
import com.google.maps.model.LatLng;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Getter
@Setter
public class ViewPort extends Bounds {
    private LatLng northEast;

    private LatLng southWest;
}

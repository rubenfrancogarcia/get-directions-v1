package garcia.ruben.personal_project.entities;

import com.google.maps.model.Bounds;
import com.google.maps.model.LatLng;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewPort extends Bounds {
    private LatLng northEast;

    private LatLng southWest;
}

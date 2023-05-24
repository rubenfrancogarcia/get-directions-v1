package garcia.ruben.personal_project.repository;

import garcia.ruben.personal_project.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationsRepository extends JpaRepository<Location, Integer> {
    Location findFirstByLatitudeAndLongitude(Double latitude, Double longitude);

    Location findFirstByPlaceId(String placeId);

    Location findFirstByFormattedAddress(String formattedAddress);
}

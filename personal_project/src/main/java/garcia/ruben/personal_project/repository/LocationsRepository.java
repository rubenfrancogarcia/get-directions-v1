package garcia.ruben.personal_project.repository;

import garcia.ruben.personal_project.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationsRepository extends JpaRepository<Location, Integer> {
}

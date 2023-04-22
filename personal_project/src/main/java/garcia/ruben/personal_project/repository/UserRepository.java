package garcia.ruben.personal_project.repository;

import garcia.ruben.personal_project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}

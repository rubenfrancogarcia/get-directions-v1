package garcia.ruben.personal_project.repository;

import garcia.ruben.personal_project.entities.User;
import garcia.ruben.personal_project.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData, Integer> {
    UserData FindByUser(User user);
}

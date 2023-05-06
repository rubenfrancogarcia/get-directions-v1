package garcia.ruben.personal_project.pojos.users;

import garcia.ruben.personal_project.entities.UserData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginUserPojo {
    private String username;
    private String password;

    private UserData userData;

    private String firstName;

    private String lastName;
}

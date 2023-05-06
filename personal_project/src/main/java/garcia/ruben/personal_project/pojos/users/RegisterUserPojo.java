package garcia.ruben.personal_project.pojos.users;

import garcia.ruben.personal_project.entities.UserData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class RegisterUserPojo {
    private String username;
    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private UserData userData;
}

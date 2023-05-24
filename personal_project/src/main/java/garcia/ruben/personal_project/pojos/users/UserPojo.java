package garcia.ruben.personal_project.pojos.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class UserPojo {
    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;
}

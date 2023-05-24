package garcia.ruben.personal_project.pojos.users;

import garcia.ruben.personal_project.entities.User;
import garcia.ruben.personal_project.utility.data.Keywords;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateDataPojo {

    private String username;

    private Set<Keywords> keywordsLikes = new HashSet<>();

    //saved routes possibly won't be saved;
    private Set<String> locationsOfInterest = new HashSet<>();

}

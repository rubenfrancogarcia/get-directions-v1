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

    private User user;

    private Set<Keywords> keywordsLikes = new HashSet<>();

    private Set<Keywords> keywordsDislikes = new HashSet<>();

    //saved routes possibly won't be saved;
    private List<LinkedList<String>> savedRoutes = new ArrayList<>();

    private Set<String> locationsOfInterest = new HashSet<>();

    private Set<String> locationsVisited = new HashSet<>();
}

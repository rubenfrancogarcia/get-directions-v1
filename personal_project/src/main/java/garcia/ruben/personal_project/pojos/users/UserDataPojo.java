package garcia.ruben.personal_project.pojos.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import garcia.ruben.personal_project.utility.data.Keywords;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDataPojo {
    private int id;

    private Set<Keywords> keywordsLikes = new HashSet<>();

    private Set<Keywords> keywordsDislikes = new HashSet<>();

    //saved routes possibly won't be saved;

    private Set<String> locationsOfInterest = new HashSet<>();

    private Set<String> locationsVisited = new HashSet<>();

    public String keywordsToString(){
        StringBuilder result = new StringBuilder();
        for (Keywords keywordsLike : keywordsLikes) {
            result.append(keywordsLike.toString()).append(", ");
        }
        return result.toString();
    }
}

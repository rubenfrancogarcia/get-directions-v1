package garcia.ruben.personal_project.entities;

import garcia.ruben.personal_project.utility.data.Keywords;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Data
@Table(name = "UserData")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserData implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(columnDefinition = "text")
    private Set<Keywords> keywordsLikes = new HashSet<>();

    @Lob
    @Column(columnDefinition = "text")
    private Set<Keywords> keywordsDislikes = new HashSet<>();

    //saved routes possibly won't be saved;
    @Lob
    @Column(columnDefinition = "text")
    private List<LinkedList<String>> savedRoutes = new ArrayList<>();

    @Lob
    @Column(columnDefinition = "text")
    private Set<String> locationsOfInterest = new HashSet<>();

    @Lob
    private Set<String> locationsVisited = new HashSet<>();
}

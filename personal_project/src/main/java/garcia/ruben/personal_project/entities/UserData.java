package garcia.ruben.personal_project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "UserData")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserData {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<String> keywordsLikes;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<String> keywordsDislikes;

    @ElementCollection
    private List<LinkedList<String>> savedRoutes;

    @ElementCollection(targetClass = Location.class)
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Location> locationsOfInterest;

    @ElementCollection(targetClass = Location.class)
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Location> locationsVisited;
}

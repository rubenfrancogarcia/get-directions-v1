package garcia.ruben.personal_project.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name="userdata")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserData {
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Id
    private int id;

    @OneToOne(mappedBy = "User.id", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private int userId;

    @Column
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<String> keywordsLikes;

    @Column
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<String> keywordsDislikes;

    @ManyToMany
    @JoinTable(
            name="user_location",
            joinColumns=@JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="location_id", referencedColumnName="id"))
    private List<Location[]> savedRoutes;
    @Column
    @ElementCollection
    private Set<Location> locationsOfInterest;

    @ElementCollection
    @Column
    private Set<Location> locationsVisited;
}

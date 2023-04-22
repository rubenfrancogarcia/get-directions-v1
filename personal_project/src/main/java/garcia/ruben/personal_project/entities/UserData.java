package garcia.ruben.personal_project.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="UserData")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserData {
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Id
    private int id;
    @OneToOne
    @Column
    private int userId;

    @Column
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<String> keywordsLikes;

    @Column
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<String> keywordsDislikes;

    @ElementCollection
    @CollectionTable(name="Location", joinColumns = @JoinColumn(name="id"))
    @OneToMany(mappedBy = "location", fetch= FetchType.LAZY)
    @Column(name="savedRoutes")
    private List<Location[]> savedRoutes;

    @ElementCollection
    @CollectionTable(name="Location", joinColumns = @JoinColumn(name="id"))
    @OneToMany(mappedBy = "location", fetch= FetchType.LAZY)
    @Column
    private List<Location> locationsOfInterest;

    @ElementCollection
    @CollectionTable(name="Location", joinColumns = @JoinColumn(name="id"))
    @OneToMany(mappedBy = "location", fetch= FetchType.LAZY)
    @Column
    private List<Location> locationsVisited;
}

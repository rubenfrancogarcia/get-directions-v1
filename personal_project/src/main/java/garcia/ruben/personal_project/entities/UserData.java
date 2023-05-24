package garcia.ruben.personal_project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import garcia.ruben.personal_project.utility.data.Keywords;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @JsonBackReference
    @OneToOne
    private User user;


    @CollectionTable(name = "user_keywords_like")
    private Set<Keywords> keywordsLikes = new HashSet<>();

    //saved routes possibly won't be saved;


    @CollectionTable(name= "user_saved_locations")
    private Set<String> locationsOfInterest = new HashSet<>();
}

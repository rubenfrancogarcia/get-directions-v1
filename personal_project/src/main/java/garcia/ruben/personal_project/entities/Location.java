package garcia.ruben.personal_project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//used for google maps place id; if id is saved peformance is better;
//for directions google maps destinations can do a lookup based on input strings
@Entity
@Table(name = "Location")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Location {
        //todo update entity for fields gained from PlaceApi
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String placeId;

    private Double latitude;

    private Double longitude;

    private String address;

    @Column(columnDefinition = "text")
    private List<String> types;

}

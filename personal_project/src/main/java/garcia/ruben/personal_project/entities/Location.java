package garcia.ruben.personal_project.entities;


import com.google.maps.model.Geometry;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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
@ToString
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String placeId;

    private String formattedAddress;

    private String name;

    //@Column(name = "view_port", columnDefinition = "text")
    //private ViewPort viewPort;
    //TODO these types were causing problems with being deserialize; needed to update to support spatial data

    //@Column(name = "bounds", columnDefinition = "text")
   // private Bounds bounds;

    private Double latitude;

    private Double longitude;

    //@Column(columnDefinition = "text")
    //private Geometry geometry;

    private String address;
    //@Column(columnDefinition = "text")
    //private List<String> types;
    //TODO deserialization error; will need to add an intermediate table for list; possible to make as clob

}

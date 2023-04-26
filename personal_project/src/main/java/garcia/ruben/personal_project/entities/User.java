package garcia.ruben.personal_project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String firstName;


    private String lastName;

    @Column( unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;


    @Column(unique = true)
    private String username;

    @OneToOne(mappedBy="user")
    private UserData userData;
}

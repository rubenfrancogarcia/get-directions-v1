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

    @Column(columnDefinition = "firstName")
    private String firstName;

    @Column(columnDefinition = "lastName")
    private String lastName;

    @Column(columnDefinition = "email", unique = true)
    private String email;

    @Column(unique = true, columnDefinition = "phoneNumber")
    private String phoneNumber;


    @Column(columnDefinition = "username", unique = true)
    private String username;

    @OneToOne(mappedBy="user")
    private UserData userData;
}

package garcia.ruben.personal_project.entities;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "users")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserData userData;
}

package garcia.ruben.personal_project.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import garcia.ruben.personal_project.pojos.users.UserPojo;
import garcia.ruben.personal_project.repository.UserDataRepository;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Table(name = "Users")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class User  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String firstName;


    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;


    @Column(unique = true)
    private String username;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private UserData userData;

    private String password;

}

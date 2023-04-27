package garcia.ruben.personal_project.controller;

import garcia.ruben.personal_project.pojos.users.LoginUserPojo;
import garcia.ruben.personal_project.pojos.users.OnboardUserPojo;
import garcia.ruben.personal_project.pojos.users.UserDataPojo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/V1/User")
public class UserController {

    @PostMapping(value = "/Login")
    public ResponseEntity<?> userLogin(@RequestBody LoginUserPojo loginUserPojo) {
        //todo
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PostMapping(value = "/Create")
    public ResponseEntity<?> createNewUser(@RequestBody OnboardUserPojo newUser) {
        //todo
        return new ResponseEntity<>(null,HttpStatus.OK);
    }


    @GetMapping(value = "/GetUser={userId}")
    public ResponseEntity<?> getUserData(@PathVariable("userId") int userId) {
        //todo
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PutMapping(value = "/UpdateUser={userId}")
    public ResponseEntity<?> updateUserData(@RequestBody UserDataPojo userDataPojo, @PathVariable("userId") int userId) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping(value = "/DeleteUser={userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") int userId) {
        //todo
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}

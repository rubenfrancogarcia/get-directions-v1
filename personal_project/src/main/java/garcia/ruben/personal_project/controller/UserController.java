package garcia.ruben.personal_project.controller;

import garcia.ruben.personal_project.pojos.users.*;
import garcia.ruben.personal_project.services.usermanagement.UserManagementWebAppImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/V1/User")
public class UserController {
    @Autowired
    UserManagementWebAppImpl userManagementWebApp;

    @PostMapping(value = "/Login")
    public ResponseEntity<?> userLogin(@RequestBody LogInRequest request) {
        LoginUserPojo userPojo = userManagementWebApp.login(request);
        if (userPojo != null) {
            return new ResponseEntity<>(userPojo, HttpStatus.OK);
        }
        return new ResponseEntity<>("wrong password or username. Try again", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/Create")
    public ResponseEntity<?> createNewUser(@RequestBody RegisterUserPojo newUser) {
        String result = userManagementWebApp.registerNewUser(newUser);
        if (result.equals("success")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/UpdateUserData")
    public ResponseEntity<?> userLogin(@RequestBody UpdateDataPojo updateDataPojo) {
        UpdateDataPojo userPojo = userManagementWebApp.updateUserData(updateDataPojo);
        if (userPojo != null) {
            return new ResponseEntity<>(userPojo, HttpStatus.OK);
        }
        return new ResponseEntity<>("Error updating", HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/GetUser={userId}")
    public ResponseEntity<?> getUserData(@PathVariable("userId") int userId) {
        //todo
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/UpdateUser={userId}")
    public ResponseEntity<?> updateUserData(@RequestBody UserDataPojo userDataPojo, @PathVariable("userId") int userId) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping(value = "/DeleteUser={userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") int userId) {
        //todo
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}

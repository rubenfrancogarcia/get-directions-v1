package garcia.ruben.personal_project.controller;

import garcia.ruben.personal_project.pojos.location.DirectionsPojo;
import garcia.ruben.personal_project.pojos.location.LocationPojo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/V1/Locations")
public class LocationController {
    @PostMapping("/GetLocation")
    public ResponseEntity<?> saveLocation(@RequestBody LocationPojo locationPojo) {
        //todo will check our database if we already have this info otherwise will call to google maps api place
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PostMapping("/GetDirections")
    public ResponseEntity<?> getDirections(@RequestBody DirectionsPojo directionsPojo) {
        //todo get from google maps api directions
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}

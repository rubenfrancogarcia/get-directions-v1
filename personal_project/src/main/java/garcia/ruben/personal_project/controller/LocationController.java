package garcia.ruben.personal_project.controller;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import garcia.ruben.personal_project.pojos.location.DirectionsPojo;
import garcia.ruben.personal_project.pojos.location.LocationPojo;
import garcia.ruben.personal_project.services.location.GoogleMapsLocationsWebAppImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/V1/Locations")
public class LocationController {
    @Autowired
    GoogleMapsLocationsWebAppImpl googleMapsLocationsWebApp;

    @PostMapping("/GetLocation")
    public ResponseEntity<?> saveLocation(@RequestBody LocationPojo locationPojo) {
        //todo will check our database if we already have this info otherwise will call to google maps api place
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/GetDirections")
    public ResponseEntity<?> getDirections(@RequestBody DirectionsPojo directionsPojo) {
        DirectionsResult directionsResult = googleMapsLocationsWebApp.getDirectionsWithRecommendations(directionsPojo);
        return new ResponseEntity<>(directionsResult, HttpStatus.OK);
    }

    @PostMapping("/TestGeoCoding")
    public ResponseEntity<?> testGeoCoding(@RequestBody DirectionsPojo directionsPojo){
        GeocodingResult[] result = googleMapsLocationsWebApp.getLocation("601 patton blvd Plano, Texas");
        return new ResponseEntity<>(result,HttpStatus.OK);
        //test via postman first
    }


}

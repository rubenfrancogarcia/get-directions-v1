package garcia.ruben.personal_project.controller;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.PlacesSearchResult;
import garcia.ruben.personal_project.pojos.location.DirectionsPojo;
import garcia.ruben.personal_project.pojos.location.GoogleMapsDirectionsServiceRequest;
import garcia.ruben.personal_project.pojos.location.GoogleRenderDirectionsPOJO;
import garcia.ruben.personal_project.pojos.location.LocationPojo;
import garcia.ruben.personal_project.services.location.GoogleMapsLocationsWebAppImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping(value = "/V1/Locations")
public class LocationController {
    private static final Logger logger = LogManager.getLogger(LocationController.class);

    @Autowired
    private GoogleMapsLocationsWebAppImpl googleMapsLocationsWebApp;

    @PostMapping("/GetLocation")
    public ResponseEntity<?> saveLocation(@RequestBody LocationPojo locationPojo) {

        //todo will check our database if we already have this info otherwise will call to google maps api place
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/GetDirections")
    public ResponseEntity<?> getDirections(@RequestBody DirectionsPojo directionsPojo) {
        logger.info("get directions called");
        DirectionsApiRequest directionsResult = googleMapsLocationsWebApp.getDirectionsWithRecommendations(directionsPojo);
        return new ResponseEntity<>(directionsResult, HttpStatus.OK);
    }

    @PostMapping("/TestGeoCoding")
    public ResponseEntity<?> testGeoCoding() {
        GeocodingResult[] result = googleMapsLocationsWebApp.getLocation("601 patton blvd Plano, Texas");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/TestPlaceAPIfromText")
    public ResponseEntity<?> testPlaceAPIfromText() {
        PlacesSearchResult result = googleMapsLocationsWebApp.googleMapsPlaceSearchFind("1729 coventry garden way Modesto Ca");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/GenerateDirectionsRequest")
    public ResponseEntity<?> GenerateDirectionsRequest(@RequestBody DirectionsPojo directionsPojo) {
        GoogleMapsDirectionsServiceRequest request = googleMapsLocationsWebApp.generateGoogleMapsRenderRequest(directionsPojo);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PostMapping("/GenerateDirectionsRequestAndPlacesInfo")
    public ResponseEntity<?> GenerateDirectionsRequestAndPlacesInfo(@RequestBody DirectionsPojo directionsPojo) {
        GoogleRenderDirectionsPOJO request = googleMapsLocationsWebApp.provideRenderingServiceInformation(directionsPojo);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

}

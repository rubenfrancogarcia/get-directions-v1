package garcia.ruben.personal_project.services.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import garcia.ruben.personal_project.entities.Bounds;
import garcia.ruben.personal_project.entities.Location;
import garcia.ruben.personal_project.entities.ViewPort;
import garcia.ruben.personal_project.pojos.location.*;
import garcia.ruben.personal_project.pojos.openai.ChatRequest;
import garcia.ruben.personal_project.pojos.openai.Message;
import garcia.ruben.personal_project.pojos.users.UserDataPojo;
import garcia.ruben.personal_project.repository.LocationsRepository;
import garcia.ruben.personal_project.repository.UserDataRepository;
import garcia.ruben.personal_project.repository.UserRepository;
import garcia.ruben.personal_project.services.openai.OpenAiWebAppImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleMapsLocationsWebAppImpl implements GoogleMapsLocationsInterface {
    private static final Logger logger = LogManager.getLogger(GoogleMapsLocationsWebAppImpl.class);

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;
    @Autowired
    private OpenAiWebAppImpl openAiWebAppImpl;

    private PlacesApi placesApi;

    private DirectionsApi directionsApi;

    @Autowired
    private LocationsRepository locationsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDataRepository userDataRepository;
    //private WebClient client = WebClient.create();
    private ObjectMapper objectMapper = new ObjectMapper();

    //these setups for using the java client
    /*public GeoApiContext geoApiContextInstance() {
        GeoApiContext.Builder builder = new GeoApiContext.Builder();
        builder = builder.apiKey(googleMapsApiKey);
        return builder.build();
    }*/
    @Autowired
    private GeoApiContext geoApiContextInstance = new GeoApiContext.Builder().apiKey(googleMapsApiKey).build();

    public List<GeocodingResult[]> gatherLocationsFromOpenAI(String[] recommendations) {
        List<GeocodingResult[]> results = new ArrayList<>();
        for (String location : recommendations) {
            GeocodingResult[] result = getLocation(location);
            results.add(result);
        }
        return results;
    }

    //geocode api https://www.javadoc.io/static/com.google.maps/google-maps-services/2.2.0/com/google/maps/GeocodingApiRequest.html
    public GeocodingResult[] getLocation(String address) {
        //format for reverse geocoding 24%20Sussex%20Drive%20Ottawa%20ON
        GeocodingResult[] geolocationResult = null;
        GeocodingApiRequest result = GeocodingApi.geocode(geoApiContextInstance, address);
        try {
            geolocationResult = result.await();
        } catch (Exception e) {
            logger.error(e);
        }
        return geolocationResult;
    }

    public GeocodingResult[] getLocation(LatLng latLng, GeoApiContext geocodingApi) {
        GeocodingResult[] geolocationResult = null;
        GeocodingApiRequest result = GeocodingApi.reverseGeocode(geoApiContextInstance, latLng);
        try {
            geolocationResult = result.await();
        } catch (Exception e) {
            logger.error(e);
        }
        return geolocationResult;
    }

    public GeocodingResult[] getLocationPlaceId(String placeId) {
        GeocodingResult[] geolocationResult = null;
        GeocodingApiRequest result = GeocodingApi.newRequest(geoApiContextInstance).place(placeId);
        try {
            geolocationResult = result.await();
        } catch (Exception e) {
            logger.error(e);
        }
        return geolocationResult;
    }

    //investigate for with multiple endpoints; can use Directions API java client or
    //create a method using newer Routes API for multiple waypoints https://developers.google.com/maps/documentation/routes/demo
    DirectionsApiRequest getDirections(String origin, String destination) {
        DirectionsApiRequest directions = null;
        try {
            directions = DirectionsApi.getDirections(geoApiContextInstance, origin, destination);
        } catch (Exception e) {
            logger.error("error while getting directions", e);
        }
        return directions;

    }

    DirectionsResult getDirections(String origin, String destination, List<FindPlaceFromText> waypoints) {
        //TODO create logic for when waypoints are do not return a list
        //for 3 waypoints; don't go over 3
        //assumes list passed in has a length of 3
        String placeId1 = waypoints.get(0).candidates[0].placeId;
        String placeId2 = waypoints.get(1).candidates[0].placeId;
        String placeId3 = waypoints.get(2).candidates[0].placeId;
        origin = "place_id:" + origin;
        destination = "place_id:" + destination;
        DirectionsApiRequest directions = DirectionsApi.getDirections(geoApiContextInstance, origin, destination);
        DirectionsResult directionsResult = null;
        try {
            directions = DirectionsApi.getDirections(geoApiContextInstance, origin, destination)
                    .waypointsFromPlaceIds(placeId1, placeId2, placeId3).departureTimeNow().optimizeWaypoints(true).mode(TravelMode.DRIVING);
            directionsResult = directions.await();
        } catch (Exception e) {
            logger.error("error while getting directions", e);
        }
        return directionsResult;

    }

    void prepForGoogleMapsRenderer(DirectionsResult directionsResult) {
        //not useful
        LatLng[] latLngBound = new LatLng[2];
        for (int i = 0; i < directionsResult.routes.length; i++) {
            com.google.maps.model.Bounds bounds = directionsResult.routes[i].bounds;
            LatLng sw = new LatLng(bounds.southwest.lat, bounds.southwest.lng);
            LatLng ne = new LatLng(bounds.northeast.lat, bounds.northeast.lng);
            //bounds = latLngBound;
        }
    }

    @Override
    public void saveLocation(LocationPojo locationPojo) {
        //todo experimental method used; refactor for only saving a entity, pojo, setup different methods based on type of input
        //reorganize logic
        //at most only starting destination and ending destination will be saved
        DirectionsResult results = null;
        try {
            results = DirectionsApi.getDirections(geoApiContextInstance, locationPojo.getOrigin(), locationPojo.getDestination()).await();

        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        GeocodedWaypoint[] geocodedWaypoints = results.geocodedWaypoints;
        DirectionsRoute[] directionsRoutes = results.routes;
        List<Location> newLocations = new ArrayList<>();
        int j = geocodedWaypoints.length; //length of geocodewaypoints
        logger.info("geocodedWaypoint {}" + Arrays.toString(geocodedWaypoints));
        for (GeocodedWaypoint geocodedWaypoint : geocodedWaypoints) {
            String placeId = geocodedWaypoint.placeId;
            Location location = (Location) locationsRepository.findByPlaceId(placeId);
            if (location == null) {
                Location newLocation = new Location();
                newLocation.setPlaceId(placeId);
                newLocations.add(newLocation);
            }
        }
        LatLng startLocation = directionsRoutes[0].legs[0].startLocation;
        LatLng endLocation = directionsRoutes[0].legs[0].endLocation;
        String startLocationAddress = directionsRoutes[0].legs[0].startAddress;
        String endLocationAddress = directionsRoutes[0].legs[0].endAddress;
        Location currLocation = newLocations.get(0);
        currLocation.setLatitude(startLocation.lat);
        currLocation.setLongitude(startLocation.lng);
        currLocation.setAddress(startLocationAddress);
        currLocation = newLocations.get(1);
        currLocation.setLatitude(endLocation.lat);
        currLocation.setLongitude(endLocation.lng);
        currLocation.setAddress(endLocationAddress);
        //this should be at most 2 elements snce waypoints will be only 2 if no additional waypoints supplied; first 2 elements will be start, end locations
        locationsRepository.saveAll(newLocations);

        DirectionsRoute[] routes = results.routes;
        logger.info("geocodedWaypoint {}" + Arrays.toString(routes));
    }

    @Override
    public Boolean checkIfLocationSaved(LocationPojo locationPojo) {
        Location dbLocation = (Location) locationsRepository.findByLatitudeAndLongitude(locationPojo.getLatitude(), locationPojo.getLongitude());
        if (dbLocation != null) {
            logger.info("location exists in db already");
            return true;
        } else {
            logger.info("location needs to be queried");
        }
        return false;
    }

    public Boolean checkIfLocationSaved(double latitude, double longitude) {
        Location dbLocation = locationsRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (dbLocation != null) {
            logger.info("location exists in db already");
            return true;
        } else {
            logger.info("location needs to be queried");
        }
        return false;
    }

    public Boolean checkIfLocationSaved(String placeId) {
        Location dbLocation = locationsRepository.findByPlaceId(placeId);
        if (dbLocation != null) {
            logger.info("location exists in db already");
            return true;
        } else {
            logger.info("location needs to be queried");
        }
        return false;
    }

    public PlacesSearchResult googleMapsPlaceSearchFind(String place) {
        //custom url for desired locations info https://maps.googleapis.com/maps/api/place/findplacefromtext/json?fields=formatted_address%2Cname%2Cgeometry%2Cplace_id%2Ctype&input=Museum%20of%20Contemporary%20Art%20Australia&inputtype=textquery&key=YOUR_API_KEY
        FindPlaceFromText result = null;
        try {
            result = PlacesApi.findPlaceFromText(geoApiContextInstance, place, FindPlaceFromTextRequest.InputType.TEXT_QUERY)
                    .fields(
                            FindPlaceFromTextRequest.FieldMask.PLACE_ID,
                            FindPlaceFromTextRequest.FieldMask.FORMATTED_ADDRESS,
                            FindPlaceFromTextRequest.FieldMask.NAME,
                            FindPlaceFromTextRequest.FieldMask.TYPES,
                            FindPlaceFromTextRequest.FieldMask.GEOMETRY
                    ).locationBias(new FindPlaceFromTextRequest.LocationBiasIP())
                    //location Bias can be used to restrict candidates
                    .await();
            if (result.candidates.length > 0) {
                //only uses first option, can optimize here for additional candidates
                Location newLocation = new Location();
                newLocation.setPlaceId(result.candidates[0].placeId);
                newLocation.setFormattedAddress(result.candidates[0].formattedAddress);
                ViewPort newViewPort = new ViewPort(result.candidates[0].geometry.viewport.northeast, result.candidates[0].geometry.viewport.southwest);
                newLocation.setViewPort(newViewPort);
                Bounds newBounds = new Bounds(result.candidates[0].geometry.bounds.northeast, result.candidates[0].geometry.bounds.southwest);
                newLocation.setBounds(newBounds);
                newLocation.setLongitude(result.candidates[0].geometry.location.lng);
                newLocation.setLatitude(result.candidates[0].geometry.location.lat);
                newLocation.setTypes(List.of(result.candidates[0].types));
                newLocation.setName(result.candidates[0].name);
                locationsRepository.save(newLocation);
                return result.candidates[0];
            }
        } catch (ApiException | InterruptedException | IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    List<PlacesSearchResult> googleMapsPlaceSearchFind(String[] places) {
        //custom url for desired locations info https://maps.googleapis.com/maps/api/place/findplacefromtext/json?fields=formatted_address%2Cname%2Cgeometry%2Cplace_id%2Ctype&input=Museum%20of%20Contemporary%20Art%20Australia&inputtype=textquery&key=YOUR_API_KEY
        //format for place lookup
        List<PlacesSearchResult> results = new ArrayList<>();
        // PlacesSearchResult candidate = response.candidates[0];
        for (String string : places) {
            PlacesSearchResult result = googleMapsPlaceSearchFind(string);
            results.add(result);
        }
        return results;
    }


    @Override
    public DirectionsApiRequest getDirectionsWithRecommendations(DirectionsPojo directionsPojo) {
        //GeocodingResult[] startingDestinationInfo = getLocation(directionsPojo.getStartingPoint());
        Location startingLocation;
        //todo check if starting and ending location is there if it's saved in locations table then use the placeId
        Location newLocation = null;
        if (directionsPojo.getStartingLongitude() != null && directionsPojo.getStartingLatitude() != null) {
            if (!checkIfLocationSaved(directionsPojo.getStartingLatitude(), directionsPojo.getStartingLongitude())) {
                newLocation = new Location();
                GeocodingResult[] startingDestinationInfo = getLocation(directionsPojo.getStartingPoint());
                newLocation.setPlaceId(startingDestinationInfo[0].placeId);
                newLocation.setFormattedAddress(startingDestinationInfo[0].formattedAddress);
                newLocation.setLongitude(startingDestinationInfo[0].geometry.location.lng);
                newLocation.setLatitude(startingDestinationInfo[0].geometry.location.lat);
                ViewPort newViewPort = new ViewPort(startingDestinationInfo[0].geometry.viewport.northeast, startingDestinationInfo[0].geometry.viewport.southwest);
                newLocation.setViewPort(newViewPort);
                Bounds newBounds = new Bounds(startingDestinationInfo[0].geometry.bounds.northeast, startingDestinationInfo[0].geometry.bounds.southwest);
                newLocation.setBounds(newBounds);
                newLocation.setName(startingDestinationInfo[0].formattedAddress);
                newLocation.setGeometry(startingDestinationInfo[0].geometry);
                if (!checkIfLocationSaved(newLocation.getPlaceId())) {
                    locationsRepository.save(newLocation);
                }
            }

        }
        GeocodingResult[] endDestinationInfo = getLocation(directionsPojo.getDestination());
        Location newEndLocation = null;
        if (endDestinationInfo.length > 0) {
            if (!checkIfLocationSaved(directionsPojo.getDestination())) {
                newEndLocation = new Location();
                newEndLocation.setPlaceId(endDestinationInfo[0].placeId);
                newEndLocation.setFormattedAddress(endDestinationInfo[0].formattedAddress);
                newEndLocation.setLongitude(endDestinationInfo[0].geometry.location.lng);
                newEndLocation.setLatitude(endDestinationInfo[0].geometry.location.lat);
                ViewPort newViewPort = new ViewPort(endDestinationInfo[0].geometry.viewport.northeast, endDestinationInfo[0].geometry.viewport.southwest);
                newLocation.setViewPort(newViewPort);
                Bounds newBounds = new Bounds(endDestinationInfo[0].geometry.bounds.northeast, endDestinationInfo[0].geometry.bounds.southwest);
                newLocation.setBounds(newBounds);
                newEndLocation.setName(endDestinationInfo[0].formattedAddress);
                newEndLocation.setGeometry(endDestinationInfo[0].geometry);
                if (!checkIfLocationSaved(newEndLocation.getPlaceId())) {
                    locationsRepository.save(newEndLocation);
                }
            }
        }
        //can optimize by using placeId to input the directions;
        //and use geocodoing api to find info
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("gpt-3.5-turbo");
        chatRequest.setTemperature(1.1);
        chatRequest.setMax_tokens(120);
        Message[] messages = new Message[2];
        Message assistantMessage = new Message();
        UserDataPojo userDataPojo = directionsPojo.getUserDataPojo();
        if (userDataPojo == null) {
            //query from database userid
            logger.warn("user data from request is null");
        }

        int amount = 3;
        //todo add customisation to fix this
        String assistantContent = "You are a travel guide recommending" + amount + " interesting locations based on the user's prompt data and between the starting and end destination.Your response should only return the name and address of the location";
        assistantMessage.setRole("assistant");
        assistantMessage.setContent(assistantContent);
        Message userMessage = new Message();
        userMessage.setRole("user");
        //modify to actually use user info
        String userContent = "user likes:" + (userDataPojo != null ? userDataPojo.keywordsToString() : "all kinds of places") + "; Origin:" + directionsPojo.getStartingPoint() + ". Destination: " + directionsPojo.getDestination() + " Your response should only return the name and address of the location";
        userMessage.setContent(userContent);
        messages[1] = userMessage;
        messages[0] = assistantMessage;
        chatRequest.setMessages(messages);
        //ChatResponse generatedRecommendations = openAiWebAppImpl.outputRecommendations(chatRequest);
        //logger.info("generated recommendations response from openAI {} ", generatedRecommendations);
        //these lines process the lines of strings from chatGPT and outputs a list of locations information derived from those strings
        //String[] stringAddresses = openAiWebAppImpl.processChatResponseObject(generatedRecommendations);
        String[] stringAddresses = new String[]{"golden Chick, plano, TX ", "house of blues, dallas, texas", "rodeo goat, plano Texas"};
        logger.info("stringAddress generated from openAI {} ", stringAddresses);
        List<PlacesSearchResult> chatGptRecommendationsGoogleInfo = googleMapsPlaceSearchFind(stringAddresses);
        //figure out how to process this and return it in a neat fashion
        //todo not urgent since not using this anymore this ain't using chatgpt recommendations as is
        return getDirections(newLocation != null ? newLocation.getPlaceId() : directionsPojo.getStartingPoint(), newEndLocation != null ? newEndLocation.getPlaceId() : directionsPojo.getDestination());
    }

    //this method provides request to be used with chatgpt waypoints and saves places to our db
    public GoogleMapsDirectionsServiceRequest generateGoogleMapsRenderRequest(DirectionsPojo directionsPojo) {
        //GeocodingResult[] startingDestinationInfo = getLocation(directionsPojo.getStartingPoint());
        Location startingLocation;
        //todo check if starting and ending location is there if it's saved in locations table then use the placeId
        Location newLocation = null;
        if (directionsPojo.getStartingLongitude() != null && directionsPojo.getStartingLatitude() != null) {
            if (!checkIfLocationSaved(directionsPojo.getStartingLatitude(), directionsPojo.getStartingLongitude())) {
                newLocation = new Location();
                GeocodingResult[] startingDestinationInfo = getLocation(directionsPojo.getStartingPoint());
                newLocation.setPlaceId(startingDestinationInfo[0].placeId);
                newLocation.setFormattedAddress(startingDestinationInfo[0].formattedAddress);
                newLocation.setLongitude(startingDestinationInfo[0].geometry.location.lng);
                newLocation.setLatitude(startingDestinationInfo[0].geometry.location.lat);
                ViewPort newViewPort = new ViewPort(startingDestinationInfo[0].geometry.viewport.northeast, startingDestinationInfo[0].geometry.viewport.southwest);
                newLocation.setViewPort(newViewPort);
                Bounds newBounds = new Bounds(startingDestinationInfo[0].geometry.bounds.northeast, startingDestinationInfo[0].geometry.bounds.southwest);
                newLocation.setBounds(newBounds);
                newLocation.setName(startingDestinationInfo[0].formattedAddress);
                newLocation.setGeometry(startingDestinationInfo[0].geometry);
                if (!checkIfLocationSaved(newLocation.getPlaceId())) {
                    locationsRepository.save(newLocation);
                }
            }
        }
        GeocodingResult[] endDestinationInfo = getLocation(directionsPojo.getDestination());
        Location newEndLocation = null;
        if (endDestinationInfo.length > 0) {
            if (!checkIfLocationSaved(directionsPojo.getDestination())) {
                newEndLocation = new Location();
                newEndLocation.setPlaceId(endDestinationInfo[0].placeId);
                newEndLocation.setFormattedAddress(endDestinationInfo[0].formattedAddress);
                newEndLocation.setLongitude(endDestinationInfo[0].geometry.location.lng);
                newEndLocation.setLatitude(endDestinationInfo[0].geometry.location.lat);
                ViewPort newViewPort = new ViewPort(endDestinationInfo[0].geometry.viewport.northeast, endDestinationInfo[0].geometry.viewport.southwest);
                newLocation.setViewPort(newViewPort);
                Bounds newBounds = new Bounds(endDestinationInfo[0].geometry.bounds.northeast, endDestinationInfo[0].geometry.bounds.southwest);
                newLocation.setBounds(newBounds);
                newEndLocation.setName(endDestinationInfo[0].formattedAddress);
                newEndLocation.setGeometry(endDestinationInfo[0].geometry);
                if (!checkIfLocationSaved(newEndLocation.getPlaceId())) {
                    locationsRepository.save(newEndLocation);
                }
            }
        }
        //can optimize by using placeId to input the directions;
        //and use geocodoing api to find info
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("gpt-3.5-turbo");
        chatRequest.setTemperature(1.1);
        chatRequest.setMax_tokens(120);
        Message[] messages = new Message[2];
        Message assistantMessage = new Message();
        UserDataPojo userDataPojo = directionsPojo.getUserDataPojo();
        if (userDataPojo == null) {
            //query from database userid
            logger.warn("user data from request is null");
        }

        int amount = 3;
        String assistantContent = "You are a travel guide recommending" + amount + " interesting locations based on the user's prompt data and between the starting and end destination.Your response should only return the name and address of the location";
        assistantMessage.setRole("assistant");
        assistantMessage.setContent(assistantContent);
        Message userMessage = new Message();
        userMessage.setRole("user");
        String userContent = "user likes:" + (userDataPojo != null ? userDataPojo.keywordsToString() : "all kinds of places") + "; Origin:" + directionsPojo.getStartingPoint() + ". Destination: " + directionsPojo.getDestination() + " Your response should only return the name and address of the location";
        userMessage.setContent(userContent);
        messages[1] = userMessage;
        messages[0] = assistantMessage;
        chatRequest.setMessages(messages);
        //ChatResponse generatedRecommendations = openAiWebAppImpl.outputRecommendations(chatRequest);
        //logger.info("generated recommendations response from openAI {} ", generatedRecommendations);
        //these lines process the lines of strings from chatGPT and outputs a list of locations information derived from those strings
        //String[] stringAddresses = openAiWebAppImpl.processChatResponseObject(generatedRecommendations);
        String[] stringAddresses = new String[]{"golden Chick, plano, TX ", "house of blues, dallas, texas", "rodeo goat, plano Texas"};
        logger.info("stringAddress generated from openAI {} ", stringAddresses);
        List<PlacesSearchResult> chatGptRecommendationsGoogleInfo = googleMapsPlaceSearchFind(stringAddresses);
        //figure out how to process this and return it in a neat fashion
        GoogleMapsDirectionsServiceRequest googleMapsDirectionsServiceRequest = new GoogleMapsDirectionsServiceRequest();
        //place_id: prefix all of them with this
        googleMapsDirectionsServiceRequest.setDestination("place_id:" + newEndLocation.getPlaceId());
        googleMapsDirectionsServiceRequest.setOrigin("place_id:" + newLocation.getPlaceId());
        DirectionsWaypoint[] waypoints = new DirectionsWaypoint[chatGptRecommendationsGoogleInfo.size()];
        for (int i = 0; i < waypoints.length; i++) {
            DirectionsWaypoint point = new DirectionsWaypoint();
            waypoints[i] = point;
            point.setLocation("place_id:" + chatGptRecommendationsGoogleInfo.get(i).placeId);
        }
        logger.info("waypoints place ids {}", waypoints);
        googleMapsDirectionsServiceRequest.setWaypoints(waypoints);
        googleMapsDirectionsServiceRequest.setTravelMode("DRIVING");
        googleMapsDirectionsServiceRequest.setOptimizeWaypoints(true);
        logger.info("request object for service request {}", googleMapsDirectionsServiceRequest);
        return googleMapsDirectionsServiceRequest;
    }

    //this service is for our page that will render the directions and provide additional information for said places
    public GoogleRenderDirectionsPOJO provideRenderingServiceInformation(DirectionsPojo directionsPojo) {
        //todo check if starting and ending location is there if it's saved in locations table then use the placeId
        Location newLocation = null;
        if (directionsPojo.getStartingLongitude() != null && directionsPojo.getStartingLatitude() != null) {
            if (!checkIfLocationSaved(directionsPojo.getStartingLatitude(), directionsPojo.getStartingLongitude())) {
                newLocation = new Location();
                GeocodingResult[] startingDestinationInfo = getLocation(directionsPojo.getStartingPoint());
                newLocation.setPlaceId(startingDestinationInfo[0].placeId);
                newLocation.setFormattedAddress(startingDestinationInfo[0].formattedAddress);
                newLocation.setLongitude(startingDestinationInfo[0].geometry.location.lng);
                newLocation.setLatitude(startingDestinationInfo[0].geometry.location.lat);
                ViewPort newViewPort = new ViewPort(startingDestinationInfo[0].geometry.viewport.northeast, startingDestinationInfo[0].geometry.viewport.southwest);
                newLocation.setViewPort(newViewPort);
                Bounds newBounds = new Bounds(startingDestinationInfo[0].geometry.bounds.northeast, startingDestinationInfo[0].geometry.bounds.southwest);
                newLocation.setBounds(newBounds);
                newLocation.setName(startingDestinationInfo[0].formattedAddress);
                newLocation.setGeometry(startingDestinationInfo[0].geometry);
                if (!checkIfLocationSaved(newLocation.getPlaceId())) {
                    locationsRepository.save(newLocation);
                }
            } else {
                newLocation = locationsRepository.findByLatitudeAndLongitude(directionsPojo.getStartingLatitude(), directionsPojo.getStartingLongitude());
            }

        }
        GeocodingResult[] endDestinationInfo = getLocation(directionsPojo.getDestination());
        Location newEndLocation = null;
        if (endDestinationInfo.length > 0) {
            if (!checkIfLocationSaved(directionsPojo.getDestination())) {
                newEndLocation = new Location();
                newEndLocation.setPlaceId(endDestinationInfo[0].placeId);
                newEndLocation.setFormattedAddress(endDestinationInfo[0].formattedAddress);
                newEndLocation.setLongitude(endDestinationInfo[0].geometry.location.lng);
                newEndLocation.setLatitude(endDestinationInfo[0].geometry.location.lat);
                ViewPort newViewPort = new ViewPort(endDestinationInfo[0].geometry.viewport.northeast, endDestinationInfo[0].geometry.viewport.southwest);
                newLocation.setViewPort(newViewPort);
                Bounds newBounds = new Bounds(endDestinationInfo[0].geometry.bounds.northeast, endDestinationInfo[0].geometry.bounds.southwest);
                newLocation.setBounds(newBounds);
                newEndLocation.setName(endDestinationInfo[0].formattedAddress);
                newEndLocation.setGeometry(endDestinationInfo[0].geometry);
                if (!checkIfLocationSaved(newEndLocation.getPlaceId())) {
                    locationsRepository.save(newEndLocation);
                }
            } else {
                newEndLocation = locationsRepository.findByFormattedAddress(directionsPojo.getDestination());
            }
        }
        //can optimize by using placeId to input the directions;
        //and use geocodoing api to find info
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("gpt-3.5-turbo");
        chatRequest.setTemperature(1.1);
        chatRequest.setMax_tokens(120);
        Message[] messages = new Message[2];
        Message assistantMessage = new Message();
        UserDataPojo userDataPojo = directionsPojo.getUserDataPojo();
        if (userDataPojo == null) {
            //query from database userid
            logger.warn("user data from request is null");
        }

        int amount = 3;
        String assistantContent = "You are a travel guide recommending" + amount + " interesting locations based on the user's prompt data and between the starting and end destination.Your response should only return the name and address of the location";
        assistantMessage.setRole("assistant");
        assistantMessage.setContent(assistantContent);
        Message userMessage = new Message();
        userMessage.setRole("user");
        String userContent = "user likes:" + (userDataPojo != null ? userDataPojo.keywordsToString() : "all kinds of places") + "; Origin:" + directionsPojo.getStartingPoint() + ". Destination: " + directionsPojo.getDestination() + " Your response should only return the name and address of the location";
        userMessage.setContent(userContent);
        messages[1] = userMessage;
        messages[0] = assistantMessage;
        chatRequest.setMessages(messages);
        //ChatResponse generatedRecommendations = openAiWebAppImpl.outputRecommendations(chatRequest);
        //logger.info("generated recommendations response from openAI {} ", generatedRecommendations);
        //these lines process the lines of strings from chatGPT and outputs a list of locations information derived from those strings
        //String[] stringAddresses = openAiWebAppImpl.processChatResponseObject(generatedRecommendations);
        String[] stringAddresses = new String[]{"golden Chick, plano, TX ", "house of blues, dallas, texas", "rodeo goat, plano Texas"};
        //TODO uncomment when done testing; Hard  code some values for testing to avoid api rate limit
        logger.info("stringAddress generated from openAI {} ", stringAddresses);
        List<PlacesSearchResult> chatGptRecommendationsGoogleInfo = googleMapsPlaceSearchFind(stringAddresses);
        List<Object> placesInfo = new ArrayList<>(chatGptRecommendationsGoogleInfo);
        placesInfo.add(newLocation);
        placesInfo.add(newEndLocation);
        FindPlaceFromText startingPointPlaceInfo = new FindPlaceFromText();
        //figure out how to process this and return it in a neat fashion
        GoogleMapsDirectionsServiceRequest googleMapsDirectionsServiceRequest = new GoogleMapsDirectionsServiceRequest();
        //place_id: prefix all of them with this
        googleMapsDirectionsServiceRequest.setDestination(newEndLocation.getPlaceId());
        googleMapsDirectionsServiceRequest.setOrigin(newLocation.getPlaceId());
        DirectionsWaypoint[] waypoints = new DirectionsWaypoint[chatGptRecommendationsGoogleInfo.size()];
        for (int i = 0; i < waypoints.length; i++) {
            DirectionsWaypoint point = new DirectionsWaypoint();
            waypoints[i] = point;
            point.setLocation(chatGptRecommendationsGoogleInfo.get(i).placeId);
        }
        logger.info("waypoints place ids {}", waypoints);
        googleMapsDirectionsServiceRequest.setWaypoints(waypoints);
        googleMapsDirectionsServiceRequest.setTravelMode("DRIVING");
        googleMapsDirectionsServiceRequest.setOptimizeWaypoints(true);
        logger.info("request object for service request {}", googleMapsDirectionsServiceRequest);
        GoogleRenderDirectionsPOJO googleRenderDirectionsPOJO = new GoogleRenderDirectionsPOJO();
        googleRenderDirectionsPOJO.setGoogleMapsDirectionsServiceRequest(googleMapsDirectionsServiceRequest);
        return googleRenderDirectionsPOJO;
    }
}




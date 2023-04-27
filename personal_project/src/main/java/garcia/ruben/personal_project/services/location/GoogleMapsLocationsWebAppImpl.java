package garcia.ruben.personal_project.services.location;

import garcia.ruben.personal_project.entities.Location;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.logging.Logger;

public class GoogleMapsLocationsWebAppImpl implements GoogleMapsLocationsInterface {
    private static final Logger logger = (Logger) LogManager.getLogger(GoogleMapsLocationsWebAppImpl.class);
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Override
    public void saveLocation() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=Museum%20of%20Contemporary%20Art%20Australia&inputtype=textquery&fields=formatted_address%2Cname%2Crating%2Copening_hours%2Cgeometry&key=" + googleMapsApiKey)
                .method("GET", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            logger.info("response to calling google maps api sample url " + response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //https://maps.googleapis.com/maps/api/place/findplacefromtext/json?parameters
        //: "restaurant" or "123 Main Street". This must be a place name, address, or category of establishments.
        //the type of input. This can be one of either textquery or phonenumber. Phone numbers must be in international format
        // (prefixed by a plus sign ("+"), followed by the country code, then the phone number itself)

    }

    @Override
    public boolean checkIfLocationSaved() {
        return false;
    }

    @Override
    public Location updateLocation() {
        return null;
    }
}

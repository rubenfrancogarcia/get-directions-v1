package garcia.ruben.personal_project.utility.google;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

@Scope("singleton")
public class GoogleMapsCommon {
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    public GeoApiContext setupGeoApiContext() {
        GeoApiContext.Builder builder = new GeoApiContext.Builder();
        builder = builder.apiKey(googleMapsApiKey);
        return builder.build();
    }
}

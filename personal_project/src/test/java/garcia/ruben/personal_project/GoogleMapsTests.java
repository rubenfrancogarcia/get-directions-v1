package garcia.ruben.personal_project;

import com.google.maps.GeoApiContext;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleMapsTests {
    private MockWebServer server;
    private GeoApiContext.Builder builder;

    @Before
    public void Setup() {
        server = new MockWebServer();
        builder = new GeoApiContext.Builder().apiKey("AIza...").queryRateLimit(500);
    }

    @Test
    public void contextLoads() {
    }

}

package garcia.ruben.personal_project;

import garcia.ruben.personal_project.services.location.GoogleMapsLocationsWebAppImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan
public class PersonalProjectApplication {
	@Autowired
	private GoogleMapsLocationsWebAppImpl googleMapsLocationsWebApp;
	public static void main(String[] args) {
		SpringApplication.run(PersonalProjectApplication.class, args);
	}

}

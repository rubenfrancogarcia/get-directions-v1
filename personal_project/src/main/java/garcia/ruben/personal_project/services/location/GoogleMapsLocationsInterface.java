package garcia.ruben.personal_project.services.location;

import garcia.ruben.personal_project.entities.Location;

public interface GoogleMapsLocationsInterface {
    public void saveLocation();

    public boolean checkIfLocationSaved();

    public Location updateLocation();
}

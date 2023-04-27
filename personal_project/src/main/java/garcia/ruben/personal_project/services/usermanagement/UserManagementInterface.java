package garcia.ruben.personal_project.services.usermanagement;

import garcia.ruben.personal_project.pojos.users.LoginUserPojo;
import garcia.ruben.personal_project.pojos.users.RegisterUserPojo;

public interface UserManagementInterface {
    //TODO integrate redis to cache all info from all entities of said user
    public LoginUserPojo login(LoginUserPojo credentialsPojo);

    public void registerNewUser(RegisterUserPojo registerUserPojo);

    public void confirmUserEmailService();

    public void confirmUserPhoneNumberService();

    public void onboardNewUserService();

    public void saveLocationToFave();

    public void saveCustomRoute();

    public void updateUserDataKeywords();

}

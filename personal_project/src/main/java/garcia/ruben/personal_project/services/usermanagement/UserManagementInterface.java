package garcia.ruben.personal_project.services.usermanagement;

import garcia.ruben.personal_project.pojos.users.LogInRequest;
import garcia.ruben.personal_project.pojos.users.LoginUserPojo;
import garcia.ruben.personal_project.pojos.users.RegisterUserPojo;
import garcia.ruben.personal_project.pojos.users.UpdateDataPojo;

public interface UserManagementInterface {
    //TODO integrate redis to cache all info from all entities of said user

    LoginUserPojo login(LogInRequest credentialsPojo);

    public String registerNewUser(RegisterUserPojo registerUserPojo);

    public UpdateDataPojo updateUserData(UpdateDataPojo updateDataPojo);

    public void confirmUserEmailService();

    public void confirmUserPhoneNumberService();

}

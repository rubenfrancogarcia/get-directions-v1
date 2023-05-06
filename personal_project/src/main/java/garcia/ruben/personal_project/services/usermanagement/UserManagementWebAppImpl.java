package garcia.ruben.personal_project.services.usermanagement;

import garcia.ruben.personal_project.entities.User;
import garcia.ruben.personal_project.entities.UserData;
import garcia.ruben.personal_project.pojos.users.LoginUserPojo;
import garcia.ruben.personal_project.pojos.users.RegisterUserPojo;
import garcia.ruben.personal_project.pojos.users.UpdateDataPojo;
import garcia.ruben.personal_project.repository.UserDataRepository;
import garcia.ruben.personal_project.repository.UserRepository;
import garcia.ruben.personal_project.utility.security.Security;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagementWebAppImpl implements UserManagementInterface {
    private static final Logger logger = LogManager.getLogger(UserManagementWebAppImpl.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDataRepository userDataRepository;

    Security securityClass;

    PasswordEncoder passwordEncoder;

    @Autowired
    public void getPasswordEncoder() {
        this.passwordEncoder = securityClass.passwordEncoder();
    }

    @Override
    public LoginUserPojo login(LoginUserPojo credentialsPojo) {
        //TODO just return all the partial user data in a Pojo
        User user = userRepository.findByUsername(credentialsPojo.getUsername());
        UserData userData = userDataRepository.FindByUser(user);
        if (user != null) {
            if (passwordEncoder.matches(credentialsPojo.getPassword(), user.getPassword())) {
                LoginUserPojo loginUserPojo = new LoginUserPojo();
                loginUserPojo.setFirstName(user.getFirstName());
                loginUserPojo.setLastName(user.getLastName());
                loginUserPojo.setUsername(user.getUsername());
                loginUserPojo.setUserData(userData);
                return loginUserPojo;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public String registerNewUser(RegisterUserPojo registerUserPojo) {
        //todo do validations to make sure no records for unique columns aren't being used again otherwise db will throw exception
        User newUser = new User();
        newUser.setEmail(registerUserPojo.getEmail());
        newUser.setFirstName(registerUserPojo.getFirstName());
        newUser.setLastName(registerUserPojo.getLastName());
        newUser.setPhoneNumber(registerUserPojo.getPhoneNumber());
        newUser.setUsername(registerUserPojo.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerUserPojo.getPassword()));
        try {
            User user = userRepository.save(newUser);
            logger.info("new user saved {}", user);
            return "success";
        } catch (Exception e) {
            logger.error(e);
        }
        return "retry";
    }

    @Override
    public UpdateDataPojo updateUserData(UpdateDataPojo updateDataPojo) {
        try {
            User user = userRepository.findByUsername(updateDataPojo.getUsername());
            UserData userData = userDataRepository.FindByUser(user);
            userData.setKeywordsDislikes(updateDataPojo.getKeywordsDislikes());
            userData.setKeywordsLikes(updateDataPojo.getKeywordsLikes());
            userData.setLocationsVisited(userData.getLocationsVisited());
            userData.setLocationsOfInterest(updateDataPojo.getLocationsOfInterest());
            userDataRepository.save(userData);
            return updateDataPojo;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    @Override
    public void confirmUserEmailService() {

    }

    @Override
    public void confirmUserPhoneNumberService() {

    }

    @Override
    public void onboardNewUserService() {

    }


    @Override
    public void saveLocationToFave() {

    }

    @Override
    public void saveCustomRoute() {

    }

    @Override
    public void updateUserDataKeywords() {

    }
}

package garcia.ruben.personal_project.services.usermanagement;

import garcia.ruben.personal_project.entities.User;
import garcia.ruben.personal_project.entities.UserData;
import garcia.ruben.personal_project.pojos.users.LogInRequest;
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
    private UserRepository userRepository;
    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private Security securityClass;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void getPasswordEncoder() {
        this.passwordEncoder = securityClass.passwordEncoder();
    }

    @Override
    public LoginUserPojo login(LogInRequest credentialsPojo) {
        User user = userRepository.findFirstByUsername(credentialsPojo.getUsername());
        if (user != null) {
            if (passwordEncoder.matches(credentialsPojo.getPassword(), user.getPassword())) {
                LoginUserPojo loginUserPojo = new LoginUserPojo();
                loginUserPojo.setUserPojo(user);
                //setting entities for faster dev
                return loginUserPojo;
            } else {
                logger.info("wrong password");
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
            UserData newUserDataInit = new UserData();
            newUserDataInit.setUser(user);
            userDataRepository.save(newUserDataInit);
            logger.info("new user saved {}", user);
            return "success";
        } catch (Exception e) {
            logger.error(e);
        }
        return "retry with different username or phone number or email";
    }

    @Override
    public UpdateDataPojo updateUserData(UpdateDataPojo updateDataPojo) {
        try {
            User user = userRepository.findFirstByUsername(updateDataPojo.getUsername());
            UserData userData = userDataRepository.findByUser(user);
            userData.setKeywordsLikes(updateDataPojo.getKeywordsLikes());
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

}

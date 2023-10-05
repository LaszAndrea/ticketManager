package hu.me.transformer;

import hu.me.domain.User;
import hu.me.model.UserModel;
import org.springframework.stereotype.Component;


@Component
public class UserTransformer {

    public UserModel transformUserToUserModel(User user) {
        UserModel userModel = new UserModel();
        if(user != null) {
            userModel.setFullName(user.getFullName());
            userModel.setPhoneNumber(user.getPhoneNumber());
            userModel.setCredentials(user.getCredentials());
            userModel.setRole(user.getRole());
            userModel.setReservedTimes(user.getReservedTimes());
            userModel.setReviews(user.getReviews());
            if (user.getId() != null)
                userModel.setId(user.getId());
        }
        return userModel;
    }

    public User transformUserModelToUser(UserModel userModel) {
        User user = new User();
        if(userModel.getId()!=null)
            user.setId(userModel.getId());
        user.setReservedTimes(userModel.getReservedTimes());
        user.setCredentials(userModel.getCredentials());
        user.setRole(userModel.getRole());
        user.setReviews(userModel.getReviews());
        user.setFullName(userModel.getFullName());
        user.setPhoneNumber(userModel.getPhoneNumber());
        return user;
    }

}

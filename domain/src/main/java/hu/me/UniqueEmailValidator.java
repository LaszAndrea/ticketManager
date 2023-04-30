package hu.me;

import hu.me.domain.User;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolationException;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if(userRepository!=null)
            return email != null && isEmailUnique(userRepository.findByCredentialsLoginName(email));
        else
            return true;
    }

    public boolean isEmailUnique(User user_n) {
        User user;
        if(user_n!=null) {
            user = userRepository.findByCredentialsLoginName(user_n.getCredentials().getLoginName());
            return user == null;
        }else
            return true;
    }

}

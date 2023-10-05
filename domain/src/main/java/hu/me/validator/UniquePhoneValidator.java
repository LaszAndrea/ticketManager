package hu.me.validator;

import hu.me.domain.User;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if(userRepository!=null) {
            return phone != null &&
                    isPhoneNumberUnique(phone);
        }else
            return true;
    }

    public boolean isPhoneNumberUnique(String phone) {
        User givenUser = userRepository.findByPhoneNumber(phone);
        User user;

        if(givenUser!=null) {
            user = userRepository.findByPhoneNumber(givenUser.getPhoneNumber());
            return user == null;
        }else
            return true;

    }

}

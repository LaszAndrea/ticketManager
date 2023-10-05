package hu.me.validator;

import hu.me.domain.User;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if(userRepository!=null) {
            return email != null &&
                    isEmailUnique(email)
                    && isEmailValid(email);
        }else
            return true;

    }

    public boolean isEmailUnique(String email) {
        User givenUser = userRepository.findByCredentialsLoginName(email);
        User user;

        if(givenUser!=null) {
            user = userRepository.findByCredentialsLoginName(givenUser.getCredentials().getLoginName());
            return user == null;
        }else
            return true;

    }

    public boolean isEmailValid(String email) {
        String[] kukac = null;

        if(email.contains("@")) {

            kukac = email.split("@");

            if (!kukac[1].equalsIgnoreCase("gmail.com") && !kukac[1].equalsIgnoreCase("freemail.hu") &&
                    !kukac[1].equalsIgnoreCase("gmail.hu") && !kukac[1].equalsIgnoreCase("student.uni-miskolc.hu")
                    && !kukac[1].equalsIgnoreCase("citromail.hu") && !kukac[1].equalsIgnoreCase("yahoo.com")) {

                return false;
            }else {
                return true;
            }
        }else
            return false;

    }


}

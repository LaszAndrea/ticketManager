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
                    isPhoneNumberUnique(phone) && isPhoneNumberValid(phone);
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

    public boolean isPhoneNumberValid(String phone){

        String[] tel = phone.split("\\+");
        int length = tel[0].length();

        if(tel!=null){
            if(tel[0].equalsIgnoreCase("")){

                if(tel[1].contains("36")){
                    length = tel[1].length();
                    if(length != 11){
                        return false;
                    }
                } else{
                    return false;
                }

            } else if(length != 11){
                return false;
            }
        }

        return true;

    }

}

package hu.me.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniquePhoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhone {

    String message() default "Ez a telefonszám már regisztrálva van, vagy nem megfelelő.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

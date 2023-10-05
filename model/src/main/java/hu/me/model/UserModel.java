package hu.me.model;

import hu.me.domain.Credentials;
import hu.me.domain.Review;
import hu.me.domain.Role;
import hu.me.domain.Time;
import hu.me.validator.UniqueEmail;
import hu.me.validator.UniquePhone;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class UserModel {

    private Long id;
    private String fullName;
    @UniquePhone
    private String phoneNumber;
    private Role role;
    @Valid
    private Credentials credentials;
    private List<Review> reviews;
    private List<Time> reservedTimes;

}

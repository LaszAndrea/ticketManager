package hu.me.model;

import hu.me.domain.Credentials;
import hu.me.domain.Review;
import hu.me.domain.Role;
import hu.me.domain.Time;
import lombok.Data;
import java.util.List;

@Data
public class UserModel {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private Role role;
    private Credentials credentials;
    private List<Review> reviews;
    private List<Time> reservedTimes;

}

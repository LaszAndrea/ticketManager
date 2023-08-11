package hu.me.model;

import hu.me.domain.User;
import lombok.Data;
import hu.me.domain.Sights;

import javax.validation.constraints.NotNull;

@Data
public class ReviewModel {

    private Long id;
    @NotNull(message = "Must not be null")
    private int rating;
    private String comment;
    private User user;
    private Sights sights;

}

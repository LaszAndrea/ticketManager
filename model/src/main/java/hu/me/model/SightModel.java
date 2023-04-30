package hu.me.model;

import hu.me.domain.Category;
import hu.me.domain.Review;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SightModel {
    private Long id;
    @NotNull(message = "Must not be null")
    @NotBlank(message = "Destination name must not be blank")
    private String name;
    @NotNull(message = "Must not be null")
    @NotBlank(message = "Destination country must not be blank")
    private String description;
    @NotNull(message = "Must not be null")
    @NotBlank(message = "Destination country must not be blank")
    private String address;
    private Category category;
    private List<Review> reviewList;
}

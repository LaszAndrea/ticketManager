package hu.me.model;

import hu.me.domain.Category;
import hu.me.domain.Review;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SightModel {

    private Long id;

    @NotNull(message = "Must not be null")
    @NotEmpty(message = "Kérem adja meg a nevet!")
    private String name;

    @NotNull(message = "Must not be null")
    @NotEmpty(message = "Kérem adja meg a leírást!")
    private String description;

    @NotNull(message = "Must not be null")
    @NotEmpty(message = "Kérem adja meg a címet!")
    private String address;

    private Category category;

    private List<Review> reviewList;

    /*private String firstPic;

    private String secondPic;

    private String thirdPic;

    private String fourthPic;*/

}

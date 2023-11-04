package hu.me.model;

import hu.me.domain.Category;
import hu.me.domain.Genre;
import hu.me.domain.Time;
import hu.me.domain.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MovieModel {

    private Long id;

    @NotNull(message = "Must not be null")
    @NotEmpty(message = "Kérem adja meg a nevet!")
    private String name;

    @NotNull(message = "Kérem adja meg a korhatárt!")
    @Min(value = 6, message = "A korhatárnak legalább 6-nak kell lennie!")
    private int age;

    @NotNull(message = "Must not be null")
    @NotEmpty(message = "Kérem adja meg a leírást!")
    private String description;

    private Genre genre;

    private Category category;

    private List<Time> times;

}

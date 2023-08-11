package hu.me.model;

import hu.me.domain.Genre;
import hu.me.domain.Time;
import hu.me.domain.User;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class MovieModel {

    private Long id;

    private String name;

    private int age;

    private String description;

    private Genre genre;

    private List<User> user;

    private List<Time> times;

}

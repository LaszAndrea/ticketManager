package hu.me.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="_2_Movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 15)
    private String name;
    @Column(nullable = true, length = 2)
    private int age;
    @Column(nullable = false, length = 300)
    private String description;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "movies")
    private List<User> user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<Time> times;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}

package hu.me.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="_1_Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 40)
    private String fullName;
    @Column(nullable = false, length = 12)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Embedded
    @Valid
    private Credentials credentials;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Review> reviews;
    /*@ManyToMany
    @JoinTable(
            name = "_3_Reserved",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> movies;*/

    @ManyToMany
    @JoinTable(
            name = "_3_Reserved",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "time_id"))
    private List<Time> reservedTimes;

    public void setId(Long id) {
        this.id = id;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Time> getReservedTimes() {
        return reservedTimes;
    }

    public void setReservedTimes(List<Time> reservedTimes) {
        this.reservedTimes = reservedTimes;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /*public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }*/

    public User(long id, String name, Role role, Credentials credentials) {
        this.id = id;
        this.fullName = name;
        this.role = role;
        this.credentials = credentials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && fullName.equals(user.fullName) && role == user.role && credentials.equals(user.credentials) && reviews.equals(user.reviews)  /* &&movies.equals(user.movies)*/;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, role, credentials, reviews /*,movies*/);
    }
}

package hu.me.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="_4_Time")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime time_date;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movieTime")
    private List<Seat> seats;
    @ManyToOne
    private Movie movie;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "reservedTimes")
    private List<User> user;

    public List<User> getUsersThisTime() {
        return user;
    }

    public void setUsersThisTime(List<User> usersThisTime) {
        this.user = usersThisTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTime_date() {
        return time_date;
    }

    public void setTime_date(LocalDateTime time_date) {
        this.time_date = time_date;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

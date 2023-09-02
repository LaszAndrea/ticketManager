package hu.me.model;

import hu.me.domain.Movie;
import hu.me.domain.Seat;
import hu.me.domain.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TimeModel {

    private Long id;

    private LocalDateTime time_date;

    private List<Seat> seats;

    private List<User> user;

    private Movie movie;

}

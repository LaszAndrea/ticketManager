package hu.me.repository;

import hu.me.domain.Seat;
import hu.me.domain.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAll();

    List<Seat> findByMovieTime(Time time_date);

}

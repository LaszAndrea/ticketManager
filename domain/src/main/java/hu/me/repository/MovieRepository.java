package hu.me.repository;

import hu.me.domain.Movie;
import hu.me.domain.Sights;
import hu.me.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAll();

    Movie findMovieById(long id);

    //List<Movie> findMovieByUserId(long id);

    //List<Movie> findMoviesByUser(User user);

    @Query(value = "select * from _2_Movie where LOWER(name) like (%:keyword%)", nativeQuery = true)
    List<Movie> findByKeyword(@Param("keyword") String keyword);

}

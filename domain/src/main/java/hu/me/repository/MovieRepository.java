package hu.me.repository;

import hu.me.domain.Movie;
import hu.me.domain.Sights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAll();

    Movie findMovieById(long id);

}

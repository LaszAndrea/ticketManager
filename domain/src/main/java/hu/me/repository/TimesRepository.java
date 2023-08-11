package hu.me.repository;

import hu.me.domain.Time;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimesRepository extends JpaRepository<Time, Long> {

    List<Time> findAll();
    List<Time> findByMovieId(long id);
    Time findTimeById(long id);

}

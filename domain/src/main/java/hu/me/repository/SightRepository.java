package hu.me.repository;

import hu.me.domain.Sights;
import hu.me.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SightRepository extends JpaRepository<Sights, Long> {

    List<Sights> findAll();

    Sights findSightsById(long id);

}

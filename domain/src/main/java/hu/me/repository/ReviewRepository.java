package hu.me.repository;

import hu.me.domain.Review;
import hu.me.domain.Sights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAll();

}

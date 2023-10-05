package hu.me.repository;

import hu.me.domain.Review;
import hu.me.domain.Sights;
import hu.me.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAll();
    List<Review> findBySightId(long id);

    List<Review> findByUser(User user);

}

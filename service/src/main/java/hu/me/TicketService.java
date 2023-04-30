package hu.me;

import hu.me.domain.Review;
import hu.me.domain.Sights;
import hu.me.domain.User;
import hu.me.repository.ReviewRepository;
import hu.me.repository.SightRepository;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService implements TicketServiceInterface {

    private User loggedInUser;
    @Autowired
    private UserRepository uRep;
    @Autowired
    private SightRepository sightRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    private List<Sights> sightsList;
    private List<User> userList;
    private List<Review> reviewList;

    public TicketService() {}

    @Override
    public User getLoggedInUser() {
        return loggedInUser;
    }
    public List<Sights> getSights(){
        return sightRepository.findAll();
    }

    public List<Review> getReviewList(){
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsBySightId(long sightId) {

        List<Review> reviewsAll = reviewRepository.findAll();
        List<Review> reviewsNeeded = new ArrayList<>();

        for (Review r : reviewsAll) {
            if(sightId == r.getSight().getId()) {
                reviewsNeeded.add(r);
            }
        }

        return reviewsNeeded;

    }

    public User findUserByUsername(String username) {

        userList = uRep.findAll();

        return userList.stream()
                .filter(user -> user.getCredentials().getLoginName().equals(username))
                .findFirst().orElse(null);

    }
    public void save(User user) {

        uRep.save(user);

    }

    public void addSight(Sights sight) {

        long currentMax = sightsList.stream()
                .map(Sights::getId)
                .max(Long::compare)
                .orElse(0L);
        sight.setId(currentMax + 1);
        sightRepository.save(sight);

    }

    public void addReview(Review review) {

        long currentMax = reviewList.stream()
                .map(Review::getId)
                .max(Long::compare)
                .orElse(0L);
        review.setId(currentMax + 1);
        reviewRepository.save(review);

    }



}

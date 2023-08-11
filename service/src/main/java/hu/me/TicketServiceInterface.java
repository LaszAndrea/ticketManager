package hu.me;

import hu.me.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface TicketServiceInterface {

    User getLoggedInUser();
    List<Sights> getSights();
    List<Movie> getMovies();
    List<Time> getTimes();
    List<Review> getReviewList();
    List<Review> getReviewsBySightId(long sightId);
    User findUserByUsername(String username);
    void save(User user);
    void addSight(Sights sight);
    void addReview(Review review);
    List<Review> findLatests(long sightId);
    long getMaxSightId();
    void saveFile(String uploadDir, String fileName,MultipartFile multipartFile) throws IOException ;
    void uploadImages(MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4, String[] imageNames) throws IOException ;
    void setCategory(String category, Sights sight);
    String[] checkImages(String images[]);
    Sights findSightById(long id);
    Movie findMovieById(long id);
    Time findTimeById(long id);
    List<Sights> findAllSights();
    void deleteSight(long id);
    String dayOfWeek(Time time);
    Map<String, String> tickets();

}

package hu.me;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import hu.me.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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


    User findUserById(long userId);

    void save(User user);

    void addSight(Sights sight);

    void updateSight(Sights sight);

    void addReview(Review review);

    List<Review> findLatests(long sightId);

    long getMaxSightId();

    void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException;

    void uploadImages(MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4, String[] imageNames) throws IOException;

    void setCategory(String category, Sights sight);

    String[] checkImages(String images[]);

    Sights findSightById(long id);

    Movie findMovieById(long id);

    Time findTimeById(long id);

    List<Sights> findAllSights();

    void deleteSight(long id);

    String dayOfWeek(Time time);

    Map<String, String> tickets();

    List<Movie> filters(String keyword, String selectedDate, String genre);

    List<Seat> getSeatsForTime(Time time);

    void reservation(Time time, User user);

    List<Time> getReservationsForUser(User user);

    void updateUser(User user);

    List<News> gatherNews();

    byte[] generateQRCodeBytes(String data) throws WriterException, IOException;

    List<Review> getUserReviews(User user);

    void getRandomSeats(long timeId);

    long getMaxSeatId();

    List<Time> filterTimes(String date);

    void reserveSeats(String[] seatArray);

    long getMaxMovieId();

    void addMovieToRepository(Movie movie);
    long getMaxTimeId();

    void addTimeToRepository(Time time);

    void checkMovieImages(MultipartFile image1, String imageName) throws IOException;

    void changeMovieDetails(Movie movie);

    void changeTimeDetails(Time time);

    void deleteMovie(Movie movie);

}

package hu.me;

import hu.me.domain.*;
import hu.me.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static hu.me.domain.Category.RESTAURANT;
import static hu.me.domain.Category.SIGHT;

@Service
public class TicketService implements TicketServiceInterface {

    private User loggedInUser;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SightRepository sightRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private TimesRepository timesRepository;
    @Autowired
    private SeatRepository seatRepository;

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

    public List<Movie> getMovies(){
        return movieRepository.findAll();
    }

    public List<Time> getTimes(){
        return timesRepository.findAll();
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

        userList = userRepository.findAll();

        return userList.stream()
                .filter(user -> user.getCredentials().getLoginName().equals(username))
                .findFirst().orElse(null);

    }
    public void save(User user) {

        String pw_hash = BCrypt.hashpw(user.getCredentials().getPassword(), BCrypt.gensalt());
        user.getCredentials().setPassword(pw_hash);

        userRepository.save(user);

    }

    public void addSight(Sights sight) {

        sight.setId(getMaxSightId()+1);
        sightRepository.save(sight);

    }

    public void updateSight(Sights sight) {

        sightRepository.save(sight);

    }

    public void addReview(Review review) {

        reviewRepository.save(review);

    }

    public List<Review> findLatests(long sightId){

        List<Review> latests = new ArrayList<>();
        reviewList = reviewRepository.findBySightId(sightId);
        List<Review> reversed = IntStream
                .range(0, reviewList.size()).map(i -> reviewList.size() - 1-i)
                .mapToObj(reviewList::get)
                .collect(Collectors.toList());
        int db = 0;

        for (Review review : reversed) {
            if(db<3){
                latests.add(review);
                db++;
            }
        }

        return latests;

    }

    public long getMaxSightId(){


        if(sightRepository.findAll().size()>0){
            long max = sightRepository.findAll().get(0).getId();

            for (Sights sight: sightRepository.findAll()) {
                if(sight.getId() > max){
                    max = sight.getId();
                }
            }

            return max;
        }

        return 0;

    }

    public void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Nem lehetett elmenteni a képet: " + fileName, ioe);
        }
    }

    public void uploadImages(MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4, String[] imageNames) throws IOException {

        String uploadDir = "app/src/main/resources/static/images/";

        saveFile(uploadDir, imageNames[0], image1);
        saveFile(uploadDir, imageNames[1], image2);
        saveFile(uploadDir, imageNames[2], image3);
        saveFile(uploadDir, imageNames[3], image4);
    }

    public void setCategory(String category, Sights sight) {
        if(category.equalsIgnoreCase("sight")){
            sight.setCategory(SIGHT);
        }else {
            sight.setCategory(RESTAURANT);
        }
    }

    public String[] checkImages(String images[]) {
        long id = getMaxSightId() + 1;

        String expected_names[] = {
                id + ".jpg",
                id + "_1.jpg",
                id + "_2.jpg",
                id + "_3.jpg"
        };

        for (int i = 0; i < expected_names.length; i++) {
            String expectedName = expected_names[i];
            String imageName = images[i];

            if (!imageName.equals(expectedName)) {
                images[i] = expectedName;
            }
        }

        return images;
    }

    public Movie findMovieById(long id){
        return movieRepository.findMovieById(id);
    }
    public Time findTimeById(long id){
        return timesRepository.findTimeById(id);
    }

    public Sights findSightById(long id){
        return sightRepository.findSightsById(id);
    }

    public void deleteSight(long id){
        sightRepository.deleteById(id);
    }

    public List<Sights> findAllSights(){
        return  sightRepository.findAll();
    }

    public String dayOfWeek(Time time){

        String dayName = switch (time.getTime_date().getDayOfWeek()) {
            case MONDAY -> "hétfő";
            case TUESDAY -> "kedd";
            case WEDNESDAY -> "szerda";
            case THURSDAY -> "csütörtök";
            case FRIDAY -> "péntek";
            case SATURDAY -> "szombat";
            case SUNDAY -> "vasárnap";
        };

        return dayName;

    }

    public Map<String, String> tickets() {

        String types[] = {"Teljesárú jegy", "Diákjegy", "Junior jegy", "Jegy fogyatékkal élők részére", "Senior jegy"};
        String prices[] = {"1950", "1650", "1650", "1650", "1650"};

        Map<String, String> typePrice = new HashMap<>();

        for (int i=0; i<types.length; i++){
            typePrice.put(types[i], prices[i]);
        }

        return typePrice;

    }

    public List<Movie> filters(String keyword, LocalDate selectedDate){

        if(keyword!=null) {
            return movieRepository.findByKeyword(keyword);
        }
        else
            return movieRepository.findAll();
    }

    public List<Seat> getSeatsForTime(Time time_date){
        return seatRepository.findByMovieTime(time_date);
    }

    /*public void addMovie(Movie movie, User user) {

        List<User> usersSeeingMovie = movie.getUser();
        usersSeeingMovie.add(user);
        movie.setUser(usersSeeingMovie);
        movieRepository.save(movie);

        List<Movie> moviesUserSeeing = user.getMovies();
        moviesUserSeeing.add(movie);
        user.setMovies(moviesUserSeeing);
        userRepository.save(user);

    }*/

    public void reservation(Time time, User user){

        List<User> usersSeeingMovieAtTime = time.getUsersThisTime();
        usersSeeingMovieAtTime.add(user);
        time.setUsersThisTime(usersSeeingMovieAtTime);
        timesRepository.save(time);

        List<Time> timesReserved = user.getReservedTimes();
        timesReserved.add(time);
        user.setReservedTimes(timesReserved);
        userRepository.save(user);

    }

    /*public List<Movie> getMoviesForUser(User user){
        return movieRepository.findMoviesByUser(user);
    }*/

    public List<Time> getReservationsForUser(User user){
        return timesRepository.findTimeByUser(user);
    }

}

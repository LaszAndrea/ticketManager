package hu.me;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import hu.me.domain.*;
import hu.me.repository.*;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

    public User findUserById(long userId){
        return userRepository.findById(userId);
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

    public List<Time> getReservationsForUser(User user){
        return timesRepository.findTimeByUser(user);
    }

    public void updateUserPhoneNumber(User user){
        userRepository.save(user);
    }

    public List<News> gatherNews() {
        List<News> newsList = new ArrayList<>();

        try (final WebClient webClient = new WebClient()) {

            // Engedélyezzük a JavaScript futtatását, ha szükséges (most pont nem, mert akkor hibákat dob)
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(false);

            // Oldal letöltése
            HtmlPage page = webClient.getPage("https://www.nyiregyhaza.hu/category/helyi-hirek");

            // Az oldal tartalma HTML-ként
            String pageContent = page.asXml();

            // HTML tartalom JSoup segítségével történő elemzése
            Document document = Jsoup.parse(pageContent);

            // Címek kinyerése
            Elements titles = document.select(".post-item .hide-on-480 .title a");
            Elements descriptions = document.select(".description");
            Elements imagesDiv = document.select(".post-item .hide-on-480 .post-item-image a img");
            Elements dates = document.select(".post-meta span");

            int indicator = 0;
            // Címek kiíratása
            do {

                boolean flag = false;
                String imageURL = imagesDiv.get(indicator).attr("data-src");
                String hrefLink = titles.get(indicator).attr("href");
                News news = new News(titles.get(indicator).text(), descriptions.get(indicator).text(),
                        imageURL, dates.get(indicator).text(), hrefLink);

                if(indicator >0 && newsList.size()<12) {
                    for (int i = 0; i<newsList.size(); i++) {
                        if (newsList.get(i).getName().equalsIgnoreCase(news.getName())) {
                            flag = true;
                            break;
                        }
                    }
                }

                if(!flag){
                    newsList.add(news);
                }

                indicator++;

            }while(newsList.size()<12);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }

    public byte[] generateQRCodeBytes(String data) throws WriterException, IOException {
        int width = 300;
        int height = 300;
        Writer writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    public List<Review> getUserReviews(User user){
        return reviewRepository.findByUser(user);
    }

    public boolean isEmailUnique(String email) {
        User givenUser = userRepository.findByCredentialsLoginName(email);
        User user;

        if(givenUser!=null) {
            user = userRepository.findByCredentialsLoginName(givenUser.getCredentials().getLoginName());
            return user == null;
        }else
            return true;

    }

    public boolean isEmailValid(String email) {
        String[] kukac = null;

        if(email.contains("@")) {

            kukac = email.split("@");

            if (!kukac[1].equalsIgnoreCase("gmail.com") && !kukac[1].equalsIgnoreCase("freemail.hu") &&
                    !kukac[1].equalsIgnoreCase("gmail.hu") && !kukac[1].equalsIgnoreCase("student.uni-miskolc.hu")
                    && !kukac[1].equalsIgnoreCase("citromail.hu") && !kukac[1].equalsIgnoreCase("yahoo.com")) {

                return false;
            }else {
                return true;
            }
        }else
            return false;

    }

    public boolean isPhoneNumberUnique(String phone) {
        User givenUser = userRepository.findByPhoneNumber(phone);
        User user;

        if(givenUser!=null) {
            user = userRepository.findByPhoneNumber(givenUser.getPhoneNumber());
            return user == null;
        }else
            return true;

    }


}

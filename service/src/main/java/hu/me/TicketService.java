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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    public List<Movie> filters(String keyword, String date, String genre){

        List<Movie> movies = movieRepository.findAll();
        List<Movie> filteredMovies = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateParsed = LocalDate.parse(date, formatter);

        for(int i=0; i<movies.size(); i++){
            for(int j=0; j<movies.get(i).getTimes().size(); j++){
                if(LocalDate.parse(movies.get(i).getTimes().get(j).getTime_date().toString().substring(0,10), formatter).equals(dateParsed)){
                    if (keyword == null || keyword.isEmpty() || movies.get(i).getName().toLowerCase().contains(keyword.toLowerCase())) {
                        if(genre.equalsIgnoreCase("összes") || movies.get(i).getGenre().toString().equalsIgnoreCase(genre)) {
                            filteredMovies.add(movies.get(i));
                            break;
                        }
                    }
                }
            }
        }

        return filteredMovies;

    }

    public List<Time> filterTimes(String date){

        List<Time> times = timesRepository.findAll();
        List<Time> filteredTimes = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateParsed = LocalDate.parse(date, formatter);

        for(int i=0; i<times.size(); i++){
                if(LocalDate.parse(times.get(i).getTime_date().toString().substring(0,10), formatter).equals(dateParsed)) {
                    filteredTimes.add(times.get(i));
                }
        }

        return filteredTimes;

    }


    public List<Seat> getSeatsForTime(Time time_date){
        return seatRepository.findByMovieTime(time_date);
    }

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

            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(false);

            HtmlPage page = webClient.getPage("https://www.nyiregyhaza.hu/category/helyi-hirek");

            String pageContent = page.asXml();

            Document document = Jsoup.parse(pageContent);

            Elements titles = document.select(".post-item .hide-on-480 .title a");
            Elements descriptions = document.select(".description");
            Elements imagesDiv = document.select(".post-item .hide-on-480 .post-item-image a img");
            Elements dates = document.select(".post-meta span");

            int indicator = 0;
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

    public void getRandomSeats(long timeId){

        Time time = timesRepository.findTimeById(timeId);
        Random rand = new Random();

        int random = rand.nextInt(36, 96);
        int dividedByTwelve = random % 12;

        long maxId = getMaxSeatId() + 1;

        if(time.getSeats().size()==0) {
            if (dividedByTwelve != 0) {
                random += 12 - dividedByTwelve;
            }

            for (int i = 0; i < random; i++) {
                seatRepository.save(new Seat(maxId + Long.valueOf(i), true, time));
            }

        }

    }

    public long getMaxSeatId(){

        if(seatRepository.findAll().size()>0) {
            long max = seatRepository.findAll().get(0).getId();

            for (Seat seat : seatRepository.findAll()) {
                if (seat.getId() > max) {
                    max = seat.getId();
                }
            }


            return max;
        }else
            return 0;

    }

    public void reserveSeats(String[] seatArray){

        for (int i=0; i<seatArray.length; i++) {
            if(!(seatArray[i].isEmpty() || seatArray[i].equalsIgnoreCase(""))) {
                long id = Long.parseLong(seatArray[i]);
                seatRepository.findById(id).setFree(false);
                seatRepository.save(seatRepository.findById(id));
            }
        }

    }

    public long getMaxMovieId(){

        if(movieRepository.findAll().size()>0) {
            long max = movieRepository.findAll().get(0).getId();

            for (Movie movie : movieRepository.findAll()) {
                if (movie.getId() > max) {
                    max = movie.getId();
                }
            }


            return max;
        }else
            return 0;

    }
    public void addMovieToRepository(Movie movie){

        movie.setId(getMaxMovieId()+1);
        movieRepository.save(movie);

    }

    public long getMaxTimeId(){

        if(timesRepository.findAll().size()>0) {
            long max = timesRepository.findAll().get(0).getId();

            for (Time time : timesRepository.findAll()) {
                if (time.getId() > max) {
                    max = time.getId();
                }
            }


            return max;
        }else
            return 0;

    }

    public void addTimeToRepository(Time time){

        time.setId(getMaxTimeId()+1);
        timesRepository.save(time);

    }

    public void checkMovieImages(MultipartFile image1, String imageName) throws IOException {
        long id = getMaxMovieId() + 1;

        String expected_name = id + ".jpg";

        if (!imageName.equals(expected_name)) {
            imageName = expected_name;
        }

        saveFile("app/src/main/resources/static/images/movie", imageName, image1);

    }

    public void changeMovieDetails(Movie movie){
        movieRepository.save(movie);
    }

    public void changeTimeDetails(Time time){
        timesRepository.save(time);
    }

    public void deleteMovie(Movie movie){
        movieRepository.delete(movie);
    }

}

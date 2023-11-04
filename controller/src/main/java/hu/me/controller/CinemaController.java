package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.*;
import hu.me.model.*;
import hu.me.transformer.MovieTransformer;
import hu.me.transformer.TimeTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CinemaController {

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @Autowired
    private TicketServiceInterface ticketService;

    @Autowired
    private MovieTransformer movieTransformer;

    @Autowired
    private TimeTransformer timeTransformer;

    @ModelAttribute("movieModel")
    public MovieModel getMovieModel() {
        return new MovieModel();
    }

    @ModelAttribute("movieListModel")
    public MovieListModel createMovieListModel() {
        return new MovieListModel(movieTransformer.transformMovieListToMovieModelList(ticketService.getMovies()));
    }

    @GetMapping("/cinemas")
    public String showMovies(Model model, @Param("searched") String searched,
                             @RequestParam(name = "date", required = false) String date,
                             @RequestParam(name = "genre", required = false) String genre,
                             @RequestParam("category") String category){

        List<Movie> list = new ArrayList<>();
        List<Time> times = new ArrayList<>();
        if(date == null || genre == null) {
            list = ticketService.filters(searched, LocalDate.now().toString(), "összes");
            times = ticketService.filterTimes(LocalDate.now().toString());
        }
        else {
            list = ticketService.filters(searched, date, genre);
            times = ticketService.filterTimes(date);
        }

        if(category.equalsIgnoreCase(Category.CINEMA.toString())){
            String genres[] = {"Összes", "Comedy", "Horror", "Action", "Kids", "Drama", "Fantasy"};
            model.addAttribute("genres", genres);
        }else {
            String genres[] = {"Összes", "Beltéri", "Kültéri"};
            model.addAttribute("genres", genres);
        }

        model.addAttribute("filteredList", list);
        model.addAttribute("timeListModel", times);
        model.addAttribute("category", category);
        addAttributes(model);

        return "cinemas";

    }

    @GetMapping("/delete-movie")
    public String deleteMovie(@RequestParam Long movieId) {
        Movie movie = ticketService.findMovieById(movieId);
        String category = movie.getCategory().toString();
        ticketService.deleteMovie(movie);
        return "redirect:/cinemas?category=" + category.toLowerCase();
    }

    @PostMapping("/cinemas")
    public String showDatedMovies(@RequestParam("date") String date,
                                  @RequestParam("genre") String genre,
                                  @RequestParam("category") String category,
                                  Model model) {

        List<Movie> list = ticketService.filters("", date, genre);
        model.addAttribute("filteredList", list);

        List<Time> times = ticketService.filterTimes(date);
        model.addAttribute("timeListModel", times);

        model.addAttribute("userChosenDate", "");
        model.addAttribute("category", category);

        addAttributes(model);

        return "cinemas::contentFragment";
    }


    private void addAttributes(Model model) {

        model.addAttribute("today",  LocalDate.now().toString());
        model.addAttribute("maxAfterToday",  LocalDate.now().plusDays(7).toString());
        model.addAttribute("allMovies", ticketService.getMovies());
        model.addAttribute("allTimes", ticketService.getTimes());
        model.addAttribute("newUser", new User());

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());


    }

}

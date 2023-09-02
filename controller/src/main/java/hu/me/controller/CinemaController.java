package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Movie;
import hu.me.domain.User;
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
import java.util.List;

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
    public MovieListModel createTimeListModel() {
        return new MovieListModel(movieTransformer.transformMovieListToMovieModelList(ticketService.getMovies()));
    }

    @ModelAttribute("timeListModel")
    public TimeListModel createMovieListModel() {
        return new TimeListModel(timeTransformer.transformTimeListToTimeModelList(ticketService.getTimes()));
    }

    @RequestMapping("/cinemas")
    public String showMovies(Model model, @Param("searched") String searched,
                             @RequestParam(name = "selectedDate", required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate, @RequestParam("category") String category){

        List<Movie> list = ticketService.filters(searched, selectedDate);
        model.addAttribute("filteredList", list);
        model.addAttribute("category", category);
        addAttributes(model);
        return "cinemas";

    }

    private void addAttributes(Model model) {

        String genres[] = {"Vígjáték", "Horror", "Akció", "Gyerek", "Dráma", "Fantasy"};
        model.addAttribute("today",  LocalDate.now().toString());
        model.addAttribute("maxAfterToday",  LocalDate.now().plusDays(7).toString());
        model.addAttribute("genres", genres);
        model.addAttribute("newUser", new User());

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());

    }

}

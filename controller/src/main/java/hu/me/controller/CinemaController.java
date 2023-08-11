package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.User;
import hu.me.model.*;
import hu.me.transformer.MovieTransformer;
import hu.me.transformer.TimeTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    @GetMapping("/cinemas")
    public String showMovies(Model model){

        addAttributes(model);
        return "cinemas";

    }

    private void addAttributes(Model model) {

        String genres[] = {"Vígjáték", "Horror", "Akció", "Gyerek", "Dráma", "Fantasy"};
        model.addAttribute("genres", genres);
        model.addAttribute("newUser", new User());
        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());

    }

}

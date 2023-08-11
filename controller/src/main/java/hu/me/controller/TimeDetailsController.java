package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.User;
import hu.me.model.MovieModel;
import hu.me.model.ShowMovieDetailsModel;
import hu.me.model.ShowTimeDetailsModel;
import hu.me.model.TimeModel;
import hu.me.transformer.MovieTransformer;
import hu.me.transformer.TimeTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TimeDetailsController {

    @Autowired
    private TicketServiceInterface ticketService;

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @Autowired
    private MovieTransformer movieTransformer;

    @Autowired
    private TimeTransformer timeTransformer;

    @ModelAttribute("movie")
    public MovieModel showSelectedMovie(ShowMovieDetailsModel showMovieDetailsModel) {
        return movieTransformer.transformMovieToMovieModel(ticketService.findMovieById(showMovieDetailsModel.getMovieId()));
    }

    @ModelAttribute("time")
    public TimeModel showSelectedTime(ShowTimeDetailsModel showTimeDetailsModel) {
        return timeTransformer.transformTimeToTimeModel(ticketService.findTimeById(showTimeDetailsModel.getTimeId()));
    }

    @GetMapping(value="/time-details", params="timeId")
    private String showTimeDetails(Model model, @RequestParam("timeId") long timeId, @RequestParam("movieId") long movieId) {

        model.addAttribute("typePrice", ticketService.tickets());
        model.addAttribute("day",ticketService.dayOfWeek(ticketService.findTimeById(timeId)));
        model.addAttribute("newUser", new User());

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());

        return "time-details";
    }

}

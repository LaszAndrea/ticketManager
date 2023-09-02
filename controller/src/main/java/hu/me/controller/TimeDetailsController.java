package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Seat;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/reservation")
    public String makeReservation(@RequestParam("timeId") long timeId, @RequestParam("movieId") long movieId){

        User loggedInUser = ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername());

        //elvileg működik
        //ticketService.addMovie(ticketService.findMovieById(movieId), loggedInUser);
        ticketService.reservation(ticketService.findTimeById(timeId), loggedInUser);
        //ticketService.reserveSeats();

        return "redirect:cinemas?category=cinema";

    }

    @GetMapping(value="/time-details")
    public String showTimeDetails(Model model, @RequestParam("timeId") long timeId, @RequestParam("movieId") long movieId) {

        model.addAttribute("typePrice", ticketService.tickets());
        model.addAttribute("day",ticketService.dayOfWeek(ticketService.findTimeById(timeId)));
        model.addAttribute("seatsWithMovie", ticketService.getSeatsForTime(ticketService.findTimeById(timeId)));
        model.addAttribute("reserveSeats", new ArrayList<Seat>());

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser"))) {
            model.addAttribute("loggedInUser", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()));
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
        }else{
            model.addAttribute("newUser", new User());
        }

        return "time-details";
    }

}

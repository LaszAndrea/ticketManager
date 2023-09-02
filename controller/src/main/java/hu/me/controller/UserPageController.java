package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @Autowired
    private TicketServiceInterface ticketService;

    @GetMapping("/user-home-page")
    public String showUserInfos(Model model){

        //model.addAttribute("userMovies", ticketService.getMoviesForUser(ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername())));

        model.addAttribute("userReservations", ticketService.getReservationsForUser(ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername())));

        //System.out.print(ticketService.getMoviesForUser(ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername())).size());

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser"))) {
            model.addAttribute("loggedInUser", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()));
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
        }

        return "user-home-page";
    }

}

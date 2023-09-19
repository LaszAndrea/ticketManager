package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Sights;
import hu.me.domain.User;
import hu.me.model.SightModel;
import hu.me.model.UserModel;
import hu.me.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

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
        System.out.print("/n" + ticketService.getReservationsForUser(ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername())).size());

        //System.out.print(ticketService.getMoviesForUser(ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername())).size());

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser"))) {
            model.addAttribute("loggedInUser", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()));
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
        }

        return "user-home-page";
    }

}

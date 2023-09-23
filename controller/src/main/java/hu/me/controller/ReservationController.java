package hu.me.controller;

import com.google.zxing.WriterException;
import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Base64;

@Controller
public class ReservationController {

    @Autowired
    TicketServiceInterface ticketService;

    @Autowired
    UserLoginDetailsService userLoginDetailsService;

    @PostMapping("/reservation")
    public String generateQRCode(@RequestParam("timeId") long timeId, @RequestParam("movieId") long movieId, Model model) throws WriterException, IOException {

        User loggedInUser = ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername());

        //elvileg működik
        //ticketService.addMovie(ticketService.findMovieById(movieId), loggedInUser);
        ticketService.reservation(ticketService.findTimeById(timeId), loggedInUser);
        //ticketService.reserveSeats();

        try {

            byte[] qrCodeImageBytes = ticketService.generateQRCodeBytes("" + timeId + " " + movieId);
            model.addAttribute("qrCodeImage", Base64.getEncoder().encodeToString(qrCodeImageBytes));

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser"))) {
            System.out.print("\n" + userLoginDetailsService.loadAuthenticatedUsername());
            model.addAttribute("loggedInUser", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()));
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
        }

        return "reservation";
    }


}

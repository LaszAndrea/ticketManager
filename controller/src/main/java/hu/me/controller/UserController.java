package hu.me.controller;

//import hu.me.UserLoginDetailsService;
import hu.me.TicketService;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Role;
import hu.me.domain.User;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.SecureRandom;

@Controller
public class UserController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserRepository uRep;

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @GetMapping("/homepage")
    public String basics(Model model) {

        model.addAttribute("newUser", new User());

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", uRep.findByCredentialsLoginName(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());

        return "homepage";

    }


}

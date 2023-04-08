package hu.me.controller;

import hu.me.UserLoginDetailsService;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository uRep;

    @Autowired
    private UserLoginDetailsService ulds;

    //@Secured({" USER "})
    @GetMapping("/homepage")
    public String basics(Model model) {

        //model.addAttribute("user", uRep.findByCredentialsLoginName(ulds.loadAuthenticatedUsername()));
        //System.out.println(ulds.loadAuthenticatedUsername());

        return "homepage";
    }


}

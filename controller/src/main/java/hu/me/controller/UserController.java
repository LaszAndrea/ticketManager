package hu.me.controller;

//import hu.me.UserLoginDetailsService;
import hu.me.domain.User;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository uRep;

    //@Autowired
    //private UserLoginDetailsService ulds;

    @GetMapping("/homepage")
    public String basics(Model model) {

        model.addAttribute("newUser", new User());

        //model.addAttribute("user", uRep.findByCredentialsLoginName(ulds.loadAuthenticatedUsername()));

        return "homepage";

    }


}

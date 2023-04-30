package hu.me.controller;

import hu.me.TicketService;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Role;
import hu.me.domain.User;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @GetMapping(value = "/")
    public String showRootPage(Model model) {
        return "redirect:homepage";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute("newUser") User newUser, BindingResult result, Model model, HttpServletRequest request) {


        /*int strength = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(strength, new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getCredentials().getPassword());
        System.out.print(encodedPassword);
        user.getCredentials().setPassword(encodedPassword);*/

        newUser.setRole(Role.USER);

        if(result.hasErrors()){
            result.rejectValue("credentials.loginName", "error.user", "");
            return "homepage";
        }else {

            ticketService.save(newUser);
            boolean checkboxValue = "checked".equals(request.getParameter("checkbox"));

            if (checkboxValue) {
                try {
                    request.login(newUser.getCredentials().getLoginName(), newUser.getCredentials().getPassword());
                    return "redirect:/homepage";
                } catch (ServletException e) {
                    throw new RuntimeException(e);
                }
            } else
                return "login";
        }

    }

}

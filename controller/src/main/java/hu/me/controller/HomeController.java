package hu.me.controller;

import hu.me.TicketService;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Role;
import hu.me.domain.User;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;

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

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public String register(@ModelAttribute("newUser") User user, Model model, BindingResult result, HttpServletRequest request) {

        /*int strength = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(strength, new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getCredentials().getPassword());
        System.out.print(encodedPassword);
        user.getCredentials().setPassword(encodedPassword);*/

        user.setRole(Role.USER);
        ticketService.save(user);

        boolean checkboxValue = "checked".equals(request.getParameter("checkbox"));

        if(checkboxValue) {
            try {
                request.login(user.getCredentials().getLoginName(), user.getCredentials().getPassword());
                return "redirect:/homepage";
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }else
            return "login";

    }

}

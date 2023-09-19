package hu.me.controller;

import hu.me.TicketService;
import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Role;
import hu.me.domain.User;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TicketServiceInterface ticketService;

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @GetMapping(value = "/")
    public String showRootPage(Model model) {
        return "redirect:homepage";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String register( @RequestHeader("referer") String referer, @Valid @ModelAttribute("newUser") User newUser, BindingResult result, Model model, HttpServletRequest request) throws MalformedURLException {

        //newUser.setRole(Role.ADMIN);
        newUser.setRole(Role.USER);

        if(result.hasErrors()){
            result.rejectValue("credentials.loginName", "error.user", "");
            return "homepage";
        }else {

            // szükséges különben a hashelt jelszóval akar  bejelentkeztetni, ami nem fog menni
            String password = newUser.getCredentials().getPassword();

            ticketService.save(newUser);
            boolean checkboxValue = "checked".equals(request.getParameter("checkbox"));

            if (checkboxValue) {
                try {
                    request.login(newUser.getCredentials().getLoginName(), password);
                    return "redirect:/" +  referer.substring(referer.lastIndexOf("/") + 1);
                } catch (ServletException e) {
                    throw new RuntimeException(e);
                }
            } else
                return "login";
        }

    }

}

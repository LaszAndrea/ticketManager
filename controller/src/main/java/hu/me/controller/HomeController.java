package hu.me.controller;

import hu.me.TicketService;
import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Role;
import hu.me.domain.User;
import hu.me.model.UserModel;
import hu.me.repository.UserRepository;
import hu.me.transformer.UserTransformer;
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

    @Autowired
    private UserTransformer userTransformer;

    @GetMapping(value = "/")
    public String showRootPage(Model model) {
        return "redirect:homepage";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String register(@RequestHeader("referer") String referer, @Valid @ModelAttribute("newUser") UserModel newUser, BindingResult result, Model model, HttpServletRequest request) {

        newUser.setRole(Role.ADMIN);
        //newUser.setRole(Role.USER);

        if(result.hasErrors()){
            result.rejectValue("credentials.loginName", "error.user", "");
            model.addAttribute("newsList", ticketService.gatherNews());

            return "homepage";
        }else {

            // szükséges különben a hashelt jelszóval akar  bejelentkeztetni, ami nem fog menni
            String password = newUser.getCredentials().getPassword();
            User user = userTransformer.transformUserModelToUser(newUser);

            ticketService.save(user);
            boolean checkboxValue = "checked".equals(request.getParameter("checkbox"));

            if (checkboxValue) {
                try {
                    request.login(newUser.getCredentials().getLoginName(), password);

                    if(referer.substring(referer.lastIndexOf("/") + 1).equalsIgnoreCase("save")){
                        return "redirect:/homepage";
                    }else
                        return "redirect:/" +  referer.substring(referer.lastIndexOf("/") + 1);

                } catch (ServletException e) {
                    throw new RuntimeException(e);
                }
            } else
                return "login";
        }

    }

}

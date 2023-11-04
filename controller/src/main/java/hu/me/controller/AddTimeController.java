package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Category;
import hu.me.model.MovieModel;
import hu.me.model.TimeModel;
import hu.me.transformer.TimeTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class AddTimeController {

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @Autowired
    private TicketServiceInterface ticketService;

    @Autowired
    private TimeTransformer timeTransformer;

    @ModelAttribute("timeModel")
    public TimeModel getTimeModel() {
        return new TimeModel();
    }

    @GetMapping("addTime")
    public String ShowFormForTimes(Model model, @RequestParam("movieId") String movieId, @RequestParam("category") String category){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        model.addAttribute("today", LocalDateTime.now().format(formatter));
        model.addAttribute("maxAfterToday", LocalDateTime.now().plusDays(7).format(formatter));
        model.addAttribute("category", category);

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());

        return "addTime";
    }

    @PostMapping("/uploadTime")
    public String addTimeToRepository(@Valid TimeModel timeModel, BindingResult bindingResult,
                                      Model model, @RequestParam("movieId") String movieId, @RequestParam("category") String category){

        timeModel.setMovie(ticketService.findMovieById(Long.parseLong(movieId)));
        ticketService.addTimeToRepository(timeTransformer.transformTimeModelToTime(timeModel));

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());

        return "redirect:/cinemas?category=" + category;

    }

}

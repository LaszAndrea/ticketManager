package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Category;
import hu.me.model.MovieModel;
import hu.me.model.ShowMovieDetailsModel;
import hu.me.model.ShowTimeDetailsModel;
import hu.me.model.TimeModel;
import hu.me.transformer.MovieTransformer;
import hu.me.transformer.TimeTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChangeTimeDetails {


    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @Autowired
    private TicketServiceInterface ticketService;

    @Autowired
    private TimeTransformer timeTransformer;

    @ModelAttribute("currentTime")
    public TimeModel createTimeModel(ShowTimeDetailsModel showTimeDetailsModel) {
        return timeTransformer.transformTimeToTimeModel(ticketService.findTimeById(showTimeDetailsModel.getTimeId()));
    }

    @GetMapping("changeTime")
    public String ShowFormForTimes(Model model, @RequestParam("category") String category){

        addAttributes(model);
        model.addAttribute("category", category);

        return "changeTimeDetails";

    }

    @PostMapping("/changeTime")
    public String changeTime(@Valid TimeModel timeModel, BindingResult bindingResult,
                                       Model model, @RequestParam("timeId") String timeId,
                                        @RequestParam("movieId") String movieId,
                                        @RequestParam("category") String category) throws IOException {

        timeModel.setMovie(ticketService.findMovieById(Long.parseLong(movieId)));
        timeModel.setId(Long.parseLong(timeId));
        ticketService.changeTimeDetails(timeTransformer.transformTimeModelToTime(timeModel));

        addAttributes(model);

        if(category.equalsIgnoreCase(Category.CINEMA.toString())) {
            return "redirect:/cinemas?category=cinema";
        }else{
            return "redirect:/cinemas?category=theatre";
        }

    }

    private void addAttributes(Model model) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        model.addAttribute("today", LocalDateTime.now().format(formatter));
        model.addAttribute("maxAfterToday", LocalDateTime.now().plusDays(7).format(formatter));

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
    }


}

package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Category;
import hu.me.model.MovieModel;
import hu.me.model.ShowMovieDetailsModel;
import hu.me.model.ShowSightDetailsModel;
import hu.me.model.SightModel;
import hu.me.transformer.MovieTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class ChangeMovieDetails {


    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @Autowired
    private TicketServiceInterface ticketService;

    @Autowired
    private MovieTransformer movieTransformer;

    @ModelAttribute("currentMovie")
    public MovieModel createMovieModel(ShowMovieDetailsModel showMovieDetailsModel) {
        return movieTransformer.transformMovieToMovieModel(ticketService.findMovieById(showMovieDetailsModel.getMovieId()));
    }

    @GetMapping("changeMovie")
    public String ShowFormForMovies(Model model, @RequestParam("category") String category){

        addAttributes(model);
        model.addAttribute("category", category);

        if(category.equalsIgnoreCase(Category.CINEMA.toString())){
            String genres[] = {"Comedy", "Horror", "Action", "Kids", "Drama", "Fantasy"};
            model.addAttribute("genres", genres);
        }else {
            String genres[] = {"Beltéri", "Kültéri"};
            model.addAttribute("genres", genres);
        }

        return "changeMovieDetails";

    }

    @PostMapping("/changeMovie")
    public String changeMovie(@Valid MovieModel movie, BindingResult bindingResult,
                                       Model model, @RequestParam("movieId") String movieId,
                              @RequestParam("category") String category) throws IOException {

        if (bindingResult.hasFieldErrors("age")) {

            bindingResult.rejectValue("age", "error.movie", "");
            addAttributes(model);
            return "changeMovieDetails";

        }else {

            movie.setId(Long.parseLong(movieId));
            if(category.equalsIgnoreCase(Category.CINEMA.toString())){
                movie.setCategory(Category.CINEMA);
            }else{
                movie.setCategory(Category.THEATRE);
            }
            ticketService.changeMovieDetails(movieTransformer.transformMovieModelToMovie(movie));

            addAttributes(model);

            return "redirect:/cinemas?category=" + category;


        }

    }

    private void addAttributes(Model model) {


        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
    }

}

package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Category;
import hu.me.domain.User;
import hu.me.model.MovieModel;
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
import java.time.LocalDate;

@Controller
public class AddMovieController {

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @Autowired
    private TicketServiceInterface ticketService;

    @Autowired
    private MovieTransformer movieTransformer;

    @ModelAttribute("movieModel")
    public MovieModel getMovieModel() {
        return new MovieModel();
    }

    @GetMapping("addMovie")
    public String ShowFormForMovies(Model model, @RequestParam("category") String category){

        categoryCheck(model, category);
        addAttributes(model);

        return "addMovie";

    }

    @PostMapping("/uploadMovie")
    public String addMovieToRepository(@Valid MovieModel movie, BindingResult bindingResult,
                                       @RequestParam("image") MultipartFile image,
                                       @RequestParam("category") String category,
                                       Model model) throws IOException {


        if (bindingResult.hasFieldErrors("age")) {

            bindingResult.rejectValue("age", "error.movie", "");
            addAttributes(model);
            categoryCheck(model, category);
            return "addMovie";

        }else {

            if(category.equalsIgnoreCase(Category.CINEMA.toString())) {
                ticketService.checkMovieImages(image, StringUtils.cleanPath(image.getOriginalFilename()));
                movie.setCategory(Category.CINEMA);
            }else{
                movie.setCategory(Category.THEATRE);
            }
            ticketService.addMovieToRepository(movieTransformer.transformMovieModelToMovie(movie));

            addAttributes(model);

            return "redirect:/cinemas?category=" + category;
        }

    }

    private void addAttributes(Model model) {
        model.addAttribute("nextId", ticketService.getMaxMovieId()+1);

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
    }

    private static void categoryCheck(Model model, String category) {
        model.addAttribute("category", category);
        if(category.equalsIgnoreCase(Category.CINEMA.toString())) {
            String genres[] = {"Comedy", "Horror", "Action", "Kids", "Drama", "Fantasy"};
            model.addAttribute("genres", genres);
        }else{
            String genres[] = {"Beltéri", "Kültéri"};
            model.addAttribute("genres", genres);
        }
    }

}

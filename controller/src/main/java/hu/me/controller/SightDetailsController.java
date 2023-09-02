package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Review;
import hu.me.domain.Sights;
import hu.me.domain.User;
import hu.me.model.*;
import hu.me.transformer.ReviewTransformer;
import hu.me.transformer.SightTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class SightDetailsController {

    @Autowired
    private SightTransformer sightTransformer;
    @Autowired
    private ReviewTransformer reviewTransformer;

    @Autowired
    private TicketServiceInterface ticketService;
    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @ModelAttribute("sight")
    public SightModel createSightModel(ShowSightDetailsModel showSightDetailsModel) {
        return sightTransformer.transformSightToSightModel(ticketService.findSightById(showSightDetailsModel.getSightId()));
    }

    @ModelAttribute("reviewModel")
    public ReviewModel createReviewModel() {
        return new ReviewModel();
    }

    @PostMapping("/create-review")
    public String createReview(@Valid ReviewModel reviewModel, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam("sightId") long sightId) {
        String result;
        if (bindingResult.hasErrors()) {
            result = "sights";
        } else {
            sightSetter(reviewModel, sightId);
            reviewModel.setUser(ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()));
            ticketService.getReviewList();
            Review review = reviewTransformer.transformReviewModelToReview(reviewModel);
            ticketService.addReview(review);
            result = "redirect:sight-details?sightId=" + sightId;
        }

        return result;
    }

    @PostMapping("/update-details")
    public String updateSight(@Valid SightModel sightModel, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam("sightId") long sightId) throws IOException {
        String result;
        if (bindingResult.hasErrors()) {
            result = "sights";
        } else {

            Sights sight = sightTransformer.transformSightModelToSight(sightModel);
            ticketService.updateSight(sight);

            result = "redirect:sight-details?sightId=" + sightId;
        }

        return result;
    }


    private void sightSetter(ReviewModel reviewModel, long destId) {

        Sights sight =null;
        List<Sights> sightsList = ticketService.findAllSights();
        for(int i=0; i<sightsList.size(); i++){
            if(sightsList.get(i).getId() == destId)
                sight = sightsList.get(i);
        }
        reviewModel.setSights(sight);

    }

    @GetMapping(value="/sight-details", params="sightId")
    private String showSightDetails(Model model, @RequestParam("sightId") long sightId) {

        model.addAttribute("reviewLatests", ticketService.findLatests(sightId));
        model.addAttribute("newUser", new User());

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());

        return "sight-details";
    }


}

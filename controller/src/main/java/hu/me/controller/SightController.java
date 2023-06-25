package hu.me.controller;

import hu.me.TicketService;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Sights;
import hu.me.domain.User;
import hu.me.model.SightListModel;
import hu.me.model.SightModel;
import hu.me.repository.SightRepository;
import hu.me.repository.UserRepository;
import hu.me.transformer.SightTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class SightController {

    @Autowired
    private SightRepository sightRepository;

    @Autowired
    private SightTransformer sightTransformer;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute("sightModel")
    public SightModel getSightModel() {
        return new SightModel();
    }

    @ModelAttribute("sightListModel")
    public SightListModel createSightListModel() {
        return new SightListModel(sightTransformer.transformSightListToSightModelList(ticketService.getSights()));
    }

    @PostMapping("/sights")
    public String createSight(@Valid SightModel sightModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        String result;
        if (bindingResult.hasErrors()) {
            result = "sights";
        } else {
            Sights sight = sightTransformer.transformSightModelToSight(sightModel);
            ticketService.addSight(sight);
            redirectAttributes.addFlashAttribute("successMessage", "Sight added successfully");
            result = "redirect:sights";
        }

        return result;
    }

    @PostMapping("/delete-sight")
    public String deleteSight(@Valid SightModel sightModel, BindingResult bindingResult) {
        String result;
        System.out.print(bindingResult);
        if (bindingResult.hasErrors()) {
            result = "sights";
        } else {
            Sights sight = sightTransformer.transformSightModelToSight(sightModel);

            sightRepository.delete(sight);
            result = "redirect:sights";
        }

        return result;
    }

    @GetMapping("/sights")
    public String showSights(Model model, @RequestParam("category") String category) {

        model.addAttribute("newUser", new User());
        model.addAttribute("category", category);

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", userRepository.findByCredentialsLoginName(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());

        return "sights";
    }

}

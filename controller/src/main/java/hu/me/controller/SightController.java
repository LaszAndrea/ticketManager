package hu.me.controller;

import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.Sights;
import hu.me.domain.User;
import hu.me.model.SightListModel;
import hu.me.model.SightModel;
import hu.me.transformer.SightTransformer;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SightController {

    @Autowired
    private SightTransformer sightTransformer;

    @Autowired
    private TicketServiceInterface ticketService;

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @ModelAttribute("sightModel")
    public SightModel getSightModel() {
        return new SightModel();
    }

    @ModelAttribute("sightListModel")
    public SightListModel createSightListModel() {
        return new SightListModel(sightTransformer.transformSightListToSightModelList(ticketService.getSights()));
    }

    @GetMapping("/delete-sight")
    public String deleteSight(@RequestParam Long sightId) {
        Sights sight = ticketService.findSightById(sightId);
        String category = sight.getCategory().toString();
        ticketService.deleteSight(sightId);
        return "redirect:/sights?category=" + category.toLowerCase();
    }

    @PostMapping("/upload")
    public String createSightEntity(@RequestParam("category") String category,
                                    @Valid SightModel sightModel,
                                    BindingResult bindingResult,
                                    @RequestParam("images") MultipartFile[] images,
                                    Model model) throws IOException {

        String imageNames[] = {
                StringUtils.cleanPath(images[0].getOriginalFilename()),
                StringUtils.cleanPath(images[1].getOriginalFilename()),
                StringUtils.cleanPath(images[2].getOriginalFilename()),
                StringUtils.cleanPath(images[3].getOriginalFilename())
        };

        if (bindingResult.hasErrors()) {

            addAttributes(model);
            return "sights";

        }else if (images.length != 4) {

            model.addAttribute("imageError", "4 képnek kell lennie");
            addAttributes(model);
            return "sights";

        }else {

            ticketService.uploadImages(images[0], images[1], images[2], images[3], ticketService.checkImages(imageNames));
            ticketService.addSight(sightTransformer.transformSightModelToSight(sightModel));

            return "redirect:sights?category=" + category.toLowerCase();

        }
    }

    @GetMapping("/sights")
    public String showSights(Model model, @RequestParam("category") String category) {

        model.addAttribute("category", category);
        addAttributes(model);

        return "sights";
    }

    private void addAttributes(Model model) {

        model.addAttribute("newUser", new User());
        model.addAttribute("nextId", ticketService.getMaxSightId()+1);

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser")))
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
    }

}

package hu.me.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.User;
import hu.me.model.ShowUserDetailsModel;
import hu.me.model.SightModel;
import hu.me.model.UserModel;
import hu.me.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ChangeUserDetailsController {

    @Autowired
    private UserLoginDetailsService userLoginDetailsService;

    @Autowired
    private TicketServiceInterface ticketService;

    @Autowired
    private UserTransformer userTransformer;

    @ModelAttribute("userModel")
    public UserModel createUserModel(ShowUserDetailsModel showUserDetailsModel) {
        return userTransformer.transformUserToUserModel(ticketService.findUserById(showUserDetailsModel.getUserId()));
    }

    @PostMapping("/change-user-details")
    public String updatePhoneNumber(@Valid UserModel userModel, BindingResult bindingResult, @RequestParam("userId") long userId) throws IOException {
        String result;
        System.out.print(bindingResult.getAllErrors());
        if (bindingResult.hasErrors()) {
            result = "user-home-page";
        } else {

            User user = userTransformer.transformUserModelToUser(userModel);
            ticketService.updateUserPhoneNumber(user);

            result = "redirect:change-user-details?userId="+ userId;
        }

        return result;
    }

    @GetMapping("/change-user-details")
    public String showChangeDetails(Model model){

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser"))) {
            System.out.print("\n" + userLoginDetailsService.loadAuthenticatedUsername());
            model.addAttribute("loggedInUser", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()));
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
        }

        return "change-user-details";
    }
}

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
import hu.me.validator.UniqueEmail;
import hu.me.validator.UniqueEmailValidator;
import hu.me.validator.UniquePhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /*@ModelAttribute("userModel")
    public UserModel createUserModel(ShowUserDetailsModel showUserDetailsModel) {
        return userTransformer.transformUserToUserModel(ticketService.findUserById(showUserDetailsModel.getUserId()));
    }*/

    @ModelAttribute("userModel")
    public UserModel createUserModel(ShowUserDetailsModel showUserDetailsModel) {
        return new UserModel();
    }

    @PostMapping("/change-user-details")
    public String changeUserDetails(UserModel userModel, @ModelAttribute("changing") String changing, BindingResult bindingResult, @RequestParam("userId") long userId,@RequestParam("change") String change, Model model) throws IOException {

        if(change.equalsIgnoreCase("phone") &&
                !ticketService.isPhoneNumberUnique(userModel.getPhoneNumber())){

            System.out.println(userModel.getPhoneNumber());
            bindingResult.rejectValue(null, "error.user.phoneNumber", "HIBAÜZENET");
            //bindingResult.rejectValue("phoneNumber", "error.user.phoneNumber", "HIBAÜZENET");

            model.addAttribute("loggedInUser", ticketService.findUserById(userId));
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());

            return "change-user-details";
        }else if(change.equalsIgnoreCase("email") && (!ticketService.isEmailValid(userModel.getCredentials().getLoginName())
            || !ticketService.isEmailUnique(userModel.getCredentials().getLoginName()))){

            bindingResult.rejectValue( "loginName", "error.credentials.loginName");

            model.addAttribute("loggedInUser", ticketService.findUserById(userId));
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());

            return "change-user-details";

        } else {

            User user = ticketService.findUserById(userId);

            if(change.equalsIgnoreCase("name")) {
                user.setFullName(userModel.getFullName());
            }else if(change.equalsIgnoreCase("phone")){
                user.setPhoneNumber(userModel.getPhoneNumber());
            }else if(change.equalsIgnoreCase("email")){
                user.getCredentials().setLoginName(userModel.getCredentials().getLoginName());
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Authentication newAuthentication = new UsernamePasswordAuthenticationToken(user.getCredentials().getLoginName(), authentication.getCredentials(), authentication.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuthentication);
            }

            ticketService.save(user);
            System.out.println(user.getFullName() + " " + user.getPhoneNumber() + " " +user.getCredentials().getLoginName());

            return "redirect:user-home-page";
        }

    }

    @GetMapping("/change-user-details")
    public String showChangeDetails(Model model){

        model.addAttribute("changing", "");

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser"))) {
            model.addAttribute("loggedInUser", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()));
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
        }

        return "change-user-details";
    }
}

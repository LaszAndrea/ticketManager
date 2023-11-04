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
import hu.me.domain.Credentials;
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

    @ModelAttribute("userModel")
    public UserModel createUserModel() {
        return new UserModel();
    }

    @PostMapping("/change-user-phone")
    public String changeUserPhone(@Valid UserModel userModel, BindingResult bindingResult,
                                  @RequestParam("userId") long userId, Model model){

        if(bindingResult.hasFieldErrors("phoneNumber")){
            bindingResult.rejectValue("phoneNumber", "error.user", "");
            addModelAttributes(model);
            return "change-user-phone";
        }else{

            User user = ticketService.findUserById(userId);
            user.setPhoneNumber(userModel.getPhoneNumber());

            ticketService.updateUser(user);

            return "redirect:user-home-page";

        }

    }

    @PostMapping("/change-user-email")
    public String changeUserEmail(@Valid UserModel userModel, BindingResult bindingResult, @RequestParam("userId") long userId, Model model){

        if(bindingResult.hasFieldErrors("credentials.loginName")){
            bindingResult.rejectValue("credentials.loginName", "error.user", "");
            addModelAttributes(model);
            return "change-user-email";
        }else{

            User user = ticketService.findUserById(userId);
            user.getCredentials().setLoginName(userModel.getCredentials().getLoginName());

            Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
            Authentication newEmailAuth = new UsernamePasswordAuthenticationToken(user.getCredentials().getLoginName(), currentAuth.getCredentials(), currentAuth.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newEmailAuth);

            ticketService.updateUser(user);

            return "redirect:user-home-page";

        }

    }

    @PostMapping("/change-user-name")
    public String changeUserName(@Valid UserModel userModel, BindingResult bindingResult, @RequestParam("userId") long userId, Model model){

        if(bindingResult.hasFieldErrors("fullName")){
            bindingResult.rejectValue("fullName", "error.user", "");
            addModelAttributes(model);
            return "change-user-name";
        }else{

            User user = ticketService.findUserById(userId);
            user.setFullName(userModel.getFullName());
            ticketService.updateUser(user);

            return "redirect:user-home-page";

        }

    }

    @GetMapping("/change-user-phone")
    public String showChangeDetailsPhone(Model model){
        addModelAttributes(model);
        return "change-user-phone";
    }

    @GetMapping("/change-user-name")
    public String showChangeDetailsName(Model model){
        addModelAttributes(model);
        return "change-user-name";
    }

    @GetMapping("/change-user-email")
    public String showChangeDetailsEmail(Model model){
        addModelAttributes(model);
        return "change-user-email";
    }

    private void addModelAttributes(Model model) {
        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser"))) {
            model.addAttribute("loggedInUser", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()));
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
        }
    }
}

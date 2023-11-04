package hu.me.controller;

import com.google.zxing.WriterException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;
import hu.me.TicketServiceInterface;
import hu.me.UserLoginDetailsService;
import hu.me.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Controller
public class ReservationController {

    @Autowired
    TicketServiceInterface ticketService;

    @Autowired
    UserLoginDetailsService userLoginDetailsService;

    @GetMapping("/reservation")
    public String showQrCode(@RequestParam("timeId") long timeId, @RequestParam("movieId") long movieId,
                             @RequestParam(name="seats", required = false) String seats,Model model) throws WriterException, IOException {

        User loggedInUser = ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername());

        if(seats != null){
            ticketService.reserveSeats(seats.split(","));
            ticketService.reservation(ticketService.findTimeById(timeId), loggedInUser);

            try {

                byte[] qrCodeImageBytes = ticketService.generateQRCodeBytes("Foglaló személy neve: " + loggedInUser.getFullName() + "\nElőadás címe: "
                        + ticketService.findMovieById(movieId).getName() +"\nFoglalt időpont: " + ticketService.findTimeById(timeId).getTime_date() + "\nFoglalt székek: " + seats);

                model.addAttribute("qrCodeImage", Base64.getEncoder().encodeToString(qrCodeImageBytes));
                model.addAttribute("text", ("Foglaló személy neve: " + loggedInUser.getFullName() + "\nElőadás címe: "
                        + ticketService.findMovieById(movieId).getName() +"\nFoglalt időpont: " + ticketService.findTimeById(timeId).getTime_date() + "\nFoglalt székek: " + seats) );

            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }

        }

        if (!(userLoginDetailsService.loadAuthenticatedUsername().equalsIgnoreCase("anonymousUser"))) {
            model.addAttribute("loggedInUser", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()));
            model.addAttribute("userName", ticketService.findUserByUsername(userLoginDetailsService.loadAuthenticatedUsername()).getFullName());
        }

        return "reservation";
    }

    @GetMapping("/download-pdf")
    public void downloadQrCode(HttpServletResponse response, @RequestParam("qrCode") String qrCode) {
        try {

            Document document = new Document();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            document.add(new Paragraph("Köszönjük a foglalását!"));

            byte[] qrCodeBytes = ticketService.generateQRCodeBytes(qrCode);
            Image qrCodeImage = Image.getInstance(qrCodeBytes);
            document.add(qrCodeImage);

            document.close();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=qr_code.pdf");
            response.getOutputStream().write(byteArrayOutputStream.toByteArray());

            response.getOutputStream().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}


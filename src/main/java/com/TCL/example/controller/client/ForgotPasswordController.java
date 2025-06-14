package com.TCL.example.controller.client;

import com.TCL.example.domain.DTO.MailBody;
import com.TCL.example.domain.ForgotPassword;
import com.TCL.example.domain.User;
import com.TCL.example.domain.request.ResetPassword;
import com.TCL.example.repository.ForgotPasswordRepository;
import com.TCL.example.service.MailService;
import com.TCL.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.Map;
@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
    private final UserService userService;
    private final MailService mailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserService userService,
                                    MailService mailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.mailService = mailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }




    // send mail for email verification
   @PostMapping("/verify-email")
@ResponseBody
public ResponseEntity<String> verifyEmail(@RequestBody Map<String, String> payload, HttpSession session) {
    String email = payload.get("email");
    User user = this.userService.getUserByEmail(email);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
    }

    int otp = otpGenerator();
    MailBody mailBody = MailBody.builder()
            .to(email)
            .content("This is the OTP for your Forgot Password request: " + otp)
            .subject("OTP for Forgot Password request")
            .build();
            forgotPasswordRepository.deleteByUserId(user.getId());

            ForgotPassword fp = ForgotPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 60 * 1000))
                    .user(user)
                    .build();
    forgotPasswordRepository.save(fp);
    mailService.sendSimpleMessage(mailBody);
    forgotPasswordRepository.save(fp);
    session.setAttribute("userEmail", email);

    return ResponseEntity.ok("OTP sent successfully");
}


    @PostMapping("/verify-otp/{otp}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, HttpSession session) {
        String email = session.getAttribute("userEmail").toString();
        User user = this.userService.getUserByEmail(email);

        ForgotPassword fp = this.forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: "+ email));
        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            this.forgotPasswordRepository.deleteByFpId(fp.getFpId());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }
        this.forgotPasswordRepository.deleteByFpId(fp.getFpId());

        return ResponseEntity.ok("OTP verified successfully!");
    }

    @PostMapping("/reset-password/{email}")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword,
                                                @PathVariable String email) {
        if(!resetPassword.getPassword().equals(resetPassword.getConfirmPassword())) {
            return new ResponseEntity<>("Mật khẩu không khớp!", HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = this.passwordEncoder.encode(resetPassword.getPassword());
        this.userService.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Password updated successfully!");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }

    @PostMapping("/delete/{email}")
    public ResponseEntity<String> deleteOTP(@PathVariable String email) {
        User user = this.userService.getUserByEmail(email);
        this.forgotPasswordRepository.deleteByUserId(user.getId());
        return ResponseEntity.ok("delete success!");
    }
    
}


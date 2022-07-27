package com.himanshu.spring.security.controller;

import com.himanshu.spring.security.entity.User;
import com.himanshu.spring.security.entity.VerificationToken;
import com.himanshu.spring.security.event.RegistrationCompletionEvent;
import com.himanshu.spring.security.model.PasswordModel;
import com.himanshu.spring.security.model.UserModel;
import com.himanshu.spring.security.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class RegistrationController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RegistrationController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private WebClient webClient;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompletionEvent(
                user,
                applicationUrl(request)
        ));
        return user.toString();
    }

    @GetMapping("api/hello")
    public String hello(){
        return "hello from register user";
    }

    @GetMapping("/api/users")
    public String[] users(
            @RegisteredOAuth2AuthorizedClient("api-client-authorization-code")
            OAuth2AuthorizedClient client){
        return this.webClient
                .get()
                .uri("http://127.0.0.1:8090/api/users")
                .attributes(oauth2AuthorizedClient(client))
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }

    private String applicationUrl(HttpServletRequest request){
        return "http://" + request.getServerName() +
                ":" + request.getServerPort() +
                request.getContextPath();
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User Verifies Successfully.";
        }
        return "Bad user";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                            HttpServletRequest request){
        VerificationToken verificationToken =
                userService.generateNewVerificationToken(oldToken);

        User user = verificationToken.getUser();

        resendVerificationTokenMail(user, applicationUrl(request), verificationToken.getToken());
        return "Verification link sent.";
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, String token) {
        String url =
                applicationUrl + "/verifyRegistration?token="
                + token;

        log.info("Your verification url is {}", url);
    }

    //todo work pending
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel,
                                HttpServletRequest request){
        User user = userService.getUserByEmail(passwordModel.getEmail());
        if(user == null){
            return "User does not exist...";
        }

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        passwordResetTokenMail(user, applicationUrl(request), token);
        return "Mail sent successfully...";
    }

    private void passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;
        log.info("Click the link to Reset your Password : {}", url);
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                                @RequestBody PasswordModel passwordModel){
        String result = userService.verifyPasswordResetToken(token);
        if(result.equalsIgnoreCase("invalid")){
            return "Invalid token!";
        }

        if(result.equalsIgnoreCase("expired")){
            return "Token expired!";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token);

        if(user.isPresent()){
            userService.resetPassword(user.get(), passwordModel.getNewPassword());
            userService.deletePasswordResetToken(token);
            return "Password Reset Successful";
        }else{
            return "Invalid Token!";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        if(passwordModel.getOldPassword() == passwordModel.getNewPassword()){
            return "Old And New Password Are Same.";
        }
        User user = userService.getUserByEmail(passwordModel.getEmail());

        if(user == null){
            return "User Does Not Exist!";
        }

        if(!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())){
            return "Invalid Old Password!";
        }

        userService.resetPassword(user, passwordModel.getNewPassword());
        return "Password Changed Successfully";
    }

}

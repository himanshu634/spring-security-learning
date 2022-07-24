package com.himanshu.spring.security.controller;

import com.himanshu.spring.security.entity.User;
import com.himanshu.spring.security.event.RegistrationCompletionEvent;
import com.himanshu.spring.security.model.UserModel;
import com.himanshu.spring.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompletionEvent(
                user,
                applicationUrl(request)
        ));
        return user.toString();
    }

    private String applicationUrl(HttpServletRequest request){
        return "http://" + request.getServerName() +
                ":" + request.getServerPort() +
                request.getContextPath();
    }

}

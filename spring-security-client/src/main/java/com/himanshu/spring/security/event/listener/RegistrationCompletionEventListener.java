package com.himanshu.spring.security.event.listener;

import com.himanshu.spring.security.event.RegistrationCompletionEvent;
import com.himanshu.spring.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import com.himanshu.spring.security.entity.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompletionEventListener implements
        ApplicationListener<RegistrationCompletionEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompletionEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(user, token);
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;

        //send verification email
        log.info("Click the link to verify you account: {}", url);
    }
}

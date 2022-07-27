package com.himanshu.spring.security.event.listener;

import com.himanshu.spring.security.entity.User;
import com.himanshu.spring.security.event.RegistrationCompletionEvent;
import com.himanshu.spring.security.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationCompletionEventListener implements
        ApplicationListener<RegistrationCompletionEvent> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RegistrationCompletionEventListener.class);
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

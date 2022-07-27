package com.himanshu.spring.security.event;

import com.himanshu.spring.security.entity.User;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

public class RegistrationCompletionEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;

    public RegistrationCompletionEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }

    public User getUser() {
        return this.user;
    }

    public String getApplicationUrl() {
        return this.applicationUrl;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }
}

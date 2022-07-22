package com.himanshu.spring.security.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import com.himanshu.spring.security.entity.User;

@Getter
@Setter
public class RegistrationCompletionEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;

    public RegistrationCompletionEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}

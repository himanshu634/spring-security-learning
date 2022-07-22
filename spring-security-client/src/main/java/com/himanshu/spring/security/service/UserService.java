package com.himanshu.spring.security.service;

import com.himanshu.spring.security.entity.User;
import com.himanshu.spring.security.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);
}

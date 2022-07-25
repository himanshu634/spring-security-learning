package com.himanshu.spring.security.service;

import com.himanshu.spring.security.entity.User;
import com.himanshu.spring.security.entity.VerificationToken;
import com.himanshu.spring.security.model.UserModel;

import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String token);

    User getUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String verifyPasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void resetPassword(User user, String newPassword);

    void deletePasswordResetToken(String token);

    boolean checkIfValidOldPassword(User user, String oldPassword);
}

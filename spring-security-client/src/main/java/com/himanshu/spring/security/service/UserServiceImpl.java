package com.himanshu.spring.security.service;

import com.himanshu.spring.security.entity.PasswordResetToken;
import com.himanshu.spring.security.entity.User;
import com.himanshu.spring.security.entity.VerificationToken;
import com.himanshu.spring.security.model.UserModel;
import com.himanshu.spring.security.repository.PasswordResetTokenRepository;
import com.himanshu.spring.security.repository.UserRepository;
import com.himanshu.spring.security.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public User registerUser(UserModel userModel){
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(
                passwordEncoder.encode(userModel.getPassword())
        );

        userRepository.save(user);

        return user;
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token){
        VerificationToken result = verificationTokenRepository.findByToken(token);

        if(result == null){
            return "valid";
        }

        User user = result.getUser();
        Calendar calendar = Calendar.getInstance();

        if((result.getExpirationTime().getTime()
        - calendar.getTime().getTime()) <= 0){
            verificationTokenRepository.delete(result);
            return "expired";
        }

        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(result);

        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token){
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String verifyPasswordResetToken(String token){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null){
            return "invalid";
        }
        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();

        if((passwordResetToken.getExpirationTime().getTime()
        - calendar.getTime().getTime() <= 0)){
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }
//        passwordResetTokenRepository.delete(passwordResetToken);
        return "valid";
    }

    public Optional<User> getUserByPasswordResetToken(String token){
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    public void resetPassword(User user, String password){
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void deletePasswordResetToken(String token){
        passwordResetTokenRepository.delete(passwordResetTokenRepository.findByToken(token));
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword){
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }


}

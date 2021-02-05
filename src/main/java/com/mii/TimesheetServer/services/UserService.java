/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.configurations.PasswordConfig;
import com.mii.TimesheetServer.entities.User;
import com.mii.TimesheetServer.repositories.StatusRepository;
import com.mii.TimesheetServer.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fahri
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private PasswordConfig passwordConfig;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String email;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    public boolean existById(Integer id) {
        return userRepository.existsById(id);
    }

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User insertUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            user.setPassword(passwordConfig.passwordEncoder().encode(user.getPassword()));
            userRepository.insertUser(user.getId(), user.getUsername(), user.getPassword(), false, -1);
            return userRepository.findById(user.getId()).get();
        }
        return null;
    }

    public User updateUser(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.updateUser(user.getUsername(), user.getPassword(), user.getVerificationCode(), user.getIsVerified(), user.getStatus().getId(), user.getId());
            return userRepository.findById(user.getId()).get();
        }
        return null;
    }

    public void deleteUser(Integer id) {
        userRepository.deleteUser(id);
    }

    public void resetToVerified(User user) {
        user.setVerificationCode(null);
        user.setIsVerified(true);
        user.setStatus(statusRepository.getOne(0));
        updateUser(user);
    }

    public void addStatusOne(User user) {
        user.setStatus(statusRepository.getOne(user.getStatus().getId() + 1));
        if (user.getStatus().getId() == 3) {
            user.setIsVerified(false);
        }
        updateUser(user);
    }

    public void sendResetPassword(User user) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String generatedRandomPassword = generateRandomString();
        helper.setFrom(email);
        helper.setTo(user.getEmployees().getEmail());
        helper.setSubject("Reset Password");
        message.setText("<h1>Reset Password</h1> "
                + "<br><br>"
                + "<p>Dear, " + user.getUsername() + "</p>"
                + "<p>You requested to reset your password. Please keep it secret and change it right after you login for security</p>"
                + "<p>New Password: " + generatedRandomPassword + "</p>"
                + "<br><br>"
                + "<p>Thank You</p>",
                "UTF-8", "html");
        user.setPassword(passwordConfig.passwordEncoder().encode(generatedRandomPassword));
        resetToVerified(user);
        javaMailSender.send(message);
    }

    public String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}

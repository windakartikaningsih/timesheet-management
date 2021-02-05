/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.User;
import java.util.UUID;
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
public class VerificationService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;
    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    public VerificationService(JavaMailSender javaMailSender, UserService userService) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }

    public void sendVerificationEmail(User user, Employees employee, String password) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        UUID uuid = UUID.randomUUID();
        // From
        helper.setFrom(email);
        // To
        helper.setTo(employee.getEmail());
        // Subject
        helper.setSubject("Your Account Has Created");
        // Body
        message.setText("<h1>Account Created</h1> "
                + "<p>Dear, "+ employee.getLastName() +"</p> "
                + "<p>We'd like to inform you that your account for MII Timesheet Website has been created.</p> "
                + "<p>Please verified your account first before you logging in. Here is your account info.</p> "
                + "<br>"
                + "<p>please do not share this verification code</p> "
                + "<h4>Username: " + user.getUsername() + "</h4> "
                + "<h4>Password: " + password + "</h4> "
                + "<h4>Verification code: " + uuid.toString() + "</h4> "
                + "<br><br>"
                + "<p>Thank you.</p> "
                + "<p>Verification link: http://localhost:8070/verification/" + user.getId() + "</p>", "UTF-8", "html");
        user.setVerificationCode(uuid.toString());
        userService.updateUser(user);
        javaMailSender.send(message);
    }
}

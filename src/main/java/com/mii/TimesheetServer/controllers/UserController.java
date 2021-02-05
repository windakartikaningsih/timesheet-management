/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.configurations.PasswordConfig;
import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.Status;
import com.mii.TimesheetServer.entities.User;
import com.mii.TimesheetServer.entities.data.PasswordStacker;
import com.mii.TimesheetServer.entities.data.ResponseMessage;
import com.mii.TimesheetServer.services.EmployeeService;
import com.mii.TimesheetServer.services.StatusService;
import com.mii.TimesheetServer.services.UserService;
import com.mii.TimesheetServer.services.VerificationService;
import java.util.List;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Fahri
 */
@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private VerificationService verifService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PasswordConfig passwordConfig;

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAll() {
        try {
            List<User> users = userService.getAll();
            return new ResponseEntity<>(users, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getById(@PathVariable Integer id) {
        if (userService.existById(id)) {
            User user = userService.getById(id).get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/username/{username}")
    public ResponseEntity getByUsername(@PathVariable String username) {
        if (userService.existByUsername(username)) {
            User user = userService.getByUsername(username).get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity insertUser(@RequestBody User user) {
        if (!userService.existById(user.getId())) {
            try {
                String password = userService.generateRandomString();
                user.setPassword(password);
                Employees employee = employeeService.findById(user.getId()).get();
                userService.insertUser(user);
                User newUser = userService.getById(user.getId()).get();
                verifService.sendVerificationEmail(newUser, employee, password);
                return new ResponseEntity<>(new ResponseMessage("created", user), HttpStatus.OK);
            } catch (MessagingException e) {
                return new ResponseEntity<>(new ResponseMessage("failed " + e, null), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ResponseMessage("user already exist", null), HttpStatus.CONFLICT);
    }

    @PutMapping("/user/{id}/change-password")
    public ResponseEntity changePasswordUser(@PathVariable Integer id, @RequestBody PasswordStacker password) {
        User user = userService.getById(id).get();
        if (passwordConfig.passwordEncoder().matches(password.getOldPassword(), user.getPassword())) {
            String newPass = passwordConfig.passwordEncoder().encode(password.getNewPassword());
            user.setPassword(newPass);
            userService.updateUser(user);
            return new ResponseEntity("password change", HttpStatus.OK);
        } else {
            return new ResponseEntity("failed", HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @PostMapping("/user/{id}/verification")
    public ResponseEntity verifUser(@PathVariable Integer id, @RequestBody String verifCode) {
        if (userService.existById(id)) {
            User user = userService.getById(id).get();
            if (user.getVerificationCode() == null) {
                return new ResponseEntity<>(new ResponseMessage("user has been verified", false), HttpStatus.OK);
            }
            if (!user.getVerificationCode().equals(verifCode)) {
                return new ResponseEntity<>(new ResponseMessage("verification code does not match", false), HttpStatus.BAD_REQUEST);
            }
            Status status = new Status(0);
            System.out.println(status);
            user.setVerificationCode(null);
            user.setIsVerified(true);
            user.setStatus(status);
            userService.updateUser(user);
            return new ResponseEntity<>(new ResponseMessage("user has been verified", true), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("data not found", false), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user/{id}/reset-status")
    public ResponseEntity resetStatus(@PathVariable Integer id) {
        if (userService.existById(id)) {
            try {
                User user = userService.getById(id).get();
                userService.resetToVerified(user);
                return new ResponseEntity<>(new ResponseMessage("reset complete", id), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseMessage("error", id), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ResponseMessage("data not found", id), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(new ResponseMessage("deleted", id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("failed", null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/user/reset-password")
    public ResponseEntity resetPassword(@RequestBody User user) {
        if (userService.existByUsername(user.getUsername())) {
            User checkUser = userService.getByUsername(user.getUsername()).get();
            String oldEmail = checkUser.getEmployees().getEmail();
            String insertedEmail = user.getEmployees().getEmail();
            if (oldEmail.equals(insertedEmail)) {
                try {
                    userService.sendResetPassword(checkUser);
                    return new ResponseEntity<>(new ResponseMessage<>("password changed, please check your email", user), HttpStatus.OK);
                } catch (MessagingException e) {
                    return new ResponseEntity<>(new ResponseMessage<>("error " + e, null), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new ResponseMessage<>("username and email doesn't match", null), HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return new ResponseEntity<>(new ResponseMessage<>("username doesn't exist", null), HttpStatus.NOT_FOUND);
        }
    }
}

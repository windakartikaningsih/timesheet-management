/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mii.TimesheetClient.services.AuthenticationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import com.mii.TimesheetClient.models.data.ResponseMessage;
import com.mii.TimesheetClient.models.data.PasswordStacker;
import com.mii.TimesheetClient.models.User;
import com.mii.TimesheetClient.models.Employees;
import com.mii.TimesheetClient.services.UserService;
import java.util.Collection;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author windak
 */
@Controller
public class LoginController {

    @Autowired
    AuthenticationService service;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String root() {
        Collection<? extends GrantedAuthority> auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
        SimpleGrantedAuthority rm = new SimpleGrantedAuthority("ROLE_RELATION_MANAGER");
        if (auth.contains(admin)) {
            return "redirect:/super-admin";
        } else if (auth.contains(rm)) {
            return "redirect:/relation-manager";
        } else {
            return "redirect:/employee";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginCheck(String username, String password) throws JsonProcessingException {
        try {
            ResponseMessage rm = service.postLogin(username, password);
            List<String> auth = (List<String>) rm.getData();
            service.grantAuth(username, password, auth);
            if (auth.contains("ROLE_ADMIN")) {
                return "redirect:/super-admin";
            } else if (auth.contains("ROLE_RELATION_MANAGER")) {
                return "redirect:/relation-manager";
            } else {
                return "redirect:/employee";
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return "redirect:/login?error";
            }
            return "redirect:/login?locked";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        service.postLogout();
        return "/";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User users = userService.getByUsername(username);
        model.addAttribute("users", users);
        return "change-password";
    }

    @PostMapping("/change-password/{id}")
    public String changePassword(PasswordStacker password, @PathVariable Integer id) {
        try {
            userService.changePassword(id, password);
            return "redirect:/logout";
        } catch (HttpClientErrorException e) {
            return "redirect:/change-password?error";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String newPasswordPage(@RequestParam(name = "username") String username, @RequestParam(name = "email") String email) {
        try {
            User user = new User();
            Employees emp = new Employees();
            emp.setEmail(email);
            user.setUsername(username);
            user.setEmployees(emp);
            ResponseMessage response = userService.resetPassword(user);
            return "redirect:/forgot-password?changed";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return "redirect:/forgot-password?not_found";
            } else if (e.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
                return "redirect:/forgot-password?not_match";
            }
            return "redirect:/forgot-password?error";
        }
    }

    @GetMapping("/verification/{id}")
    public String verificationPage(@PathVariable Integer id) {
        return "verification";
    }

    @PostMapping("/verification/{id}")
    public String verificationPageProcess(@PathVariable Integer id, @RequestParam String code, HttpServletResponse response) {
        try {
            userService.verificationUser(id, code);
            System.out.println(response.getStatus());
            return "redirect:/verification/" + id + "?success";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST){
                return "redirect:/verification/" + id + "?not_match";
            }
            return "redirect:/verification/" + id + "?error";
        }
    }
}

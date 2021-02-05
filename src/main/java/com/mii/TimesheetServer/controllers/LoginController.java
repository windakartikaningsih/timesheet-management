/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.auth.AuthDataRequest;
import com.mii.TimesheetServer.entities.data.ResponseMessage;
import com.mii.TimesheetServer.services.AuthenticationService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Fahri
 */
@RestController
@RequestMapping("/api")
public class LoginController {
    
    @Autowired
    private AuthenticationService service;
    
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody AuthDataRequest request) {
        ResponseMessage responseMessage = service.login(request);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        if (responseMessage.getMessage().equalsIgnoreCase("success")) {
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } else if (responseMessage.getMessage().equalsIgnoreCase("failed")) {
            return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>("logout", HttpStatus.OK);
    }
}

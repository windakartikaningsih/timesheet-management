/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.configurations.loginevent;

import com.mii.TimesheetServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author Fahri
 */
@Component
public class BadCredentialEvent implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        userService.addStatusOne(userService.getByUsername(e.getAuthentication().getName()).get());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.configurations.EmailConfig;
import com.mii.TimesheetServer.entities.MyUserDetails;
import com.mii.TimesheetServer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fahri
 */
@Service
public class MyUserDetailService implements UserDetailsService{

    @Autowired
    private EmailConfig emailConfig;
    @Autowired
    private UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!emailConfig.isValid(username)) {
            User user = userService.getByUsername(username).get();
            if (user == null) {
                throw new UsernameNotFoundException("Username not found");
            }
            return new MyUserDetails(user);
        } else{
            User user = userService.getByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("Email not found");
            }
            return new MyUserDetails(user);
        }
    }
    
}

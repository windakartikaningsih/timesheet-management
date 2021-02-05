/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.MyUserDetails;
import com.mii.TimesheetServer.entities.auth.AuthDataRequest;
import com.mii.TimesheetServer.entities.data.ResponseMessage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fahri
 */
@Service
public class AuthenticationService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailService service;

    public ResponseMessage login(AuthDataRequest request){
        MyUserDetails user = (MyUserDetails) service.loadUserByUsername(request.getUsername());
        if (user.isAccountNonLocked()) {
            try {
                UsernamePasswordAuthenticationToken authToken
                        = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
                Authentication auth = authenticationManager.authenticate(authToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
                return createResponse(user);
            } catch (AuthenticationException e){
                return new ResponseMessage("failed", null);
            }
        }
        return new ResponseMessage("locked", null);
    }

    public ResponseMessage createResponse(MyUserDetails user) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority authority : user.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        return new ResponseMessage("success", authorities);
    }
    
}

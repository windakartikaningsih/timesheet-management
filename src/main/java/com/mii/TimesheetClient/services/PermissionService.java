/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import com.mii.TimesheetClient.models.Permission;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author windak
 */
@Service
public class PermissionService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    AuthenticationService service;
    
    @Value("${api.url}/")
    private String url;
    
    public HttpEntity header() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        HttpHeaders header = service.createHeaders(auth.getName(), auth.getCredentials().toString());
        HttpHeaders header = service.createHeaders();
        HttpEntity<?>  param = new HttpEntity<>(header);
        return param;
    }
    
    // ============================== Permission ============================== //
    public List<Permission> getAllPermissions() {
        ResponseEntity<List<Permission>> response = restTemplate.exchange(url + "permission", HttpMethod.GET, header(), 
                new ParameterizedTypeReference<List<Permission>>() {
        });
        List<Permission> permission = response.getBody();
        return permission;
    }
    // ============================== End Permission ============================== //
}

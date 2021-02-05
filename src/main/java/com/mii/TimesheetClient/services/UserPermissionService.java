/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

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
import com.mii.TimesheetClient.models.UserPermission;
import com.mii.TimesheetClient.models.data.ResponseMessage;

/**
 *
 * @author windak
 */
@Service
public class UserPermissionService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    AuthenticationService service;
    
    @Value("${api.url}/user-permission")
    private String url;
  
    // ============================== User Permission ============================== //
    public List<UserPermission> getAllUserPermission() {
        ResponseEntity<List<UserPermission>> response 
                = restTemplate.exchange(url, HttpMethod.GET, 
                    new HttpEntity<>(service.createHeaders()), 
                    new ParameterizedTypeReference<List<UserPermission>>() {
        });
        List<UserPermission> uper = response.getBody();
        return uper;
    }
    
    public UserPermission getUserPermissionById(Integer id){
        ResponseEntity<UserPermission> response = 
                restTemplate.exchange(url + "/" + id, HttpMethod.GET, 
                        new HttpEntity<>(service.createHeaders()), 
                        new ParameterizedTypeReference<UserPermission>() {
        });
        UserPermission uper = response.getBody();
        return uper;
    }
    
    public ResponseMessage createUserPermission(UserPermission userpermit){
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(userpermit, header);
        ResponseEntity<ResponseMessage> upermit = restTemplate.exchange(url, HttpMethod.POST, 
                httpEntity, ResponseMessage.class);
        return upermit.getBody();
    }
    
    public ResponseMessage updateUserPermission(Integer id, UserPermission userpermit){
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(userpermit, header);
        ResponseEntity<ResponseMessage> upermit = restTemplate.exchange(url + "/" + id, 
                HttpMethod.PUT, httpEntity, ResponseMessage.class);
        return upermit.getBody();
    }
    
    public String deleteUserPermission(Integer id){
        System.out.println(url + id);
        ResponseEntity<String> response = restTemplate.exchange(url + "/" + id, 
                HttpMethod.DELETE, new HttpEntity<>(service.createHeaders()),
                String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }
    
    // ============================== End User Permission ============================== //
}

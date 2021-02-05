/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import com.mii.TimesheetClient.models.UserRole;
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
import com.mii.TimesheetClient.models.data.ResponseMessage;

/**
 *
 * @author windak
 */
@Service
public class UserRoleService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    AuthenticationService service;
    
    @Value("${api.url}/user-role")
    private String url;
    
//    public HttpEntity header() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        HttpHeaders header = service.createHeaders(auth.getName(), auth.getCredentials().toString());
//        HttpHeaders header = service.createHeaders();
//        HttpEntity<?>  param = new HttpEntity<>(header);
//        return param;
//    }
    
    // ============================== User Role ============================== //
    public List<UserRole> getAllUserRole() {
        ResponseEntity<List<UserRole>> response = restTemplate.exchange(url, HttpMethod.GET, 
                new HttpEntity<>(service.createHeaders()), 
                new ParameterizedTypeReference<List<UserRole>>() {
        });
        List<UserRole> urole = response.getBody();
        return urole;
    }
    
    public UserRole getUserRoleById(Integer id){
        ResponseEntity<UserRole> response = restTemplate.exchange(url + "/" + id, HttpMethod.GET, 
                new HttpEntity<>(service.createHeaders()), 
                new ParameterizedTypeReference<UserRole>() {
        });
        UserRole urole = response.getBody();
        return urole;
    }
    
    public ResponseMessage createUserRole(UserRole userrole){
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> httpEntity =  new HttpEntity<>(userrole, header);
        ResponseEntity<ResponseMessage> urole = restTemplate.exchange(url, HttpMethod.POST, 
                httpEntity, 
                ResponseMessage.class);
        return urole.getBody();
    }
    
    public ResponseMessage updateUserRole(Integer id, UserRole userrole){
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(userrole, header);
        ResponseEntity<ResponseMessage> urole = restTemplate.exchange(url + "/" + id, 
                HttpMethod.PUT, 
                httpEntity, 
                ResponseMessage.class);
        return urole.getBody();
    }
    
    public String deleteUserRole(Integer id){
        ResponseEntity<String> response = restTemplate.exchange(url + "/" + id, 
                HttpMethod.DELETE, 
                new HttpEntity<>(service.createHeaders()), 
                String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }
    
    // ============================== End User Role ============================== //
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import com.mii.TimesheetClient.models.User;
import com.mii.TimesheetClient.models.data.ResponseMessage;
import com.mii.TimesheetClient.models.data.PasswordStacker;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Fahri
 */
@Service
public class UserService {
    
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AuthenticationService authService;
    
    @Value("${api.url}/user/")
    private String url;
    
        public List<User> getAllUser() {
        ResponseEntity<List<User>> response = restTemplate.exchange(url, 
                HttpMethod.GET, 
                new HttpEntity<>(authService.createHeaders()), 
                new ParameterizedTypeReference<List<User>>() {
        });
        List<User> user = response.getBody();
        return user;
    }
    
    public User getByUsername(String username){
        ResponseEntity<User> response = 
                restTemplate.exchange(url + "username/" + username, 
                        HttpMethod.GET, 
                        new HttpEntity<>(authService.createHeaders()),
                        User.class);
        return response.getBody();
    }
    
    public ResponseMessage createUser(User body){
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(body, header);
        ResponseEntity<ResponseMessage> response = restTemplate.exchange(url,
                HttpMethod.POST, 
                httpEntity, 
                ResponseMessage.class);
        return response.getBody();
    }
    
    public ResponseMessage deleteUser(Integer id){
        ResponseEntity<ResponseMessage> response = 
                restTemplate.exchange(url + id, 
                        HttpMethod.DELETE, 
                        new HttpEntity<>(authService.createHeaders()),
                        ResponseMessage.class);
        System.out.println(response.getStatusCode());
        return response.getBody();
    }
    
    public ResponseMessage resetVerif(Integer id){
        ResponseEntity<ResponseMessage> response = 
                restTemplate.exchange(url + id + "/reset-status", 
                        HttpMethod.PUT, 
                        new HttpEntity<>(authService.createHeaders()),
                        ResponseMessage.class);
        return response.getBody();
    }
    
    public ResponseMessage verificationUser(Integer id, String verifCode){
        HttpEntity<?> httpEntity = new HttpEntity<>(verifCode);
        ResponseEntity<ResponseMessage> response = 
                restTemplate.exchange(url + id + "/verification", 
                        HttpMethod.POST, 
                        httpEntity,
                        ResponseMessage.class);
        return response.getBody();
    }
    
    public String changePassword(Integer id, PasswordStacker password) {
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(password, header);
        ResponseEntity<String> response = restTemplate.exchange(url + id + "/change-password",
                HttpMethod.PUT, httpEntity, String.class);
        System.out.println(response.getStatusCode());
        return response.getBody();
    }
    
    public ResponseMessage resetPassword(User body){
        HttpEntity<?> httpEntity = new HttpEntity<>(body);
        ResponseEntity<ResponseMessage> response = restTemplate.exchange(url + "reset-password",
                HttpMethod.POST, 
                httpEntity, 
                ResponseMessage.class);
        return response.getBody();
    }
}

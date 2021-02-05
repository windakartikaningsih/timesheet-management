/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.mii.TimesheetClient.models.data.ResponseMessage;
import com.mii.TimesheetClient.models.auth.AuthDataRequest;
/**
 *
 * @author windak
 */
@Service
public class AuthenticationService<T> {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${api.url}/")
    private String url;
    
    public HttpHeaders createHeaders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String password = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }
    
    public ResponseMessage postLogin(String username, String password) {
        AuthDataRequest request = new AuthDataRequest(username, password);
        ResponseEntity<ResponseMessage> response = 
                restTemplate.postForEntity(url + "login", request, ResponseMessage.class);
        return response.getBody();
    }
    
    public void postLogout(){
        restTemplate.postForEntity(url + "logout", null, null);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
    
    public void grantAuth(String username, String password, List<String> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}

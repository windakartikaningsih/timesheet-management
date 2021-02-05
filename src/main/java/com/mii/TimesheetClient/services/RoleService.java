/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import com.mii.TimesheetClient.models.Role;
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
public class RoleService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    AuthenticationService service;
    
    @Value("${api.url}/role")
    private String url;
    
    public HttpEntity header() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        HttpHeaders header = service.createHeaders(auth.getName(), auth.getCredentials().toString());
        HttpHeaders header = service.createHeaders();
        HttpEntity<?>  param = new HttpEntity<>(header);
        return param;
    }
    
    // ============================== Role ============================== //
    public List<Role> getAllRoles() {
        ResponseEntity<List<Role>> response = restTemplate.exchange(url, HttpMethod.GET, header(), 
                new ParameterizedTypeReference<List<Role>>() {
        });
        return response.getBody();
    }
    
    public Role getRoleById(Integer id){
        ResponseEntity<Role> response = 
                restTemplate.exchange(url + "role/" + id, HttpMethod.GET, header(), 
                        new ParameterizedTypeReference<Role>() {
        });
        Role role = response.getBody();
        return role;
    }
    
    public Role createRole(Role role) {
        Role r = new Role();
        r.setId(role.getId());
        r.setRoleName(role.getRoleName());
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> param = new HttpEntity<> (r, header);
        ResponseEntity<Role> response = restTemplate.exchange(url + "role", HttpMethod.POST, param, 
                new ParameterizedTypeReference<Role>() {});;
        return response.getBody();
    }
    
    public Role updateRole(Role role) {
        Role r = new Role();
        r.setId(role.getId());
        r.setRoleName(role.getRoleName());
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> param = new HttpEntity<> (r, header);
        ResponseEntity<Role> response = restTemplate.exchange(url + "role", HttpMethod.PUT, param, 
                new ParameterizedTypeReference<Role>() {});;
        return response.getBody();
    }
    
    public Role deleteRole(Integer id) {
        ResponseEntity<Role> response = restTemplate.exchange(url + "role/" + id, HttpMethod.DELETE, 
                header(), new ParameterizedTypeReference<Role>() {});
        return response.getBody();
    }
    // ============================== End Role ============================== //
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import com.mii.TimesheetClient.models.Division;
import com.mii.TimesheetClient.models.Employees;
import com.mii.TimesheetClient.models.ManagerSite;
import com.mii.TimesheetClient.models.Permission;
import com.mii.TimesheetClient.models.Role;
import com.mii.TimesheetClient.models.Site;
import com.mii.TimesheetClient.models.UserPermission;
import com.mii.TimesheetClient.models.UserRole;
import com.mii.TimesheetClient.models.data.ResponseMessage;
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
public class DivisionService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    AuthenticationService authService;
    
    @Value("${api.url}/division")
    private String url;
    
    // ============================== Division ============================== //
    public List<Division> getAllDivision() {
        ResponseEntity<List<Division>> response 
                = restTemplate.exchange(url, HttpMethod.GET, 
                        new HttpEntity<>(authService.createHeaders()), 
                        new ParameterizedTypeReference<List<Division>>() {
        });
        List<Division> div = response.getBody();
        return div;
    }
    
    public Division getDivisionById(String id){
        ResponseEntity<Division> response 
                = restTemplate.exchange(url + "/" + id, HttpMethod.GET, 
                    new HttpEntity<>(authService.createHeaders()), 
                    new ParameterizedTypeReference<Division>() {
        });
        Division div = response.getBody();
        return div;
    }

    public ResponseMessage createDivision(Division division) {
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(division, header);
        ResponseEntity<ResponseMessage> dv = restTemplate.exchange(url, HttpMethod.POST,
                httpEntity, ResponseMessage.class);
        return dv.getBody();
    }

    public ResponseMessage updateDivision(String id, Division division) {
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(division, header);
        ResponseEntity<ResponseMessage> dv = restTemplate.exchange(url + "/" + id,
                HttpMethod.PUT, httpEntity, ResponseMessage.class);
        return dv.getBody();
    }

    public String deleteDivision(String id) {
        System.out.println(url + id);
        ResponseEntity<String> response = restTemplate.exchange(url + "/" + id,
                HttpMethod.DELETE, new HttpEntity<>(authService.createHeaders()),
                String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }
    // ============================== End Division ============================== //
    
    
    
    
    
    
    
}

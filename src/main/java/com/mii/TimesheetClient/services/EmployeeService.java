/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import com.mii.TimesheetClient.models.Employees;
import com.mii.TimesheetClient.models.Division;
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
public class EmployeeService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    AuthenticationService authService;
    
    @Value("${api.url}/employee")
    private String url;
    
    // ============================== Employee ============================== //
    public List<Employees> getAllEmployee() {
        ResponseEntity<List<Employees>> response 
                = restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(authService.createHeaders()),
                    new ParameterizedTypeReference<List<Employees>>() {
                });
        List<Employees> emp = response.getBody();
        return emp;
    }
    
    public Employees getEmployeeById(Integer id){
        ResponseEntity<Employees> response = 
                restTemplate.exchange(url + "/" + id, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()), 
                        new ParameterizedTypeReference<Employees>() {
        });
        Employees emp = response.getBody(); 
        return emp;
    }
    
    public List<Employees> getEmployeeBySite(String siteId){
        ResponseEntity<List<Employees>> response = 
                restTemplate.exchange(url + "/site/" + siteId, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()), 
                        new ParameterizedTypeReference<List<Employees>>() {
        });
        return response.getBody();
    }
    
    public ResponseMessage createEmployee(Employees emp){
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(emp, header);
        ResponseEntity<ResponseMessage> employees = restTemplate.exchange(url, HttpMethod.POST, 
                httpEntity, ResponseMessage.class);
        return employees.getBody();
    }
    
    public ResponseMessage updateEmployee(Integer id, Employees emp){
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(emp, header);
        ResponseEntity<ResponseMessage> employees = restTemplate.exchange(url + "/" + id, 
                HttpMethod.PUT, httpEntity, ResponseMessage.class);
        return employees.getBody();
    }

    public String deleteEmployee(Integer id){
        System.out.println(url + id);
        ResponseEntity<String> response = restTemplate.exchange(url + "/" + id, 
                HttpMethod.DELETE, new HttpEntity<>(authService.createHeaders()),
                String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }
    // ============================== End Employee ============================== //
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import com.mii.TimesheetClient.models.StatusAttendance;
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
 * @author BlackPearl
 */
@Service
public class StatusAttendanceService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    AuthenticationService service;
    
    @Value("${api.url}/status-attendance/")
    private String url;
    
    
    public HttpEntity header(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> param = new HttpEntity<>(header);
        return param;
    }
    
    public List<StatusAttendance> getAllStatusAttendance(){
        ResponseEntity<List<StatusAttendance>> attendance = restTemplate.exchange(url, 
                HttpMethod.GET, 
                new HttpEntity<>(service.createHeaders()),
                new ParameterizedTypeReference<List<StatusAttendance>>(){
            });
        List<StatusAttendance> att = attendance.getBody();
        return att;
    }
    
    public StatusAttendance getStatusAttendanceById(Integer id){
        ResponseEntity<StatusAttendance> attendance = restTemplate.exchange(url + id, 
                HttpMethod.GET, 
                new HttpEntity<>(service.createHeaders()),
                new ParameterizedTypeReference<StatusAttendance>() {
            });
        StatusAttendance att = attendance.getBody();
        return att;
    }
}

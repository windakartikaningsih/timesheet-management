/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.mii.TimesheetClient.models.NoteHistory;
import com.mii.TimesheetClient.models.data.NoteData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
/**
 *
 * @author Fahri
 */
@Service
public class NoteHistoryService {
    
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AuthenticationService authService;
    @Value("${api.url}/note-history/")
    private String url;
    
    public List<NoteHistory> getAllHistories(){
        ResponseEntity<List<NoteHistory>> response
                = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()),
                        new ParameterizedTypeReference<List<NoteHistory>>() {});
        return response.getBody();
    }
    
    public String sendNote(NoteData note){
        ResponseEntity<String> response
                = restTemplate.exchange(url, HttpMethod.POST,
                        new HttpEntity<>(note, authService.createHeaders()),
                        new ParameterizedTypeReference<String>() {});
        return response.getBody();
    }
    
    public List<NoteHistory> getByReport(String reportId){
        ResponseEntity<List<NoteHistory>> response
                = restTemplate.exchange(url + "report/" + reportId, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()),
                        new ParameterizedTypeReference<List<NoteHistory>>() {});
        return response.getBody();
    }
    
    public List<NoteHistory> getByRead(Integer employeeId, Integer readStatus){
        ResponseEntity<List<NoteHistory>> response
                = restTemplate.exchange(url + "read/" + employeeId + "/" + readStatus, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()),
                        new ParameterizedTypeReference<List<NoteHistory>>() {});
        return response.getBody();
    }
    
    public List<NoteHistory> getByEmployee(Integer employeeId){
        ResponseEntity<List<NoteHistory>> response
                = restTemplate.exchange(url + "employee/" + employeeId, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()),
                        new ParameterizedTypeReference<List<NoteHistory>>() {});
        return response.getBody();
    }
    
    public String readNote(Integer noteId){
        ResponseEntity<String> response
                = restTemplate.exchange(url + "read/" + noteId, HttpMethod.PUT,
                        new HttpEntity<>(authService.createHeaders()),
                        new ParameterizedTypeReference<String>() {});
        return response.getBody();
    }
    
    public NoteHistory getById(Integer id){
        ResponseEntity<NoteHistory> response = 
                restTemplate.exchange(url + id, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()),
                        new ParameterizedTypeReference<NoteHistory>() {});
        return response.getBody();
    }
}

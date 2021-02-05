/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import com.mii.TimesheetClient.models.Task;
import com.mii.TimesheetClient.models.data.ResponseMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
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
public class TaskService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    AuthenticationService service;
    
    @Value("${api.url}/task/")
    private String url;
    
    public HttpEntity header(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> param = new HttpEntity<>(header);
        return param;
    }
    
    public List<Task> getAllTask(){
        ResponseEntity<List<Task>> task = restTemplate.exchange(url, HttpMethod.GET, 
                new HttpEntity<>(service.createHeaders()),
                new ParameterizedTypeReference<List<Task>>(){ 
                });
        List<Task> tsk = task.getBody();
        return tsk;
    }
    
    public Task getTaskById(String id){
        ResponseEntity<Task> task = restTemplate.exchange(url + id, HttpMethod.GET, 
                new HttpEntity<>(service.createHeaders()),
                new ParameterizedTypeReference<Task>() {
                });
        Task tsk = task.getBody();
        return tsk;
    }
    
    public List<Task> getTaskByDateAndEmployee(Integer employeeId, String date){
        ResponseEntity<List<Task>> task = restTemplate.exchange(url + employeeId + "/" + date, HttpMethod.GET, 
                new HttpEntity<>(service.createHeaders()),
                new ParameterizedTypeReference<List<Task>>() {
                });
        return task.getBody();
    }
    
    public ResponseMessage createTask(Task body){
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(body, header);
        ResponseEntity<ResponseMessage> task = restTemplate.exchange(url, HttpMethod.POST, 
                httpEntity,
                ResponseMessage.class);
        return task.getBody();
    }
    
    public ResponseMessage updateTask(String id, Task body){
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(body, header);
        ResponseEntity<ResponseMessage> task = restTemplate.exchange(url + id, 
                HttpMethod.PUT, 
                httpEntity,
                ResponseMessage.class);
        return task.getBody();
    }
    
    public String deleteTask(String id){
        System.out.println(url + id);
        ResponseEntity<String> response = restTemplate.exchange(url + id, 
                HttpMethod.DELETE, 
                new HttpEntity<>(service.createHeaders()),
                String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }
    
    public ByteArrayResource generateReport(Task body) throws IOException{
        HttpHeaders header = service.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(body, header);
        ResponseEntity<ByteArrayResource> response = restTemplate.exchange(url + "generate", 
                HttpMethod.POST, 
                httpEntity,
                ByteArrayResource.class);
        return response.getBody();
    }
}

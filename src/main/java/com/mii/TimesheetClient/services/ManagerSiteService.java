/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import com.mii.TimesheetClient.models.ManagerSite;
import com.mii.TimesheetClient.models.Site;
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
public class ManagerSiteService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    AuthenticationService authService;
    
    @Value("${api.url}/manager-site")
    private String url;
    
    // ============================== Mananger Site ============================== //
    public List<ManagerSite> getAllManagerSite() {
        ResponseEntity<List<ManagerSite>> response 
                = restTemplate.exchange(url, HttpMethod.GET, 
                    new HttpEntity<>(authService.createHeaders()), 
                    new ParameterizedTypeReference<List<ManagerSite>>() {
        });
        List<ManagerSite> ms = response.getBody();
        return ms;
    }
    
    public ManagerSite getManagerSiteById(Integer id){
        ResponseEntity<ManagerSite> response = 
                restTemplate.exchange(url + "/" + id, HttpMethod.GET, 
                        new HttpEntity<>(authService.createHeaders()),
                        new ParameterizedTypeReference<ManagerSite>() {
        });
        ManagerSite man = response.getBody();
        return man;
    }

    public ResponseMessage createManagerSite(ManagerSite manager) {
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(manager, header);
        ResponseEntity<ResponseMessage> ms = restTemplate.exchange(url, HttpMethod.POST,
                httpEntity, ResponseMessage.class);
        return ms.getBody();
    }

    public ResponseMessage updateManagerSite(Integer id, ManagerSite manager) {
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(manager, header);
        ResponseEntity<ResponseMessage> ms = restTemplate.exchange(url + "/" + id,
                HttpMethod.PUT, httpEntity, ResponseMessage.class);
        return ms.getBody();
    }

    public String deleteManagerSite(Integer id) {
        ResponseEntity<String> response = restTemplate.exchange(url + "/" + id,
                HttpMethod.DELETE, new HttpEntity<>(authService.createHeaders()),
                String.class);
        System.out.println(response.getStatusCode());
        return response.getBody();
    }
    // ============================== End Manager Site ============================== //
}

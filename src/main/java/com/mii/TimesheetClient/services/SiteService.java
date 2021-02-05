/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.services;

import com.mii.TimesheetClient.models.Site;
import com.mii.TimesheetClient.models.data.ResponseMessage;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

/**
 *
 * @author windak
 */
@Service
public class SiteService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    AuthenticationService authService;
    
    @Value("${api.url}/site")
    private String url;
    
    // ============================== Site ============================== //
    public String urlToId(HttpServletRequest request) {
        final String path
                = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern
                = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        return new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);
    }
    
    public List<Site> getAllSite() {
        ResponseEntity<List<Site>> response 
                = restTemplate.exchange(url, HttpMethod.GET, 
                        new HttpEntity<>(authService.createHeaders()),  
                        new ParameterizedTypeReference<List<Site>>() {
        });
        List<Site> site = response.getBody();
        return site;
    }
    
    public Site getSiteById(String id){
        ResponseEntity<Site> response = 
                restTemplate.exchange(url + "/" + id, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()), 
                        new ParameterizedTypeReference<Site>() {
        });
        Site site = response.getBody();
        return site;
    }
    
    public List<Site> getAllSiteByRM(Integer rmId) {
        ResponseEntity<List<Site>> response 
                = restTemplate.exchange(url + "/rm/" + rmId, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()), 
                        new ParameterizedTypeReference<List<Site>>() {
        });
        List<Site> site = response.getBody();
        return site;
    }
    
    public ResponseMessage createSite(Site site) {
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(site, header);
        ResponseEntity<ResponseMessage> s = restTemplate.exchange(url, HttpMethod.POST,
                httpEntity, ResponseMessage.class);
        return s.getBody();
    }

    public ResponseMessage updateSite(String id, Site site) {
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(site, header);
        ResponseEntity<ResponseMessage> s = restTemplate.exchange(url + "/" + id,
                HttpMethod.PUT, httpEntity, ResponseMessage.class);
        return s.getBody();
    }

    public String deleteSite(String id) {
        System.out.println(url + id);
        ResponseEntity<String> response = restTemplate.exchange(url + "/" + id,
                HttpMethod.DELETE, new HttpEntity<>(authService.createHeaders()),
                String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }
    // ============================== End Site ============================== //
}

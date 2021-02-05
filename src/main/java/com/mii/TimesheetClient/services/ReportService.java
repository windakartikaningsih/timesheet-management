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
import com.mii.TimesheetClient.models.Report;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.mii.TimesheetClient.models.data.ReportData;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

/**
 *
 * @author Fahri
 */
@Service
public class ReportService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AuthenticationService authService;
    @Value("${api.url}/report/")
    private String url;

    public String urlToId(HttpServletRequest request) {
        final String path
                = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern
                = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        return new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);
    }
    
    public List<Report> getAllReports() {
        ResponseEntity<List<Report>> response
                = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()),
                        new ParameterizedTypeReference<List<Report>>() {
                });
        return response.getBody();
    }

    public Report getById(String id) {
        ResponseEntity<Report> response
                = restTemplate.exchange(url + id, HttpMethod.GET,
                        new HttpEntity<>(authService.createHeaders()),
                        new ParameterizedTypeReference<Report>() {});
        return response.getBody();
    }
    
    public List<Report> getByDateAndEmployee(Integer employeeId, String dateString){
        ResponseEntity<List<Report>> response
                = restTemplate.exchange(url + employeeId + "/" + dateString , HttpMethod.GET, 
                        new HttpEntity<>(authService.createHeaders()), 
                        new ParameterizedTypeReference<List<Report>>(){});
        return response.getBody();
    }
    
    public List<Report> getByMonthAndEmployee(Integer employeeId, String dateString){
        ResponseEntity<List<Report>> response
                = restTemplate.exchange(url + "month/" + employeeId + "/" + dateString , HttpMethod.GET, 
                        new HttpEntity<>(authService.createHeaders()), 
                        new ParameterizedTypeReference<List<Report>>(){});
        return response.getBody();
    }
    
    public List<Report> getByEmployee(Integer employeeId){
        ResponseEntity<List<Report>> response
                = restTemplate.exchange(url + "employee/" + employeeId , HttpMethod.GET, 
                        new HttpEntity<>(authService.createHeaders()), 
                        new ParameterizedTypeReference<List<Report>>(){});
        return response.getBody();
    }
    
    public String acceptOrRejectReport(String reportId, Boolean acceptStatus){
        String status = (acceptStatus) ? "accept":"reject";
        ResponseEntity<String> response
                = restTemplate.exchange(url + reportId + "/" + status, HttpMethod.PUT, 
                        new HttpEntity<>(authService.createHeaders()), 
                        new ParameterizedTypeReference<String>(){});
        return response.getBody();
    }
    
    public String submitReport(Report report, MultipartFile file) throws IOException {
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();

        HttpHeaders mainHeaders = authService.createHeaders();
        mainHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.parseMediaType(file.getContentType()));

        HttpEntity<ByteArrayResource> attachment;
        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        attachment = new HttpEntity<>(fileResource, fileHeaders);
        
        multipartRequest.set("file", attachment);
        
        
        HttpHeaders reportHeaders = new HttpHeaders();
        reportHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Report> requestEntity = new HttpEntity<>(report, reportHeaders);
        multipartRequest.set("report", requestEntity);
        
        HttpEntity<MultiValueMap<String, Object>> finalRequest = new HttpEntity<>(multipartRequest, mainHeaders);

        ResponseEntity<String> response
                = restTemplate.exchange(url, HttpMethod.POST,
                        finalRequest,
                        String.class);
        System.out.println(response.getStatusCode());
        return response.getBody();
    }
    
    public String updateReport(Report report, MultipartFile file) throws IOException {
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();

        HttpHeaders mainHeaders = authService.createHeaders();
        mainHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.parseMediaType(file.getContentType()));

        HttpEntity<ByteArrayResource> attachment;
        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        attachment = new HttpEntity<>(fileResource, fileHeaders);
        
        multipartRequest.set("file", attachment);
        
        
        HttpHeaders reportHeaders = new HttpHeaders();
        reportHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Report> requestEntity = new HttpEntity<>(report, reportHeaders);
        
        multipartRequest.set("report", requestEntity);
        
        HttpEntity<MultiValueMap<String, Object>> finalRequest = new HttpEntity<>(multipartRequest, mainHeaders);

        ResponseEntity<String> response
                = restTemplate.exchange(url + report.getId(), HttpMethod.PUT,
                        finalRequest,
                        String.class);
        System.out.println(response.getStatusCode());
        return response.getBody();
    }
    
    public String deleteReport(String reportId){
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(header);
        ResponseEntity<String> response = restTemplate.exchange(url + reportId, 
                HttpMethod.DELETE, 
                httpEntity, 
                String.class);
        return response.getBody();
    }
    
    public ByteArrayResource downloadReport(String reportId){
        HttpHeaders header = authService.createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(header);
        ResponseEntity<ByteArrayResource> response = restTemplate.exchange(url + "download/" + reportId, 
                HttpMethod.POST, 
                httpEntity, 
                ByteArrayResource.class);
        return response.getBody();
    }
}

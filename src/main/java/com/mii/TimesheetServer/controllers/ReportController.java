/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.Report;
import com.mii.TimesheetServer.services.EmployeeService;
import com.mii.TimesheetServer.services.ReportService;
import com.mii.TimesheetServer.services.ReportStatusService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Fahri
 */
@RestController
@ControllerAdvice
@RequestMapping("api")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportStatusService reportStatusService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/report")
    public ResponseEntity getAll() {
        try {
            List<Report> reports = reportService.getAll();
            return new ResponseEntity<>(reports, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/report/**")
    public ResponseEntity getById(HttpServletRequest request) {
        String id = reportService.urlToId(request);
        if (reportService.existsById(id)) {
            Report report = reportService.getById(id).get();
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
        return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/report/employee/{id}")
    public ResponseEntity getByEmployee(@PathVariable("id") Integer id) {
        if (employeeService.existById(id)) {
            try {
                Employees employees = employeeService.findById(id).get();
                List<Report> reports = reportService.getByEmployee(employees);
                return new ResponseEntity<>(reports, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/report/{id}/{date}")
    public ResponseEntity getByEmployeeAndDate(@PathVariable("id") Integer id, @PathVariable("date") String stringDate) {
        if (employeeService.existById(id)) {
            try {
                String fullDate = stringDate + "-01-01";
                Employees employee = employeeService.findById(id).get();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fullDate);
                List<Report> reports = reportService.getByDateAndEmployee(date, employee);
                return new ResponseEntity<>(reports, HttpStatus.OK);
            } catch (ParseException e) {
                return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/report/month/{id}/{date}")
    public ResponseEntity getByEmployeeAndMonth(@PathVariable("id") Integer id, @PathVariable("date") String stringDate){
        if (employeeService.existById(id)) {
            try {
                Employees employee = employeeService.findById(id).get();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
                List<Report> report = reportService.getByMonthAndEmployee(date, employee);
                return new ResponseEntity<>(report, HttpStatus.OK);
            } catch (ParseException e) {
                return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/report/download/**")
    public ResponseEntity<ByteArrayResource> downloadReport(HttpServletRequest request) throws IOException {
        String id = reportService.urlToId(request);
        if (reportService.existsById(id)) {
            Report report = reportService.getById(id).get();
            File file = new File(report.getDocumentDir());
            byte[] bytes = Files.readAllBytes(file.toPath());
//            String fileType = Files.probeContentType(file.toPath());
            return new ResponseEntity(new ByteArrayResource(bytes), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/report")
    @PreAuthorize("hasAnyAuthority('CREATE_EMPLOYEE')")
    public ResponseEntity uploadReport(@RequestParam("report") Report report,
            @RequestParam("file") MultipartFile file) {
        try {
            Gson g = new GsonBuilder().setDateFormat("dd-MM-yyyy").setPrettyPrinting().serializeNulls().create();
            Report myReport = g.fromJson(report.getId(), Report.class);
            reportService.insertReport(myReport, file);
            return new ResponseEntity<>("created", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("failed to create", HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("data already exist", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/report/**")
    public ResponseEntity reUploadReport(HttpServletRequest request,
            @RequestParam("report") Report report,
            @RequestParam("file") MultipartFile file) {
        String id = reportService.urlToId(request);
        if (reportService.existsById(id)) {
            try {
                System.out.println(report.getId());
                Gson g = new GsonBuilder().setDateFormat("dd-MM-yyyy").setPrettyPrinting().serializeNulls().create();
                Report myReport = g.fromJson(report.getId(), Report.class);
                Report oldReport = reportService.getById(id).get();
                Date uploadDate = new Date();
////                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(myReport.);
                oldReport.setName(myReport.getName());
                oldReport.setUploadDate(uploadDate);
                oldReport.setReportDate(myReport.getReportDate());
                if (oldReport.getReportStatus().getId() != 2) {
                    oldReport.setReportStatus(reportStatusService.getById(2).get());
                }
                reportService.updateReport(oldReport, file);
                return new ResponseEntity<>("updated", HttpStatus.OK);

            } catch (IOException | JsonSyntaxException e) {
                System.out.println(e);
                return new ResponseEntity<>("failed to update: " + e, HttpStatus.BAD_REQUEST);
            } catch (DataIntegrityViolationException e) {
                return new ResponseEntity<>("data already exist", HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/report/**/accept")
    public ResponseEntity acceptReport(HttpServletRequest request) {
        String id = reportService.urlToId(request).replace("/accept", "");
        String statusChange = reportService.urlToId(request).replace(id + "/", "");
        if (reportService.existsById(id)) {
            Report oldReport = reportService.getById(id).get();
            reportService.changeStatus(oldReport, statusChange);
            return new ResponseEntity<>("report accepted", HttpStatus.OK);
        }
        return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/report/**/reject")
    public ResponseEntity rejectReport(HttpServletRequest request) {
        String id = reportService.urlToId(request).replace("/reject", "");
        String statusChange = reportService.urlToId(request).replace(id + "/", "");
        if (reportService.existsById(id)) {
            Report oldReport = reportService.getById(id).get();
            reportService.changeStatus(oldReport, statusChange);
            return new ResponseEntity<>("report rejected", HttpStatus.OK);
        }
        return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/report/**")
    public ResponseEntity deleteReport(HttpServletRequest request) {
        String id = reportService.urlToId(request);
        if (reportService.existsById(id)) {
            reportService.deleteReport(id);
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("failed to delete", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleFileUploadError() {
        return new ResponseEntity<>("file size too large, max 5MB", HttpStatus.PAYLOAD_TOO_LARGE);
    }
}

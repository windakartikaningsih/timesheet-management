/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.NoteHistory;
import com.mii.TimesheetServer.entities.Report;
import com.mii.TimesheetServer.entities.data.NoteData;
import com.mii.TimesheetServer.repositories.EmployeeRepository;
import com.mii.TimesheetServer.services.EmployeeService;
import com.mii.TimesheetServer.services.NoteHistoryService;
import com.mii.TimesheetServer.services.ReportService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Fahri
 */
@RestController
@RequestMapping("api")
public class NoteHistoryController {

    @Autowired
    private NoteHistoryService historyService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("note-history")
    public ResponseEntity getAll() {
        try {
            List<NoteHistory> notes = historyService.getAll();
            return new ResponseEntity(notes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("failed to get", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("note-history/{id}")
    public ResponseEntity getById(@PathVariable Integer id) {
        if (historyService.existsById(id)) {
            NoteHistory note = historyService.getById(id).get();
            return new ResponseEntity(note, HttpStatus.OK);
        }
        return new ResponseEntity("data not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("note-history/report/**")
    public ResponseEntity getByReport(HttpServletRequest request) {
        String id = reportService.urlToId(request);
        if (reportService.existsById(id)) {
            List<NoteHistory> notes = historyService.getByReport(reportService.getById(id).get());
            return new ResponseEntity<>(notes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("note-history/read/{id}/{isRead}")
    public ResponseEntity getByRead(@PathVariable("isRead") Boolean isRead, @PathVariable("id") Integer id) {
        try {
            List<NoteHistory> notes = historyService.getByReadAndEmployee(isRead, id);
            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("note-history/employee/{id}")
    public ResponseEntity getByEmployee(@PathVariable Integer id) {
        try {
            List<NoteHistory> notes = historyService.getByEmployee(id);
            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("data not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("note-history")
    public ResponseEntity insertNote(@RequestBody NoteData data) {
        try {
            System.out.println(data);
            Boolean statusChange = data.getIsAccepted();
            if (data.getReport() == null) {
                Report newReport = new Report();
                Employees employee = employeeService.findById(data.getEmployee().getId()).get();
                String dateId = new SimpleDateFormat("MM/yy").format(data.getReportDate());
                newReport.setReportDate(data.getReportDate());
                newReport.setEmployees(employee);
                reportService.insertEmptyReport(newReport);
//                Report report = reportService.getByReportDate(data.getReportDate()).get();
                newReport.setId("RPT/" + data.getEmployee().getId() + "/" + dateId);
                NoteHistory note = historyService.constructNote(data);
                note.setReport(newReport);
                System.out.println(note);
                historyService.sendNote(note);
                historyService.sendNotesMail(note);
            } else if (data.getReport() != null) {
                Report report = reportService.getById(data.getReport().getId()).get();
                NoteHistory note = historyService.constructNote(data);
                note.setReport(report);
                note.setEmployees(employeeService.findById(data.getRelationManager().getId()).get());
                historyService.sendNote(note);
                historyService.sendNotesMail(note);
                if (statusChange == null) {
                    System.out.println("just send");
                } else if (statusChange) {
                    reportService.changeStatus(report, "accept");
                } else if (!statusChange) {
                    reportService.changeStatus(report, "reject");
                }
            }
            return new ResponseEntity("sent", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("failed to create", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("note-history/read/{id}")
    public ResponseEntity readNote(@PathVariable Integer id) {
        if (historyService.existsById(id)) {
            historyService.readNote(id);
            return new ResponseEntity("read", HttpStatus.OK);
        }
        return new ResponseEntity("unread", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("note-history/{id}")
    public ResponseEntity deleteNote(@PathVariable Integer id) {
        try {
            historyService.deleteNote(id);
            return new ResponseEntity("deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("failed to delete", HttpStatus.BAD_REQUEST);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Fahri
 */
@Data
public class Report {

    private String id, name;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String reportDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String uploadDate;
    private List<NoteHistory> noteHistoryList;
    private Employees employees;
    private ReportStatus reportStatus;

    public Report(String id, String name, String reportDate, String uploadDate, List<NoteHistory> noteHistoryList, Employees employees, ReportStatus reportStatus) {
        this.id = id;
        this.name = name;
        this.reportDate = reportDate;
        this.uploadDate = uploadDate;
        this.noteHistoryList = noteHistoryList;
        this.employees = employees;
        this.reportStatus = reportStatus;
    }

    public Report() {
    }
}

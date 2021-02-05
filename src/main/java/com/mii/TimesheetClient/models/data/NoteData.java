/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models.data;

import com.mii.TimesheetClient.models.Employees;
import com.mii.TimesheetClient.models.Report;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Fahri
 */
@Data
public class NoteData {
    
    private String note;
    private Date createdDate;
    private Boolean isAccepted;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date reportDate;
    private Employees relationManager;
    private Report report;
    private Employees employee;

    public NoteData(String note, Date createdDate, Boolean isAccepted, Date reportDate, Employees relationManager, Report report, Employees employee) {
        this.note = note;
        this.createdDate = createdDate;
        this.isAccepted = isAccepted;
        this.reportDate = reportDate;
        this.relationManager = relationManager;
        this.report = report;
        this.employee = employee;
    }

    public NoteData() {
    }
}
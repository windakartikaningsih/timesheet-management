/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Fahri
 */
@Data
public class NoteHistory {
    
    private Integer id;
    private String note;
    private String createdDate;
    private Boolean isAccepted;
    private Employees employees;
    private Report report;
    private Boolean isRead;

    public NoteHistory(Integer id, String note, String createdDate, Boolean isAccepted, Employees employees, Report report, Boolean isRead) {
        this.id = id;
        this.note = note;
        this.createdDate = createdDate;
        this.isAccepted = isAccepted;
        this.employees = employees;
        this.report = report;
        this.isRead = isRead;
    }

    public NoteHistory() {
    }
    
    
}

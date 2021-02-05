/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models;

import lombok.Data;

/**
 *
 * @author Fahri
 */
@Data
public class ReportStatus {
    
    private Integer id;
    private String reportStatusName;

    public ReportStatus(Integer id, String reportStatusName) {
        this.id = id;
        this.reportStatusName = reportStatusName;
    }

    public ReportStatus() {
    }
}

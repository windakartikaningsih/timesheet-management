/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models.data;
import com.mii.TimesheetClient.models.Report;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Fahri
 */
@Data
public class ReportData {
    
    private Report report;
    private MultipartFile file;

    public ReportData(Report report, MultipartFile file) {
        this.report = report;
        this.file = file;
    }

    public ReportData() {
    }
    
    
}

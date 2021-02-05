/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.ReportStatus;
import com.mii.TimesheetServer.repositories.ReportStatusRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fahri
 */
@Service
public class ReportStatusService {

    @Autowired
    private ReportStatusRepository reportStatusRepository;

    public List<ReportStatus> getAll() {
        return reportStatusRepository.findAll();
    }

    public Optional<ReportStatus> getById(Integer id) {
        return reportStatusRepository.findById(id);
    }

    public boolean existById(Integer id) {
        return reportStatusRepository.existsById(id);
    }
}

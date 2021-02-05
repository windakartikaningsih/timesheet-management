/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.Report;
import com.mii.TimesheetServer.entities.Site;
import com.mii.TimesheetServer.repositories.ReportRepository;
import com.mii.TimesheetServer.repositories.ReportStatusRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    ReportRepository reportRepository;
    @Autowired
    ReportStatusRepository reportStatusRepository;

    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    public Optional<Report> getById(String id) {
        return reportRepository.findById(id);
    }

    public Optional<Report> getByReportDate(Date reportDate) {
        return reportRepository.findByReportDate(reportDate);
    }

    public List<Report> getByEmployee(Employees employees) {
        return reportRepository.findAllByEmployees(employees);
    }

    public List<Report> getByDateAndEmployee(Date date, Employees employees) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer thisYear = cal.get(Calendar.YEAR);
        Date startYear = new SimpleDateFormat("dd-MM-yyyy").parse(1 + "-" + 1 + "-" + thisYear);
        Date endYear = new SimpleDateFormat("dd-MM-yyyy").parse(31 + "-" + 12 + "-" + thisYear);
        List<Report> reports = reportRepository.findAllByReportDateBetweenAndEmployees(startYear, endYear, employees);
        return reports;
    }

    public List<Report> getByMonthAndEmployee(Date date, Employees employees) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer thisYear = cal.get(Calendar.YEAR);
        Integer thisMonth = cal.get(Calendar.MONTH) + 1;
        Integer lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date startYear = new SimpleDateFormat("dd-MM-yyyy").parse(1 + "-" + thisMonth + "-" + thisYear);
        Date endYear = new SimpleDateFormat("dd-MM-yyyy").parse(lastDay + "-" + thisMonth + "-" + thisYear);
        List<Report> reports = reportRepository.findAllByReportDateBetweenAndEmployees(startYear, endYear, employees);
        return reports;
    }

    public boolean existsById(String id) {
        return reportRepository.existsById(id);
    }

    public void insertEmptyReport(Report report) {
        reportRepository.insertReport(null, report.getReportDate(), null, null, 1, report.getEmployees().getId());
    }

    public Report insertReport(Report report, MultipartFile file) throws IOException {
        String dir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\reports\\"
                + report.getEmployees().getId() + "\\" + new SimpleDateFormat("MM-yy").format(report.getReportDate());
        File checkDir = new File(dir);
        if (!checkDir.exists()) {
            checkDir.mkdirs();
        }
        String documentDir = dir + "\\" + file.getOriginalFilename();
        File saveFile = new File(documentDir);
        saveFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(saveFile);
        fout.write(file.getBytes());
        fout.close();
        reportRepository.insertReport(report.getName(), report.getReportDate(), documentDir, new Date(), 2, report.getEmployees().getId());
        String date = new SimpleDateFormat("MM/yy").format(report.getReportDate());
        return reportRepository.findById("RPT/" + report.getEmployees().getId() + "/" + date).get();
    }

    public Report updateReport(Report report, MultipartFile file) throws IOException {
        String dir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\reports\\"
                + report.getEmployees().getId() + "\\" + new SimpleDateFormat("MM-yy").format(report.getReportDate());
        String documentDir = dir + "\\" + file.getOriginalFilename();
        if (report.getDocumentDir() != null) {
            File oldFile = new File(report.getDocumentDir());
            oldFile.delete();
        }
        File newFile = new File(documentDir);
        //save to db
        report.setDocumentDir(documentDir);
        reportRepository.save(report);
        //delete old file
        //create new file
        newFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(newFile);
        fout.write(file.getBytes());
        fout.close();
        return reportRepository.findById(report.getId()).get();
    }

    public String changeStatus(Report report, String status) {
        if (status.equals("accept")) {
            report.setReportStatus(reportStatusRepository.findById(3).get());
            reportRepository.save(report);
            return "accepted";
        }
        report.setReportStatus(reportStatusRepository.findById(4).get());
        reportRepository.save(report);
        return "rejected";
    }

    public void deleteReport(String id) {
        File file = new File(reportRepository.getOne(id).getDocumentDir());
        file.delete();
        reportRepository.deleteById(id);
    }

    public String urlToId(HttpServletRequest request) {
        final String path
                = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern
                = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        return new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);
    }
}

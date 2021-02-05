/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.NoteHistory;
import com.mii.TimesheetServer.entities.Report;
import com.mii.TimesheetServer.entities.data.NoteData;
import com.mii.TimesheetServer.repositories.NoteHistoryRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fahri
 */
@Service
public class NoteHistoryService {

    @Autowired
    private NoteHistoryRepository historyRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String email;

    public List<NoteHistory> getAll() {
        return historyRepository.findAll();
    }

    public List<NoteHistory> getByReport(Report report) {
        return historyRepository.findAllByReport(report);
    }

    public List<NoteHistory> getByReadAndEmployee(Boolean isRead, Integer id) {
        return historyRepository.findAllByIsReadAndEmployees(isRead, id);
    }

    public List<NoteHistory> getByEmployee(Integer id) {
        return historyRepository.findAllByEmployee(id);
    }

    public Optional<NoteHistory> getById(Integer id) {
        return historyRepository.findById(id);
    }

    public boolean existsById(Integer id) {
        return historyRepository.existsById(id);
    }

    public String sendNote(NoteHistory history) {
        Date dateNow = new Date();
        history.setCreatedDate(dateNow);
        history.setIsRead(false);
        historyRepository.save(history);
        return "sent";
    }

    public String readNote(Integer id) {
        if (historyRepository.existsById(id)) {
            NoteHistory note = historyRepository.findById(id).get();
            note.setIsRead(true);
            historyRepository.save(note);
            return "read";
        }
        return null;
    }

    public String deleteNote(Integer id) {
        if (historyRepository.existsById(id)) {
            historyRepository.deleteById(id);
            return "deleted";
        }
        return null;
    }

    public void sendNotesMail(NoteHistory note) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(email);
        helper.setTo(note.getReport().getEmployees().getEmail());
        if (note.getIsAccepted() == null) {
            helper.setSubject("Reminder Message");
            message.setText("<h1>Reminder</h1> "
                    + "<p>" + note.getNote() + "</p>", "UTF-8", "html");
        } else if (note.getIsAccepted() == true) {
            String reportDate = new SimpleDateFormat("MMM, yyyy").format(note.getReport().getReportDate());
            String name = note.getReport().getEmployees().getLastName();
            helper.setSubject("Report Accepted");
            message.setText("<h1><center>Report Accepted</center></h1> "
                    + "<p>Dear, " + name + " </p> "
                    + "<p>We inform that your report on " + reportDate + " has been accepted</p> "
                    + "<p>Please read the information below for the details</p> "
                    + "<br><br> "
                    + "<p>Report ID: " + note.getReport().getId() + "</p> "
                    + "<p>Report Name: " + note.getReport().getName() + "</p> "
                    + "<p>Report Date: " + reportDate + "</p> "
                    + "<p>Accepted by: " + note.getEmployees().getLastName() + "</p> "
                    + "<p>Message: " + note.getNote() + "</p> "
                    + "<br><br> "
                    + "<p>Thank you</p>", "UTF-8", "html");
        } else {
            String reportDate = new SimpleDateFormat("MMM, yyyy").format(note.getReport().getReportDate());
            String name = note.getReport().getEmployees().getLastName();
            helper.setSubject("Report Rejected");
            message.setText("<h1>Report Rejected</h1> "
                    + "<p>Dear, " + name + " </p> "
                    + "<p>We inform that your report on " + reportDate + " has been rejected</p> "
                    + "<p>Please read the information below for the details</p> "
                    + "<br><br><br> "
                    + "<p>Report ID: " + note.getReport().getId() + "</p> "
                    + "<p>Report Name: " + note.getReport().getName() + "</p> "
                    + "<p>Report Date: " + note.getReport().getReportDate() + "</p> "
                    + "<p>Rejected by: " + note.getEmployees().getLastName() + "</p> "
                    + "<p>Message: " + note.getNote() + "</p> "
                    + "<br><br><br> "
                    + "<p>Thank you</p>", "UTF-8", "html");
        }
        javaMailSender.send(message);
    }

    public NoteHistory constructNote(NoteData data) {
        NoteHistory note = new NoteHistory();
        note.setNote(data.getNote());
        note.setIsAccepted(data.getIsAccepted());
        note.setEmployees(data.getRelationManager());
        note.setReport(data.getReport());
        return note;
    }
}
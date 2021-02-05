/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.controllers;

import com.mii.TimesheetClient.models.Category;
import com.mii.TimesheetClient.models.Employees;
import com.mii.TimesheetClient.services.ReportService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mii.TimesheetClient.models.Report;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.mii.TimesheetClient.models.NoteHistory;
import com.mii.TimesheetClient.models.Task;
import com.mii.TimesheetClient.services.NoteHistoryService;
import com.mii.TimesheetClient.services.UserService;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import com.mii.TimesheetClient.models.User;
import com.mii.TimesheetClient.models.StatusAttendance;
import com.mii.TimesheetClient.models.data.ResponseMessage;
import com.mii.TimesheetClient.services.CategoryService;
import com.mii.TimesheetClient.services.StatusAttendanceService;
import com.mii.TimesheetClient.services.TaskService;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Fahri
 */
@Controller
@RequestMapping("/employee")
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RELATION_MANAGER')")
public class EmployeeController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private NoteHistoryService historyService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private StatusAttendanceService statusAttendanceService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('READ_EMPLOYEE')")
    public String employeeDashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getByUsername(username);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        model.addAttribute("employee", user.getEmployees());
        model.addAttribute("unread", historyService.getByRead(user.getId(), 0));
        model.addAttribute("tasks", taskService.getTaskByDateAndEmployee(user.getId(), today));
        model.addAttribute("report", reportService.getByMonthAndEmployee(user.getId(), today));
        return "employee/dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('READ_EMPLOYEE')")
    public String employeeProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        model.addAttribute("user", userService.getByUsername(username));
        return "profile";
    }

    @GetMapping("/report")
    @PreAuthorize("hasAuthority('READ_EMPLOYEE')")
    public String listReport(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getByUsername(username);
        Date today = new Date();
        String thisYear = new SimpleDateFormat("yyyy").format(today);
        model.addAttribute("report", reportService.getByDateAndEmployee(user.getEmployees().getId(), thisYear));
        return "employee/list-report";
    }

    @GetMapping("/report/{year}")
    @ResponseBody
    public List<Report> listReportByYear(@PathVariable String year) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            User user = userService.getByUsername(username);
            List<Report> reports = reportService.getByDateAndEmployee(user.getEmployees().getId(), year);
            return reports;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/report")
    @ResponseBody
    public String submitReport(@RequestParam("name") String name,
            @RequestParam("reportDate") String reportDate,
            @RequestParam("file") MultipartFile file) throws ParseException, IOException {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            Report report = new Report();
            User user = userService.getByUsername(username);
            report.setName(name);
            report.setReportDate(reportDate);
            report.setEmployees(user.getEmployees());
            reportService.submitReport(report, file);
            return "created";
        } catch (IOException e) {
            return null;
        }
    }

    @PostMapping("/report/**/update")
    @ResponseBody
    public String updateReport(HttpServletRequest request,
            @RequestParam("name") String name,
            @RequestParam("reportDate") String reportDate,
            @RequestParam("file") MultipartFile file) {
        try {
            String id = reportService.urlToId(request).replace("/update", "");
            System.out.println(id);
            Report report = new Report();
            report.setId(id);
            report.setName(name);
            report.setReportDate(reportDate);
            reportService.updateReport(report, file);
            return "done";
        } catch (IOException e) {
            return null;
        }
    }

    @PostMapping("/report/**/delete")
    @ResponseBody
    public String deleteReport(HttpServletRequest request) {
        try {
            String id = reportService.urlToId(request).replace("/delete", "");
            System.out.println(id);
            String response = reportService.deleteReport(id);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/report/download/**")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadReport(HttpServletRequest request) {
        try {
            String id = reportService.urlToId(request);
            ByteArrayResource data = reportService.downloadReport(id);
            String fileName = id.replace("/", "_") + ".pdf";
            InputStreamResource i = new InputStreamResource(data.getInputStream());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"" + fileName + "\""));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(data.contentLength())
                    .headers(headers)
                    .body(i);
        } catch (IOException e) {
            System.out.println(e);
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/task")
    @PreAuthorize("hasAuthority('READ_EMPLOYEE')")
    public String listTask(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getByUsername(username);
        Date now = new Date();
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(now);
        List<Task> tasks = taskService.getTaskByDateAndEmployee(user.getEmployees().getId(), strDate);
        List<StatusAttendance> statusAttendance = statusAttendanceService.getAllStatusAttendance();
        List<Category> category = categoryService.getAllCategory();
        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("statusAttendances", statusAttendance);
        model.addAttribute("categories", category);
        return "employee/list-task";
    }

    @GetMapping("/task/{date}/{empId}")
    @ResponseBody
    public List<Task> reloadTaskTable(@PathVariable("date") String date, @PathVariable("empId") Integer empId) {
        System.out.println(empId);
        List<Task> tasks = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getByUsername(username);
        if (empId != 0) {
            tasks = taskService.getTaskByDateAndEmployee(empId, date);
        } else {
            tasks = taskService.getTaskByDateAndEmployee(user.getEmployees().getId(), date);
        }
        return tasks;
    }

    @PostMapping("/task")
    @ResponseBody
    public ResponseMessage inputTask(@RequestBody Task task) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            User user = userService.getByUsername(username);
            Employees e = new Employees(user.getEmployees().getId());
            String[] dateString = task.getDate().split("-");
            task.setDate(dateString[2] + "-" + dateString[1] + "-" + dateString[0]);
            task.setEmployees(e);
            ResponseMessage r = taskService.createTask(task);
            return r;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @PostMapping("/task/{id}/update")
    @ResponseBody
    public ResponseMessage updateTask(@RequestBody Task task, @PathVariable String id) {
        try {
            String[] dateString = task.getDate().split("-");
            task.setDate(dateString[2] + "-" + dateString[1] + "-" + dateString[0]);
            ResponseMessage r = taskService.updateTask(id, task);
            return r;
        } catch (Exception e) {
            return new ResponseMessage("failed", e);
        }
    }

    @PostMapping("/task/{id}/delete")
//    @PreAuthorize("hasAuthority('DELETE_EMPLOYEE')")
    public String deleteTask(@PathVariable String id) {
        try {
            String response = taskService.deleteTask(id);
            return "redirect:/employee/task";
        } catch (Exception e) {
            return "redirect:/employee/task?error";
        }
    }

    @PostMapping("/task/generate")
    @ResponseBody
    public ResponseEntity generateReport(@RequestBody Task task) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            User user = userService.getByUsername(username);
            Employees e = new Employees(user.getEmployees().getId());
            String date = task.getDate().substring(3);
            String fileName = "Report-" + e.getId() + "-" + date + ".xlsx";
            if (task.getEmployees() == null) {
                task.setEmployees(e);
            }
            ByteArrayResource data = taskService.generateReport(task);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"")
                    .body(data);
        } catch (IOException e) {
            System.out.println("gagal");
            System.out.println(e);
            return null;
        }
    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('READ_EMPLOYEE')")
    public String listHistory(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getByUsername(username);
        List<NoteHistory> notes = historyService.getByEmployee(user.getId());
        List<Report> reports = reportService.getByEmployee(user.getId());
        model.addAttribute("notes", notes);
        model.addAttribute("reports", reports);

        return "employee/list-history";
    }

    @GetMapping("/history/ajax")
    @ResponseBody
    public List<NoteHistory> ajaxGetAll() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            User user = userService.getByUsername(username);
            List<NoteHistory> notes = historyService.getByEmployee(user.getId());
            return notes;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/history/report/**")
    @ResponseBody
    public List<NoteHistory> ajaxGetByReport(HttpServletRequest request) {
        try {
            String id = reportService.urlToId(request);
            List<NoteHistory> notes = historyService.getByReport(id);
            return notes;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/history/read/{id}")
    @ResponseBody
    public String ajaxReadHistory(@PathVariable Integer id) {
        try {
            String response = historyService.readNote(id);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

}

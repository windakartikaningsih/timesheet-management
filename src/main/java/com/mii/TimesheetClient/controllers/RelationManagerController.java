/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.controllers;

import com.mii.TimesheetClient.models.Employees;
import com.mii.TimesheetClient.models.Task;
import com.mii.TimesheetClient.models.StatusAttendance;
import com.mii.TimesheetClient.models.Category;
import com.mii.TimesheetClient.models.ManagerSite;
import com.mii.TimesheetClient.models.NoteHistory;
import com.mii.TimesheetClient.models.data.NoteData;
import com.mii.TimesheetClient.models.Report;
import com.mii.TimesheetClient.models.User;
import com.mii.TimesheetClient.models.Site;
import com.mii.TimesheetClient.services.CategoryService;
import com.mii.TimesheetClient.services.EmployeeService;
import com.mii.TimesheetClient.services.NoteHistoryService;
import com.mii.TimesheetClient.services.ReportService;
import com.mii.TimesheetClient.services.SiteService;
import com.mii.TimesheetClient.services.StatusAttendanceService;
import com.mii.TimesheetClient.services.TaskService;
import com.mii.TimesheetClient.services.UserService;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author windak
 */
@Controller
@RequestMapping("/relation-manager")
@PreAuthorize("hasRole('RELATION_MANAGER')")
public class RelationManagerController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserService userService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private StatusAttendanceService saSercive;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private NoteHistoryService historyService;

    @GetMapping("")
    public String rmDashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getByUsername(username);
        List<Site> sites = siteService.getAllSiteByRM(user.getId());
        List<Employees> employees = new ArrayList<>();
        Set<Integer> manager = new HashSet<>();
        sites.forEach((site) -> {
            for (Employees employee : employeeService.getEmployeeBySite(site.getId())) {
                employees.add(employee);
                manager.add(employee.getDivision().getManagerSite().getId());
            }
        });
        model.addAttribute("employee", employees);
        model.addAttribute("manager", manager);
        model.addAttribute("site", sites);
        return "relationmanager/dashboard";
    }

    @GetMapping("/profile")
    public String rmProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        model.addAttribute("user", userService.getByUsername(username));
        return "profile";
    }

    @GetMapping("/site")
    public String getSiteByRM(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getByUsername(username);
        List<Site> site = siteService.getAllSiteByRM(user.getEmployees().getId());
        model.addAttribute("sites", site);
        return "relationmanager/list-site";
    }

    @GetMapping("/{siteId}/employee")
    public String getEmployeeBySite(@PathVariable String siteId, Model model) {
        model.addAttribute("site", siteService.getSiteById(siteId));
        model.addAttribute("employees", employeeService.getEmployeeBySite(siteId));
        return "relationmanager/list-employee";
    }

    @GetMapping("/{siteId}/{empId}/report")
    public String listReport(@PathVariable Integer empId, Model model) {
        Employees employees = employeeService.getEmployeeById(empId);
        Date today = new Date();
        String todayString = new SimpleDateFormat("yyyy-MM-dd").format(today);
        String thisYear = new SimpleDateFormat("yyyy").format(today);
        model.addAttribute("thisMonthReport", reportService.getByMonthAndEmployee(empId, todayString));
        model.addAttribute("employee", employees);
        model.addAttribute("report", reportService.getByDateAndEmployee(employees.getId(), thisYear));
        return "relationmanager/list-report";
    }

    @GetMapping("/report/{year}/{id}")
    @ResponseBody
    public List<Report> listReportByYear(@PathVariable("year") String year, @PathVariable("id") Integer empId) {
        try {
            List<Report> reports = reportService.getByDateAndEmployee(empId, year);
            return reports;
        } catch (Exception e) {
            return null;
        }
    }

//    @GetMapping("/{siteId}/{empId}/{reportId}/task")
    @GetMapping("/{siteId}/{empId}/task")
    public String listTask(@PathVariable Integer empId, Model model) {
        Employees employees = employeeService.getEmployeeById(empId);
        Date now = new Date();
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(now);
        model.addAttribute("employee", employees);
        model.addAttribute("tasks", taskService.getTaskByDateAndEmployee(employees.getId(), strDate));
        model.addAttribute("statusAttendances", saSercive.getAllStatusAttendance());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "relationmanager/list-task";
    }

    @PostMapping("/note-history/send")
    @ResponseBody
    public String sendNote(@RequestBody NoteHistory note) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            User user = userService.getByUsername(username);
            NoteData body = new NoteData();
            body.setNote(note.getNote());
            body.setRelationManager(user.getEmployees());
            if (note.getReport() != null) {
                body.setIsAccepted(note.getIsAccepted());
                body.setReport(note.getReport());
            } else {
                body.setEmployee(note.getEmployees());
                body.setReportDate(new Date());
            }
            System.out.println(body);
            String response = historyService.sendNote(body);
            return response;
        } catch (Exception e) {
            return null;
        }
    }
}

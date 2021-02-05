/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.controllers;

import com.mii.TimesheetClient.models.Employees;
import com.mii.TimesheetClient.models.UserPermission;
import com.mii.TimesheetClient.models.UserRole;
import com.mii.TimesheetClient.services.DivisionService;
import com.mii.TimesheetClient.services.EmployeeService;
import com.mii.TimesheetClient.services.ManagerSiteService;
import com.mii.TimesheetClient.services.PermissionService;
import com.mii.TimesheetClient.services.RoleService;
import com.mii.TimesheetClient.services.SiteService;
import com.mii.TimesheetClient.services.UserPermissionService;
import com.mii.TimesheetClient.services.UserRoleService;
import com.mii.TimesheetClient.services.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mii.TimesheetClient.models.data.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author windak
 */
@Controller
@RequestMapping("/super-admin")
@PreAuthorize("hasRole('ADMIN')")
public class SuperAdminController {

    @Autowired
    private DivisionService divisionService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ManagerSiteService msService;
    @Autowired
    private PermissionService pService;
    @Autowired
    private RoleService rService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private UserPermissionService uperService;
    @Autowired
    private UserRoleService uroleService;
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String dashboard(Model model) {
        model.addAttribute("employee", employeeService.getAllEmployee());
        model.addAttribute("site", siteService.getAllSite());
        model.addAttribute("manager", msService.getAllManagerSite());
        model.addAttribute("division", divisionService.getAllDivision());
        model.addAttribute("role", rService.getAllRoles());
        model.addAttribute("permission", pService.getAllPermissions());
        model.addAttribute("userRole", uroleService.getAllUserRole());
        model.addAttribute("userPermission", uperService.getAllUserPermission());
        return "superadmin/dashboard";
    }

    // ============================== Division ============================== //
    @GetMapping("/division")
    public String listDivision(Model model) {
        model.addAttribute("division", divisionService.getAllDivision());
        model.addAttribute("manager", msService.getAllManagerSite());
        return "superadmin/list-division";
    }
    // ============================== End Manager Site ============================== //

    // ============================== Employee ============================== //
    @GetMapping("/profile")
    public String profileSuperAdmin(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        model.addAttribute("user", userService.getByUsername(username));
        return "profile";
    }

    @GetMapping("/employee")
    public String getAllEmployee(Model model) {
        System.out.println(divisionService.getAllDivision());
        model.addAttribute("division", divisionService.getAllDivision());
        model.addAttribute("employees", employeeService.getAllEmployee());
        return "superadmin/list-employees";
    }
    // ============================== End Employee ============================== //

    // ============================== Manager Site ============================== //
    @GetMapping("/manager-site")
    public String getAllManagerSite(Model model) {
        model.addAttribute("site", siteService.getAllSite());
        model.addAttribute("manager", msService.getAllManagerSite());
        return "superadmin/list-manager-site";
    }
    // ============================== End Manager Site ============================== //

    // ============================== Permission ============================== //
    @GetMapping("/permission")
    public String getAllPermission(Model model) {
        model.addAttribute("permissions", pService.getAllPermissions());
        return "superadmin/list-permissions";
    }
    // ============================== End Permission ============================== //

    // ============================== Role ============================== //
    @GetMapping("/role")
    public String getAllRole(Model model) {
        model.addAttribute("roles", rService.getAllRoles());
        return "superadmin/list-roles";
    }
    // ============================== End Role ============================== //

    // ============================== Site ============================== //
    @GetMapping("/site")
    public String getAllSite(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployee());
        model.addAttribute("sites", siteService.getAllSite());
        return "superadmin/list-site";
    }
    // ============================== End Site ============================== //

    // ============================== User ============================== //
    @GetMapping("/user")
    public String getAllUser(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployee());
        return "superadmin/list-user";
    }

    // ============================== End User ============================== //
    
    // ============================== User Permission ============================== //
    @GetMapping("/user-permission")
    public String getAllUserPermission(Model model) {
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("permission", pService.getAllPermissions());
        model.addAttribute("userpermit", uperService.getAllUserPermission());
        return "superadmin/list-user-permission";
    }
    // ============================== End User Permission ============================== //

    // ============================== User Role ============================== //
    @GetMapping("/user-role")
    public String getAllUserRole(Model model) {
        model.addAttribute("roles", rService.getAllRoles());
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("userrole", uroleService.getAllUserRole());
        return "superadmin/list-user-role";
    }
    // ============================== End User Role ============================== //

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.controllers;

import com.mii.TimesheetClient.models.Employees;
import com.mii.TimesheetClient.models.Division;
import com.mii.TimesheetClient.models.ManagerSite;
import com.mii.TimesheetClient.models.Role;
import com.mii.TimesheetClient.models.Site;
import com.mii.TimesheetClient.models.User;
import com.mii.TimesheetClient.models.UserPermission;
import com.mii.TimesheetClient.models.UserRole;
import com.mii.TimesheetClient.models.data.ResponseMessage;
import com.mii.TimesheetClient.services.AuthenticationService;
import com.mii.TimesheetClient.services.DivisionService;
import com.mii.TimesheetClient.services.EmployeeService;
import com.mii.TimesheetClient.services.ManagerSiteService;
import com.mii.TimesheetClient.services.PermissionService;
import com.mii.TimesheetClient.services.RoleService;
import com.mii.TimesheetClient.services.SiteService;
import com.mii.TimesheetClient.services.UserPermissionService;
import com.mii.TimesheetClient.services.UserRoleService;
import com.mii.TimesheetClient.services.UserService;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author windak
 */
@Controller
@RequestMapping("/ajax/super-admin")
public class AjaxSuperAdminController {

    @Autowired
    private AuthenticationService authService;

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
    private UserService userService;
    @Autowired
    private UserPermissionService uperService;
    @Autowired
    private UserRoleService uroleService;

    // ============================== Division ============================== //
    @GetMapping("/division")
    public @ResponseBody
    List<Division> getAllDivision() {
        return divisionService.getAllDivision();
    }

    @GetMapping("/division/{id}")
    public @ResponseBody
    Division getDivisionById(@PathVariable("id") String id) {
        return divisionService.getDivisionById(id);
    }

    @PostMapping("/division/create")
    @ResponseBody
    public ResponseMessage createDivision(@RequestBody Division division) {
        try {
            ResponseMessage dv = divisionService.createDivision(division);
            return dv;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Create", e);
        }
    }

    @PostMapping("/division/{id}/update")
    @ResponseBody
    public ResponseMessage updateDivision(@RequestBody Division division, @PathVariable String id) {
        try {
            ResponseMessage dv = divisionService.updateDivision(id, division);
            return dv;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Create", e);
        }
    }

    @PostMapping("/division/{id}/delete")
    public String deleteDivision(@PathVariable String id) {
        try {
            String response = divisionService.deleteDivision(id);
            return "Deleted";
        } catch (Exception e) {
            return "Failed";
        }
    }
    // ============================== End Division ============================== //
    
    // ============================== Employee ============================== //
    @GetMapping("/employee")
    public @ResponseBody
    List<Employees> getAllEmployee() {
        return employeeService.getAllEmployee();
    }

    @GetMapping("/employee/{id}")
    public @ResponseBody
    Employees getEmployeeById(@PathVariable("id") Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping("/employee/create")
    @ResponseBody
    public ResponseMessage createEmployee(@RequestBody Employees employees) {
        try {
            if ("".equals(employees.getDivision().getId())) {
                employees.setDivision(null);
            }
            ResponseMessage emp = employeeService.createEmployee(employees);
            return emp;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                return new ResponseMessage("email already exist", e);
            }
            return new ResponseMessage("Failed To Create", e);
        }
    }

    @PostMapping("/employee/{id}/update")
    @ResponseBody
    public ResponseMessage updateEmployee(@RequestBody Employees employees, @PathVariable Integer id) {
        try {
            if ("".equals(employees.getDivision().getId())) {
                employees.setDivision(null);
            }
            ResponseMessage emp = employeeService.updateEmployee(id, employees);
            return emp;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Create", e);
        }
    }

    @PostMapping("/employee/{id}/delete")
    @ResponseBody
    public String deleteEmployee(@PathVariable Integer id) {
        try {
            String response = employeeService.deleteEmployee(id);
            return "done";
        } catch (Exception e) {
            return "failed";
        }
    }
    // ============================== End Employee ============================== //

    // ============================== Manager Site ============================== //
    @GetMapping("/manager-site")
    public @ResponseBody
    List<ManagerSite> getAllManagerSite() {
        return msService.getAllManagerSite();
    }

    @GetMapping("/manager-site/{id}")
    public @ResponseBody
    ManagerSite getManagerSiteById(@PathVariable("id") Integer id) {
        return msService.getManagerSiteById(id);
    }

    @PostMapping("/manager-site/create")
    @ResponseBody
    public ResponseMessage createManagerSite(@RequestBody ManagerSite manager) {
        try {
            ResponseMessage ms = msService.createManagerSite(manager);
            return ms;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Create", e);
        }
    }

    @PostMapping("/manager-site/{id}/update")
    @ResponseBody
    public ResponseMessage updateManagerSite(@RequestBody ManagerSite manager, @PathVariable Integer id) {
        try {
            ResponseMessage ms = msService.updateManagerSite(id, manager);
            return ms;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Update", e);
        }
    }

    @PostMapping("/manager-site/{id}/delete")
    @ResponseBody
    public String deleteManagerSite(@PathVariable Integer id) {
        try {
            String response = msService.deleteManagerSite(id);
            return "Done";
        } catch (Exception e) {
            return "Failed";
        }
    }
    // ============================== End Manager Site ============================== //

    // ============================== Role ============================== //
    @GetMapping("/role")
    public @ResponseBody
    List<Role> getAllRole() {
        return rService.getAllRoles();
    }

    @GetMapping("/role/{id}")
    public @ResponseBody
    Role getRoleById(@PathVariable("id") Integer id) {
        return rService.getRoleById(id);
    }

    @PostMapping("/role/create")
    public @ResponseBody
    Role createRole(Role role) {
        return rService.createRole(role);
    }

    @PutMapping("/role/{id}/update")
    public @ResponseBody
    Role updateRole(@PathVariable("id") Integer id, Role role) {
        return rService.updateRole(role);
    }

    @GetMapping("/role/{id}/delete")
    public @ResponseBody
    Role deleteRole(@PathVariable("id") Integer id) {
        return rService.deleteRole(id);
    }
    // ============================== End Role ============================== //

    // ============================== Site ============================== //
    @GetMapping("/site")
    public @ResponseBody
    List<Site> getAllSite() {
        return siteService.getAllSite();
    }

    @GetMapping("/site/{id}")
    public @ResponseBody
    Site getSiteById(@PathVariable("id") String id) {
        return siteService.getSiteById(id);
    }

    @PostMapping("/site/create")
    @ResponseBody
    public ResponseMessage createSite(@RequestBody Site site) {
        try {
            ResponseMessage s = siteService.createSite(site);
            return s;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Create", e);
        }
    }

    @PostMapping("/site/{id}/update")
    @ResponseBody
    public ResponseMessage updateSite(@RequestBody Site site, @PathVariable String id) {
        try {
            ResponseMessage s = siteService.updateSite(id, site);
            return s;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Create", e);
        }
    }

    @PostMapping("/site/{id}/delete")
    @ResponseBody
    public String deleteSite(@PathVariable String id) {
        try {
            String response = siteService.deleteSite(id);
            return "Done";
        } catch (Exception e) {
            return "Failed";
        }
    }

    // ============================== End Site ============================== //
    
    // ============================== User ============================== //
    @GetMapping("/user")
    @ResponseBody
    public List<User> getAllUser() {
        try {
            List<User> users = userService.getAllUser();
            return users;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/user")
    @ResponseBody
    public ResponseMessage createUser(@RequestBody User user) {
        try {
            ResponseMessage response = userService.createUser(user);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/user/{id}/delete")
    @ResponseBody
    public ResponseMessage deleteUser(@PathVariable Integer id) {
        try {
            ResponseMessage response = userService.deleteUser(id);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/user/{id}/reset-verif")
    @ResponseBody
    public ResponseMessage resetVerifUser(@PathVariable Integer id) {
        try {
            ResponseMessage response = userService.resetVerif(id);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    // ============================== User Role ============================== //
    @GetMapping("/user-role")
    public @ResponseBody
    List<UserRole> getAllUserRole() {
        return uroleService.getAllUserRole();
    }

    @GetMapping("/user-role/{id}")
    public @ResponseBody
    UserRole getUserRoleById(@PathVariable("id") Integer id) {
        return uroleService.getUserRoleById(id);
    }

    @PostMapping("/user-role/create")
    @ResponseBody
    public ResponseMessage createUserRole(@RequestBody UserRole userrole) {
        try {
            ResponseMessage urole = uroleService.createUserRole(userrole);
            return urole;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Create", e);
        }
    }

    @PostMapping("/user-role/{id}/update")
    @ResponseBody
    public ResponseMessage updateUserRole(@RequestBody UserRole userrole, @PathVariable Integer id) {
        try {
            ResponseMessage urole = uroleService.updateUserRole(id, userrole);
            return urole;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Update", e);
        }
    }

    @PostMapping("/user-role/{id}/delete")
    @ResponseBody
    public String deleteUserRole(@PathVariable Integer id) {
        try {
            String response = uroleService.deleteUserRole(id);
            return "Done";
        } catch (Exception e) {
            return "Failed";
        }
    }

    // ============================== End User Role ============================== //
    
    // ============================== User Permission ============================== //
    @GetMapping("/user-permission")
    public @ResponseBody
    List<UserPermission> getAllUserPermission() {
        return uperService.getAllUserPermission();
    }

    @GetMapping("/user-permission/{id}")
    public @ResponseBody
    UserPermission getUserPermissionById(@PathVariable("id") Integer id) {
        return uperService.getUserPermissionById(id);
    }

    @PostMapping("/user-permission/create")
    @ResponseBody
    public ResponseMessage createUserPermission(@RequestBody UserPermission userpermit) {
        try {
            ResponseMessage upermit = uperService.createUserPermission(userpermit);
            return upermit;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Create", e);
        }
    }

    @PostMapping("/user-permission/{id}/update")
    @ResponseBody
    public ResponseMessage updateUserPermission(@RequestBody UserPermission userpermit, @PathVariable Integer id) {
        try {
            ResponseMessage upermit = uperService.updateUserPermission(id, userpermit);
            return upermit;
        } catch (Exception e) {
            return new ResponseMessage("Failed To Update", e);
        }
    }

    @PostMapping("/user-permission/{id}/delete")
    @ResponseBody
    public String deleteUserPermission(@PathVariable Integer id) {
        try {
            String response = uperService.deleteUserPermission(id);
            return "Done";
        } catch (Exception e) {
            return "Failed";
        }
    }
    // ============================== End User Permission ============================== //
}

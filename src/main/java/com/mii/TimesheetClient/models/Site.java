/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models;

import java.util.List;
import lombok.Data;

/**
 *
 * @author windak
 */
@Data
public class Site {
    private String id;
    private String siteName;
    private String address;
    private String city;
    private Employees employees;
    private List<ManagerSite> managerSiteList;

    public Site(String id, String siteName, String address, String city, Employees employees, List<ManagerSite> managerSiteList) {
        this.id = id;
        this.siteName = siteName;
        this.address = address;
        this.city = city;
        this.employees = employees;
        this.managerSiteList = managerSiteList;
    }

    public Site() {
    }

    public Site(String id) {
        this.id = id;
    }
    
}

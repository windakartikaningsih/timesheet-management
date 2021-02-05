/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Fahri
 */
@Entity
@Table(name = "site", catalog = "timesheet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Site.findAll", query = "SELECT s FROM Site s")
    , @NamedQuery(name = "Site.findById", query = "SELECT s FROM Site s WHERE s.id = :id")
    , @NamedQuery(name = "Site.findBySiteName", query = "SELECT s FROM Site s WHERE s.siteName = :siteName")
    , @NamedQuery(name = "Site.findByAddress", query = "SELECT s FROM Site s WHERE s.address = :address")
    , @NamedQuery(name = "Site.findByCity", query = "SELECT s FROM Site s WHERE s.city = :city")})
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false, length = 10)
    private String id;
    @Column(name = "site_name", length = 150)
    private String siteName;
    @Column(name = "address", length = 250)
    private String address;
    @Column(name = "city", length = 50)
    private String city;
    @JoinColumn(name = "relation_manager_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Employees employees;
    @OneToMany(mappedBy = "site", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ManagerSite> managerSiteList;

    public Site() {
    }

    public Site(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }

    @XmlTransient
    public List<ManagerSite> getManagerSiteList() {
        return managerSiteList;
    }

    public void setManagerSiteList(List<ManagerSite> managerSiteList) {
        this.managerSiteList = managerSiteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Site)) {
            return false;
        }
        Site other = (Site) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mii.TimesheetServer.entities.Site[ id=" + id + " ]";
    }
    
}

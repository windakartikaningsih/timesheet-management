/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Fahri
 */
@Entity
@Table(name = "manager_site", catalog = "timesheet", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ManagerSite.findAll", query = "SELECT m FROM ManagerSite m")
    , @NamedQuery(name = "ManagerSite.findById", query = "SELECT m FROM ManagerSite m WHERE m.id = :id")
    , @NamedQuery(name = "ManagerSite.findByFirstName", query = "SELECT m FROM ManagerSite m WHERE m.firstName = :firstName")
    , @NamedQuery(name = "ManagerSite.findByLastName", query = "SELECT m FROM ManagerSite m WHERE m.lastName = :lastName")
    , @NamedQuery(name = "ManagerSite.findByEmail", query = "SELECT m FROM ManagerSite m WHERE m.email = :email")})
public class ManagerSite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "first_name", length = 100)
    private String firstName;
    @Column(name = "last_name", length = 100)
    private String lastName;
    @Column(name = "email", length = 150)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(mappedBy = "managerSite", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Division division;
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Site site;

    public ManagerSite() {
    }

    public ManagerSite(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
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
        if (!(object instanceof ManagerSite)) {
            return false;
        }
        ManagerSite other = (ManagerSite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mii.TimesheetServer.entities.ManagerSite[ id=" + id + " ]";
    }
    
}

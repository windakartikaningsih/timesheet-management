/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "status_attendance", catalog = "timesheet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StatusAttendance.findAll", query = "SELECT s FROM StatusAttendance s")
    , @NamedQuery(name = "StatusAttendance.findById", query = "SELECT s FROM StatusAttendance s WHERE s.id = :id")
    , @NamedQuery(name = "StatusAttendance.findByStatusAttendanceName", query = "SELECT s FROM StatusAttendance s WHERE s.statusAttendanceName = :statusAttendanceName")})
public class StatusAttendance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "status_attendance_name", length = 50)
    private String statusAttendanceName;
    @OneToMany(mappedBy = "statusAttendance", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Task> taskList;

    public StatusAttendance() {
    }

    public StatusAttendance(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusAttendanceName() {
        return statusAttendanceName;
    }

    public void setStatusAttendanceName(String statusAttendanceName) {
        this.statusAttendanceName = statusAttendanceName;
    }

    @XmlTransient
    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
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
        if (!(object instanceof StatusAttendance)) {
            return false;
        }
        StatusAttendance other = (StatusAttendance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mii.TimesheetServer.entities.StatusAttendance[ id=" + id + " ]";
    }
    
}

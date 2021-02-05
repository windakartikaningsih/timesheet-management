/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Fahri
 */
@Entity
@Table(name = "task", catalog = "timesheet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t")
    , @NamedQuery(name = "Task.findById", query = "SELECT t FROM Task t WHERE t.id = :id")
    , @NamedQuery(name = "Task.findByProjectName", query = "SELECT t FROM Task t WHERE t.projectName = :projectName")
    , @NamedQuery(name = "Task.findByDate", query = "SELECT t FROM Task t WHERE t.date = :date")
    , @NamedQuery(name = "Task.findByStartHour", query = "SELECT t FROM Task t WHERE t.startHour = :startHour")
    , @NamedQuery(name = "Task.findByEndHour", query = "SELECT t FROM Task t WHERE t.endHour = :endHour")
    , @NamedQuery(name = "Task.findByActivity", query = "SELECT t FROM Task t WHERE t.activity = :activity")})
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", length = 20)
    private String id;
    @Column(name = "project_name", length = 100)
    private String projectName;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Bangkok")
    private Date date;
    @Column(name = "start_hour")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Date startHour;
    @Column(name = "end_hour")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Date endHour;
    @Column(name = "activity", length = 500)
    private String activity;
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
    @JoinColumn(name = "status_attendance_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private StatusAttendance statusAttendance;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employees employees;

    public Task() {
    }

    public Task(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartHour() {
        return startHour;
    }

    public void setStartHour(Date startHour) {
        this.startHour = startHour;
    }

    public Date getEndHour() {
        return endHour;
    }

    public void setEndHour(Date endHour) {
        this.endHour = endHour;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public StatusAttendance getStatusAttendance() {
        return statusAttendance;
    }

    public void setStatusAttendance(StatusAttendance statusAttendance) {
        this.statusAttendance = statusAttendance;
    }

    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
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
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mii.TimesheetServer.entities.Task[ id=" + id + " ]";
    }

}

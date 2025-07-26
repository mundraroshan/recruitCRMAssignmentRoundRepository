package com.crm.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "employee_project")
public class EmployeeProject {

    @EmbeddedId
    private EmployeeProjectId id;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    @NotNull
    private Employee employee;

    @ManyToOne
    @NotNull
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "assigned_date")
    @NotNull
    private Date assignedDate;

    @Column(name = "role", length = 50)
    private String role;

    public EmployeeProject() {
    }

    public EmployeeProject(EmployeeProjectId id, Employee employee, Project project,
            Date assignedDate, String role) {
        this.id = id;
        this.employee = employee;
        this.project = project;
        this.assignedDate = assignedDate;
        this.role = role;
    }

    public EmployeeProjectId getId() {
        return id;
    }

    public void setId(EmployeeProjectId id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "EmployeeProject [id=" + id.toString() + ", employee=" + employee + ", project=" + project + ", assignedDate="
                + assignedDate + ", role=" + role + "]";
    }

    

}
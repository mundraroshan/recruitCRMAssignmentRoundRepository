package com.crm.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class EmployeeProjectId implements Serializable {
    
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "project_id")
    private Integer projectId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public EmployeeProjectId() {
    }

    public EmployeeProjectId(Long employeeId, Integer projectId) {
        this.employeeId = employeeId;
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "EmployeeProjectId [employeeId=" + employeeId + ", projectId=" + projectId + "]";
    }

    

}

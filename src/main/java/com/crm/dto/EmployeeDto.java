package com.crm.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long id;
    private String name;
    private String email;
    private String dateOfJoining;
    private Double salary;
    private String managerName;
    private String departmentName;
    private List<ReviewDto> performanceReviews;
    private List<ProjectDto> projects;
   
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDateOfJoining() {
        return dateOfJoining;
    }
    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }
    public Double getSalary() {
        return salary;
    }
    public void setSalary(Double salary) {
        this.salary = salary;
    }
    public String getManagerName() {
        return managerName;
    }
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public List<ReviewDto> getPerformanceReviews() {
        return performanceReviews;
    }
    public void setPerformanceReviews(List<ReviewDto> performanceReviews) {
        this.performanceReviews = performanceReviews;
    }
    public List<ProjectDto> getProjects() {
        return projects;
    }
    public void setProjects(List<ProjectDto> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "EmployeeDto [id=" + id + ", name=" + name + ", email=" + email + ", dateOfJoining=" + dateOfJoining
                + ", salary=" + salary + ", managerName=" + managerName + ", departmentName=" + departmentName
                + ", performanceReviews=" + performanceReviews + ", projects=" + projects + "]";
    }

}

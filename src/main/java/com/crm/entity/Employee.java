package com.crm.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Email(message = "Invalid email format")
    @Column(name = "email", unique = true)
    @NotNull
    private String email;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Department.class)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    @NotNull
    private Department department;

    @Column(name = "date_of_joining")
    @NotNull
    private Date dateOfJoining;

    @Column(name = "salary")
    @NotNull
    private Double salary;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Employee.class)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private List<Employee> reportees = new ArrayList<Employee>();

    @OneToMany(mappedBy = "employee")
    @OrderBy("reviewDate DESC")
    private Set<PerformanceReview> performanceReviews;

    @OneToMany(mappedBy = "employee")
    private Set<EmployeeProject> employeeProjects;

    @ManyToMany
    @JoinTable(name = "employee_project", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects;

    public Employee() {
    }

    public Employee(Long id, String name, @Email(message = "Invalid email format") @NotNull String email,
            Department department, Date dateOfJoining, Double salary,
            Employee manager) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.dateOfJoining = dateOfJoining;
        this.salary = salary;
        this.manager = manager;
    }

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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Date getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Date dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public List<Employee> getReportees() {
        return reportees;
    }

    public void setReportees(List<Employee> reportees) {
        this.reportees = reportees;
    }

    public Set<PerformanceReview> getPerformanceReviews() {
        return performanceReviews;
    }

    public void setPerformanceReviews(Set<PerformanceReview> performanceReviews) {
        this.performanceReviews = performanceReviews;
    }

    public Set<EmployeeProject> getEmployeeProjects() {
        return employeeProjects;
    }

    public void setEmployeeProjects(Set<EmployeeProject> employeeProjects) {
        this.employeeProjects = employeeProjects;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", email=" + email + ", department=" + department
                + ", dateOfJoining=" + dateOfJoining + ", salary=" + salary + ", manager=" + manager + ", reportees="
                + reportees + ", performanceReviews=" + performanceReviews + ", employeeProjects=" + employeeProjects
                + ", projects=" + projects + "]";
    }

}
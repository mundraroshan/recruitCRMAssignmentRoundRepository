package com.crm.entity;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "performance_review")
public class PerformanceReview implements Comparable<PerformanceReview> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @NotNull
    private Employee employee;

    @Column(name = "review_date")
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date reviewDate;

    @Column(name = "score", precision = 2, scale = 2)
    @DecimalMin(value = "0.00", message = "Score must be at least 0.00")
    @DecimalMax(value = "10.00", message = "Score must be at most 10.00")
    @NotNull
    private BigDecimal score;

    @Column(name = "review_comments", length = 200)
    private String reviewComments;

    public PerformanceReview() {
    }

    public PerformanceReview(Long id, @NotNull Employee employee, @NotNull Date reviewDate,
            @DecimalMin(value = "0.00", message = "Score must be at least 0.00") @DecimalMax(value = "10.00", message = "Score must be at most 10.00") @NotNull BigDecimal score,
            String reviewComments) {
        this.id = id;
        this.employee = employee;
        this.reviewDate = reviewDate;
        this.score = score;
        this.reviewComments = reviewComments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }

    @Override
    public String toString() {
        return "PerformanceReview [id=" + id + ", employee=" + employee + ", reviewDate=" + reviewDate + ", score="
                + score + ", reviewComments=" + reviewComments + "]";
    }

    @Override
    public int compareTo(PerformanceReview o) {
        return o.getReviewDate().compareTo(this.getReviewDate()); 
    }

}
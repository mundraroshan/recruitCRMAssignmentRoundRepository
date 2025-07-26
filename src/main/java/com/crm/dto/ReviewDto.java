package com.crm.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private String reviewDate;
    private BigDecimal score;
    private String comments;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getReviewDate() {
        return reviewDate;
    }
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
    public BigDecimal getScore() {
        return score;
    }
    public void setScore(BigDecimal score) {
        this.score = score;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

     @Override
    public String toString() {
        return "ReviewDto [id=" + id + ", reviewDate=" + reviewDate + ", score=" + score + ", comments=" + comments
                + "]";
    }

}

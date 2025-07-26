package com.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.entity.PerformanceReview;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {


    // We can define any custom query methods here
    // For example:
    // List<PerformanceReview> findbyReviewComments(String name);
    //-->This returns list of PerformanceReview by ReviewComments
    // We can also use Spring Data JPA's derived query methods
    // or @Query annotations for more complex queries.

}

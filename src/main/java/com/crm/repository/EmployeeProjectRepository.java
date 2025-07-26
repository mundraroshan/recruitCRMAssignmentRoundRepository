package com.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.entity.EmployeeProject;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Long> {
    
    // Define any custom query methods if needed
    // For example:
    // List<EmployeeProject> findByEmployeeId(Long empId);
    // This returns list of EmployeeProject by employee ID
    
    // We can also use Spring Data JPA's derived query methods
    // or @Query annotations for more complex queries.
    
}

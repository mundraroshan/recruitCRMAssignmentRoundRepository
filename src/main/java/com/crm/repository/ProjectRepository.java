package com.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    // Define any custom query methods if needed
    // For example:
    // List<Project> findByName(String name);
    // ---> This returns list of projects by name
    
    // You can also use Spring Data JPA's derived query methods
    // or @Query annotations for more complex queries.
    
}

package com.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    

    // We can define any custom query methods here
    // For example:
    // List<Department> findbyName(String name);
    //-->This returns list of departments by name
    
    // We can also use Spring Data JPA's derived query methods
    // or @Query annotations for more complex queries.
    
}

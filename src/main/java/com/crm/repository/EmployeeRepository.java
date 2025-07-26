package com.crm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.crm.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    // We can define any custom query methods here
    // For example:
    // List<Employee> findbyName(String name);
    //-->This returns list of Employees by name


    // We can also use Spring Data JPA's derived query methods
    // or @Query annotations for more complex queries.
}

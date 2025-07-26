package com.crm.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.crm.dto.EmployeeDto;
import com.crm.entity.Employee;
import com.crm.entity.PerformanceReview;
import com.crm.exception.ResourceNotFoundException;
import com.crm.mapper.EmployeeMapper;
import com.crm.repository.EmployeeRepository;
import com.crm.service.EmployeeService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

  private EmployeeRepository employeeRepository;

  private static final Logger LOG = LogManager.getLogger(EmployeeServiceImpl.class);

  @Value("${config.maximumReviewsCount}")
  private Integer maxReviewsCount;

  public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  /**
   * Retrieves comprehensive employee data including their projects and
   * performance reviews.
   * 
   * @param id The unique identifier of the employee
   * @return EmployeeDto containing employee details, projects, and filtered
   *         performance reviews
   * @throws ResourceNotFoundException if employee is not found
   * @throws RuntimeException          if there's an unexpected error during data
   *                                   retrieval
   */
  @Override
  public EmployeeDto getEmployeesData(Long id) {
    LOG.info("Fetching employee data for ID: {}", id);

    // Step 1: Retrieve employee details from database
    Employee employee = fetchEmployeeById(id);

    // Step 2: Process and filter performance reviews based on configuration
    Set<PerformanceReview> filteredReviews = processEmployeeReviews(employee, id);

    // Step 3: Transform entity data to DTO for client response
    return EmployeeMapper.mapToEmployeeDto(
        employee,
        employee.getEmployeeProjects(),
        filteredReviews);
  }

  /**
   * Retrieves an employee by their ID from the repository.
   * 
   * @param id The employee's unique identifier
   * @return Employee entity if found
   * @throws ResourceNotFoundException if employee doesn't exist
   * @throws RuntimeException          for database access issues
   */
  private Employee fetchEmployeeById(Long id) {
    try {
      // Attempt to find employee, throw custom exception if not found
      return employeeRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", String.valueOf(id)));
    } catch (EntityNotFoundException e) {
      // Handle specific case where entity is not found
      LOG.error("Employee not found with ID: {}", id);
      throw new ResourceNotFoundException("Employee", "id", String.valueOf(id));
    } catch (Exception e) {
      // Handle unexpected database or system errors
      LOG.error("Error accessing employee data for ID: {}", id, e);
      throw new RuntimeException("Database access error while fetching employee data");
    }
  }

  /**
   * Processes and filters employee performance reviews based on configured
   * maximum limit.
   * 
   * @param employee The employee whose reviews need to be processed
   * @param id       The employee's ID (for logging purposes)
   * @return Set of filtered performance reviews
   */
  private Set<PerformanceReview> processEmployeeReviews(Employee employee, Long id) {
    Set<PerformanceReview> reviews = employee.getPerformanceReviews();

    // Handle case where employee has no reviews
    if (reviews == null || reviews.isEmpty()) {
      LOG.warn("No performance reviews available for employee ID: {}", id);
      return new HashSet<>();
    }

    // If reviews are within limit, return as is
    if (reviews.size() <= maxReviewsCount) {
      return reviews;
    }

    // Filter to get only the most recent reviews up to maxReviewsCount
    LOG.info("Filtering to most recent {} reviews for employee ID: {}", maxReviewsCount, id);
    return reviews.stream()
        // Sort in descending order (newest first)
        .sorted((r1, r2) -> r2.getReviewDate().compareTo(r1.getReviewDate()))
        // Take only the configured maximum number of reviews
        .limit(maxReviewsCount)
        // Collect into LinkedHashSet to maintain sorting order
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  /**
   * Retrieves all employees based on the provided filter criteria.
   * 
   * @param filterCriteria JSON object containing filter parameters
   * @return List of EmployeeDto objects matching the criteria
   * @throws ResourceNotFoundException if no employees match the criteria
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<EmployeeDto> getAllEmployeesData(JSONObject filterCriteria) {
    LOG.info("Initiating employee search with filters: {}", filterCriteria);

    // Apply filters and retrieve matching employees
    List<Employee> employees = findEmployeesWithFilters(filterCriteria);

    // Validate and process results
    validateSearchResults(employees, filterCriteria);

    // Transform to DTOs
    return mapEmployeesToDtos(employees);
  }

  /**
   * Applies the specified filters to find matching employees.
   */
  private List<Employee> findEmployeesWithFilters(JSONObject filterCriteria) {
    return employeeRepository.findAll((Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      
      // Apply each type of filter
      applyDepartmentFilter(filterCriteria, root, predicates);
      applyProjectFilter(filterCriteria, root, predicates);
      applyReviewDateFilter(filterCriteria, root, cb, predicates);

      // Return combined predicates or default conjunction
      return predicates.isEmpty() ? cb.conjunction() 
                                : cb.and(predicates.toArray(new Predicate[0]));
    });
  }

  /**
   * Applies department-based filtering if specified in criteria.
   */
  @SuppressWarnings("unchecked")
  private void applyDepartmentFilter(JSONObject criteria, Root<Employee> root, List<Predicate> predicates) {
    if (criteria.containsKey("department")) {
      List<String> departments = (List<String>) criteria.get("department");
      if (departments != null && !departments.isEmpty()) {
        LOG.info("Applying department filter: {}", departments);
        predicates.add(root.get("department").get("name").in(departments));
      }
    }
  }

  /**
   * Applies project-based filtering if specified in criteria.
   */
  @SuppressWarnings("unchecked")
  private void applyProjectFilter(JSONObject criteria, Root<Employee> root, List<Predicate> predicates) {
    if (criteria.containsKey("projects")) {
      List<String> projects = (List<String>) criteria.get("projects");
      if (projects != null && !projects.isEmpty()) {
        LOG.info("Applying project filter: {}", projects);
        predicates.add(root.join("employeeProjects").join("project").get("name").in(projects));
      }
    }
  }

  /**
   * Applies review date filtering if specified in criteria.
   */
  private void applyReviewDateFilter(JSONObject criteria, Root<Employee> root, 
                                   CriteriaBuilder cb, List<Predicate> predicates) {
    if (criteria.containsKey("reviewDate")) {
      String dateStr = (String) criteria.get("reviewDate");
      if (dateStr != null && !dateStr.isEmpty()) {
        LOG.info("Applying review date filter: {}", dateStr);
        java.sql.Date reviewDate = java.sql.Date.valueOf(dateStr);
        predicates.add(cb.equal(root.join("performanceReviews").get("reviewDate"), reviewDate));
      }
    }
  }

  /**
   * Validates that the search returned results.
   */
  private void validateSearchResults(List<Employee> employees, JSONObject filterCriteria) {
    if (employees.isEmpty()) {
      LOG.warn("Search returned no results for criteria: {}", filterCriteria);
      throw new ResourceNotFoundException("Employee", "filterCriteria", filterCriteria.toString());
    }
    LOG.info("Found {} matching employees", employees.size());
  }

  /**
   * Maps employee entities to DTOs.
   */
  private List<EmployeeDto> mapEmployeesToDtos(List<Employee> employees) {
    return employees.stream()
        .map(employee -> EmployeeMapper.mapToEmployeeDto(
            employee,
            employee.getEmployeeProjects(),
            employee.getPerformanceReviews()))
        .collect(Collectors.toList());

  }
}
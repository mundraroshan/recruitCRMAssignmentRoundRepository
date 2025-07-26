package com.crm.mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.crm.dto.EmployeeDto;
import com.crm.dto.ProjectDto;
import com.crm.dto.ReviewDto;
import com.crm.entity.Employee;
import com.crm.entity.EmployeeProject;
import com.crm.entity.PerformanceReview;
import com.crm.entity.Project;

public class EmployeeMapper {

    private static final Logger LOG = LogManager.getLogger(EmployeeMapper.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Safely formats a date to string using the default date format.
     * @param date the date to format
     * @return formatted date string or null if date is null
     */
    private static String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : null;
    }

    /**
     * Safely gets a name from an entity that may be null
     * @param entity the entity that has a getName method
     * @param defaultValue the default value if entity is null
     * @return the name or default value
     */
    private static String getNameOrDefault(Object entity, String defaultValue) {
        try {
            return entity != null ? ((Class.forName(entity.getClass().getName())).getMethod("getName").invoke(entity)).toString() : defaultValue;
        } catch (Exception e) {
            LOG.warn("Error getting name from entity: {}", e.getMessage());
            return defaultValue;
        }
    }

    /**
     * Maps project details to ProjectDto
     * @param project the project entity
     * @return mapped ProjectDto
     */
    private static ProjectDto mapToProjectDto(Project project) {
        LOG.debug("Mapping project ID: {}", project.getId());
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(Long.valueOf(project.getId()));
        projectDto.setProjectName(project.getName());
        projectDto.setStartDate(formatDate(project.getStartDate()));
        projectDto.setEndDate(formatDate(project.getEndDate()));
        projectDto.setDepartmentName(getNameOrDefault(project.getDepartment(), "No Department"));
        return projectDto;
    }

    /**
     * Maps review details to ReviewDto
     * @param review the performance review entity
     * @return mapped ReviewDto
     */
    private static ReviewDto mapToReviewDto(PerformanceReview review) {
        LOG.debug("Mapping review ID: {}", review.getId());
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setReviewDate(review.getReviewDate().toString());
        reviewDto.setScore(review.getScore());
        reviewDto.setComments(review.getReviewComments());
        return reviewDto;
    }

    public static EmployeeDto mapToEmployeeDto(Employee employee, Set<EmployeeProject> employeeProjects,
            Set<PerformanceReview> reviews) {
        LOG.debug("Mapping Employee to EmployeeDto for employee ID: {}", employee.getId());
        EmployeeDto employeeDto = new EmployeeDto();
        try {
            employeeDto.setId(employee.getId());
            employeeDto.setName(employee.getName());
            employeeDto.setEmail(employee.getEmail());
            employeeDto.setDateOfJoining(formatDate(employee.getDateOfJoining()));
            employeeDto.setSalary(employee.getSalary());
            employeeDto.setManagerName(getNameOrDefault(employee.getManager(), "No Manager"));
            employeeDto.setDepartmentName(getNameOrDefault(employee.getDepartment(), "No Department"));

            // Map projects
            if (employeeProjects != null && !employeeProjects.isEmpty()) {
                LOG.debug("Mapping {} projects for employee ID: {}", employeeProjects.size(), employee.getId());
                List<ProjectDto> projectDtos = employeeProjects.stream()
                        .map(ep -> mapToProjectDto(ep.getProject()))
                        .collect(Collectors.toList());
                employeeDto.setProjects(projectDtos);
            } else {
                employeeDto.setProjects(List.of());
            }

            // Map performance reviews
            List<ReviewDto> reviewDtos = new ArrayList<>();
            if (reviews != null && !reviews.isEmpty()) {
                LOG.debug("Mapping {} performance reviews for employee ID: {}", reviews.size(), employee.getId());
                reviewDtos = reviews.stream()
                        .map(EmployeeMapper::mapToReviewDto)
                        .collect(Collectors.toList());
            }
            employeeDto.setPerformanceReviews(reviewDtos);
        } catch (Exception e) {
            LOG.error("Error mapping Employee to EmployeeDto for employee ID: {} :: {}", employee.getId(),
                    e.getMessage(), e);
        }
        return employeeDto;
    }

}

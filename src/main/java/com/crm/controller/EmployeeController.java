package com.crm.controller;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dto.EmployeeDto;
import com.crm.dto.ResponseDto;
import com.crm.service.EmployeeService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * REST Controller for managing Employee-related operations.
 * Provides endpoints for retrieving employee data and filtering employees based on various criteria.
 * All responses are wrapped in ResponseDto for consistent API response structure.
 * 
 * Base Path: /crm
 * Produces: APPLICATION_JSON
 */
@RequestMapping(path = "/crm", produces = { MediaType.APPLICATION_JSON_VALUE })
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*")
@RestController
@Validated
public class EmployeeController {

    private EmployeeService employeeService;

    private static final Logger LOGGER = LogManager.getLogger(EmployeeController.class);

    /**
     * Constructs an EmployeeController with the required service dependency.
     * 
     * @param employeeService Service layer component for employee operations
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves detailed employee information by their ID.
     * 
     * @param id The employee ID (must be a positive number)
     * @return ResponseEntity containing employee data or appropriate error response
     * 
     * HTTP Status:
     * - 200 OK: Employee found and returned successfully
     * - 400 BAD_REQUEST: Invalid ID format or negative ID
     * - 404 NOT_FOUND: Employee with given ID doesn't exist
     * - 500 INTERNAL_SERVER_ERROR: Unexpected server error
     */
    @GetMapping("/getEmployeeDatabyId/{id}")
    public ResponseEntity<ResponseDto> fetchEmployeeData(
            @NotNull @Pattern(regexp = "^[0-9]+$", message = "ID should be a number") @PathVariable("id") String id) {
        LOGGER.info("Fetching employee data for ID: {}", id);
        ResponseDto dto = new ResponseDto();
        try {
            Long employeeId = Long.parseLong(id);
            if (employeeId <= 0) {
                LOGGER.warn("Invalid employee ID: {}", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDto(false, new Date(), 0, "employee id must be greater than 0", "BAD_REQUEST",
                                null));
            }
            EmployeeDto employeeDto = employeeService.getEmployeesData(employeeId);
            if (employeeDto == null) {
                LOGGER.error("Employee not found for ID: {}", id);
                dto.setSuccess(false);
                dto.setCount(0);
                dto.setMessage("Employee not found.");
                dto.setErrorCode("EMPLOYEE_NOT_FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
            } else {
                LOGGER.info("Employee data fetched successfully for ID: {}", id);
                dto.setSuccess(true);
                dto.setMessage("Employee data fetched successfully.");
                dto.setData(employeeDto);
                dto.setCount(1);
                return ResponseEntity.status(HttpStatus.OK).body(dto);
            }
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid ID format: {}", id, e);
            dto.setSuccess(false);
            dto.setMessage("Invalid ID format. ID must be a valid number.");
            dto.setErrorCode("INVALID_ID_FORMAT");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
        } catch (Exception e) {
            LOGGER.error("Unexpected error while fetching employee with ID: {}", id, e);
            dto.setSuccess(false);
            dto.setMessage("An unexpected error occurred while fetching employee data.");
            dto.setErrorCode("INTERNAL_SERVER_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
        }
    }

    /**
     * Filters and retrieves employees based on specified criteria.
     * Supports filtering by department, projects, and review dates.
     * 
     * @param filterCriteria JSON object containing filter parameters:
     *                       - department: List of department names
     *                       - projects: List of project names
     *                       - reviewDate: Review date to filter by
     * @return ResponseEntity containing filtered list of employees or appropriate error response
     * 
     * HTTP Status:
     * - 200 OK: Employees found and returned successfully
     * - 400 BAD_REQUEST: Invalid filter criteria format
     * - 404 NOT_FOUND: No employees match the filter criteria
     * - 500 INTERNAL_SERVER_ERROR: Unexpected server error
     */
    @PostMapping("/getFilterEmployees")
    public ResponseEntity<ResponseDto> filterEmployees(@RequestBody JSONObject filterCriteria) {
        LOGGER.info("Fetching all the filtered employees with criteria: {}", filterCriteria);
        ResponseDto dto = new ResponseDto();
        try {
            List<EmployeeDto> employees = employeeService.getAllEmployeesData(filterCriteria);
            LOGGER.info("Employees fetched: {}", employees.size());
            
            if (employees.isEmpty()) {
                LOGGER.info("No employees found matching the criteria.");
                dto.setSuccess(false);
                dto.setMessage("No employees found matching the criteria.");
                dto.setErrorCode("NO_EMPLOYEES_FOUND");
                dto.setCount(0);
                dto.setTimeStamp(new Date());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
            }
            
            dto.setSuccess(true);
            dto.setMessage("Employees fetched successfully.");
            dto.setData(employees);
            dto.setCount(employees.size());
            dto.setTimeStamp(new Date());
            return ResponseEntity.status(HttpStatus.OK).body(dto);
            
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid filter criteria: {}", filterCriteria, e);
            dto.setSuccess(false);
            dto.setMessage("Invalid filter criteria provided.");
            dto.setErrorCode("INVALID_FILTER_CRITERIA");
            dto.setTimeStamp(new Date());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
        } catch (Exception e) {
            LOGGER.error("Unexpected error while filtering employees: {}", filterCriteria, e);
            dto.setSuccess(false);
            dto.setMessage("An unexpected error occurred while filtering employees.");
            dto.setErrorCode("INTERNAL_SERVER_ERROR");
            dto.setTimeStamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
        }
    }

}

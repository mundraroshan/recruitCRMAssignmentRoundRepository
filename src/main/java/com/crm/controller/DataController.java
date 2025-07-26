package com.crm.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dto.DropDownDto;
import com.crm.dto.ResponseDto;
import com.crm.service.DataService;

/**
 * REST Controller for handling reference data operations.
 * Provides endpoints for retrieving dropdown data such as departments and projects
 * used throughout the CRM application.
 * All responses are wrapped in ResponseDto for consistent API response structure.
 * 
 * Base Path: /setup
 * Produces: APPLICATION_JSON
 * Cross-Origin: Enabled for localhost:3001
 * 
 * @see ResponseDto
 * @see DataService
 * @see DropDownDto
 */
@RequestMapping(path = "/data", produces = { MediaType.APPLICATION_JSON_VALUE })
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*")
@RestController
@Validated
public class DataController {

    private DataService dataService;

    /**
     * Constructs a DataController with the required service dependency.
     * 
     * @param dataService Service layer component handling reference data operations
     *                   including departments and projects retrieval
     */
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    /**
     * Retrieves all departments for dropdown population.
     * Returns departments sorted alphabetically by name.
     * The response is wrapped in a ResponseDto for consistency.
     * 
     * @return ResponseEntity containing:
     *         - List of departments as DropDownDto objects if found
     *         - Empty response with NOT_FOUND status if no departments exist
     *         - Success flag indicating operation status
     *         - Count of departments returned
     *         - Error code corresponding to HTTP status
     *         - Success/error message
     *         
     * HTTP Status:
     * - 200 OK: Departments found and returned successfully
     * - 404 NOT_FOUND: No departments exist in the system
     * 
     * @see DropDownDto
     * @see ResponseDto
     */
    @GetMapping("/fetch/department")
    public ResponseEntity<ResponseDto> getDepartments() {
        List<DropDownDto> depDto = dataService.getDepartments();
        ResponseDto dto = new ResponseDto();
        if (depDto.size() == 0) {
            dto.setSuccess(false);
            dto.setCount(0);
            dto.setErrorCode(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
        } else {
            dto.setSuccess(true);
            dto.setCount(depDto.size());
            dto.setData(depDto);
            dto.setErrorCode(HttpStatus.OK.toString());
            dto.setMessage("Departments fetched successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        }
    }

    /**
     * Retrieves all projects for dropdown population.
     * Returns projects sorted alphabetically by name.
     * The response is wrapped in a ResponseDto for consistency.
     * 
     * @return ResponseEntity containing:
     *         - List of projects as DropDownDto objects if found
     *         - Empty response with NOT_FOUND status if no projects exist
     *         - Success flag indicating operation status
     *         - Count of projects returned
     *         - Error code corresponding to HTTP status
     *         - Success/error message
     *         
     * HTTP Status:
     * - 200 OK: Projects found and returned successfully
     * - 404 NOT_FOUND: No projects exist in the system
     * 
     * @see DropDownDto
     * @see ResponseDto
     */
    @GetMapping("/fetch/project")
    public ResponseEntity<ResponseDto> getProjects() {
        List<DropDownDto> depDto = dataService.getProjects();
        ResponseDto dto = new ResponseDto();
        if (depDto.size() == 0) {
            dto.setSuccess(false);
            dto.setCount(0);
            dto.setErrorCode(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
        } else {
            dto.setSuccess(true);
            dto.setCount(depDto.size());
            dto.setData(depDto);
            dto.setErrorCode(HttpStatus.OK.toString());
            dto.setMessage("Projects fetched successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        }
    }
}
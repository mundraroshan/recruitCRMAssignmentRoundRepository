package com.crm.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.crm.dto.DropDownDto;
import com.crm.entity.Department;
import com.crm.entity.Project;
import com.crm.repository.DepartmentRepository;
import com.crm.repository.ProjectRepository;
import com.crm.service.DataService;

/**
 * Implementation of the DataService interface that provides functionality
 * for retrieving dropdown data for departments and projects.
 * This service is responsible for converting entity data to DTOs suitable
 * for UI dropdown components.
 */
@Service
public class DataServiceImpl implements DataService {

    private DepartmentRepository departmentRepository;
    private ProjectRepository projectRepository;

    /**
     * Constructs a new dataServiceImpl with required repositories.
     * 
     * @param departmentRepository Repository for accessing department data
     * @param projectRepository Repository for accessing project data
     */
    public DataServiceImpl(DepartmentRepository departmentRepository, ProjectRepository projectRepository) {
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * Retrieves all departments and converts them to DropDownDto format.
     * The departments are sorted by ID for consistent display order.
     * 
     * @return List of departments as DropDownDto objects, sorted by ID.
     *         Returns empty list if no departments are found.
     */
    @Override
    public List<DropDownDto> getDepartments() {
        // Fetch all departments from the repository
        List<Department> departments = departmentRepository.findAll();
        
        // Return empty list if no departments found
        if (departments.size() == 0) {
            return new ArrayList<>();
        }

        // Convert departments to DTOs and sort by ID
        return departments.stream()
            .map(dep -> {
                DropDownDto dto = new DropDownDto();
                dto.setId(dep.getId());
                dto.setName(dep.getName());
                return dto;
            })
            .sorted(Comparator.comparing(DropDownDto::getId))
            .toList();
    }

    /**
     * Retrieves all projects and converts them to DropDownDto format.
     * The projects are sorted by ID for consistent display order.
     * 
     * @return List of projects as DropDownDto objects, sorted by ID.
     *         Returns empty list if no projects are found.
     */
    @Override
    public List<DropDownDto> getProjects() {
        // Fetch all projects from the repository
        List<Project> projects = projectRepository.findAll();
        
        // Return empty list if no projects found
        if (projects.size() == 0) {
            return new ArrayList<>();
        }

        // Convert projects to DTOs and sort by ID
        return projects.stream()
            .map(proj -> {
                DropDownDto dto = new DropDownDto();
                dto.setId(proj.getId());
                dto.setName(proj.getName());
                return dto;
            })
            .sorted(Comparator.comparing(DropDownDto::getId))
            .toList();
    }

}

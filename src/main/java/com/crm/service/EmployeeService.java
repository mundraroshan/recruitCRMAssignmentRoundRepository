package com.crm.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.crm.dto.EmployeeDto;

public interface EmployeeService {
    
    public EmployeeDto getEmployeesData(Long id);

    public List<EmployeeDto> getAllEmployeesData(JSONObject filterCriteria);
}

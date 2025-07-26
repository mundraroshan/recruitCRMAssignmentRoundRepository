package com.crm.service;

import java.util.List;

import com.crm.dto.DropDownDto;

public interface DataService {

    public List<DropDownDto> getDepartments();
    public List<DropDownDto> getProjects();
}
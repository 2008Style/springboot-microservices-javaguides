package net.javaguides.employeeservice.service;

import net.javaguides.employeeservice.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(url = "http://localhost:8080;http://localhost:8082", value = "DEPARMENT-SERVICE")
@FeignClient(name = "DEPARTMENT-SERVICE")
public interface APIClient {

    @GetMapping("api/departments/{departmentCode}")
    public DepartmentDto getDepartment(@PathVariable("departmentCode") String departmentCode);

}

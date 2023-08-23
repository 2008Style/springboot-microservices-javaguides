package net.javaguides.employeeservice.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import net.javaguides.employeeservice.dto.APIResponseDto;
import net.javaguides.employeeservice.dto.DepartmentDto;
import net.javaguides.employeeservice.dto.EmployeeDto;
import net.javaguides.employeeservice.dto.OrganizationDto;
import net.javaguides.employeeservice.entity.Employee;
import net.javaguides.employeeservice.exception.ResourceNotFoundException;
import net.javaguides.employeeservice.mapper.ManEmployeeMapper;
import net.javaguides.employeeservice.repository.EmployeeRepository;
import net.javaguides.employeeservice.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

//    private RestTemplate restTemplate;

    private WebClient webClient;

//    private APIClient apiClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        //Manual
//        Employee employee = new Employee(
//                employeeDto.getId(),
//                employeeDto.getFirstName(),
//                employeeDto.getLastName(),
//                employeeDto.getEmail(),
//                employeeDto.getDepartmentCode()
//        );
        // ModelMapper
//        Employee employee = modelMapper.map(employeeDto, Employee.class);

        Employee employee = ManEmployeeMapper.mapToEmployee(employeeDto);

        // Map Struct falla con los campos de DepartmentDto
//        Employee employee = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);

        Employee savedEmployee = employeeRepository.save(employee);

        // Manual
//        EmployeeDto savedEmployeeDto = new EmployeeDto(
//                savedEmployee.getId(),
//                savedEmployee.getFirstName(),
//                savedEmployee.getLastName(),
//                savedEmployee.getEmail(),
//                savedEmployee.getDepartmentCode()
//        );

        // ModelMapper
//        EmployeeDto savedEmployeeDto = modelMapper.map(savedEmployee, EmployeeDto.class);
        // Manual Mapper
        EmployeeDto savedEmployeeDto = ManEmployeeMapper.mapToEmployeeDto(savedEmployee);

        return savedEmployeeDto;

        // Map Struct falla con los campos de DepartmentDto
//        return EmployeeMapper.MAPPER.mapToEmployeeDto(savedEmployee);
    }

    //@CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {

        LOGGER.info("inside getEmployeeById() method");

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId)
        );

//        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/" + employee.getDepartmentCode(),
//                DepartmentDto.class);
//        DepartmentDto departmentDto = responseEntity.getBody();

        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

//        DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());

//        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        OrganizationDto organizationDto = webClient.get()
                .uri("http://localhost:8084/api/organizations/" + employee.getOrganizationCode())
                .retrieve()
                .bodyToMono(OrganizationDto.class)
                .block();

        EmployeeDto employeeDto = ManEmployeeMapper.mapToEmployeeDto(employee);

        //Map Struct falla con los campos de DepartmentDto
//        EmployeeDto employeeDto = EmployeeMapper.MAPPER.mapToEmployeeDto(employee);

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);
        apiResponseDto.setOrganizationDto(organizationDto);

        // Manual
//        EmployeeDto employeeDto = new EmployeeDto(
//                employee.getId(),
//                employee.getFirstName(),
//                employee.getLastName(),
//                employee.getEmail()
//        );
        // Model Mapper
//        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        // Map Struct
        return apiResponseDto;
    }

    public APIResponseDto getDefaultDepartment(Long employeeId, Exception exception) {

        LOGGER.info("inside getDefaultDepartment() method");

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId)
        );

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("R&D Department");
        departmentDto.setDepartmentCode("RD001");
        departmentDto.setDepartmentDescription("Research and Development Department");

        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);

        return apiResponseDto;
    }

}

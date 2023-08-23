package net.javaguides.departmentservice.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.departmentservice.dto.DepartmentDto;
import net.javaguides.departmentservice.entity.Department;
import net.javaguides.departmentservice.exception.ResourceNotFoundException;
import net.javaguides.departmentservice.mapper.DepartmentMapper;
import net.javaguides.departmentservice.mapper.ManDepartmentMapper;
import net.javaguides.departmentservice.repository.DepartmentRepository;
import net.javaguides.departmentservice.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;

    private ModelMapper modelMapper;

    @Override
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {

        // Convert department dto to department jpa entity
        // Manual
//        Department department = new Department(
//                departmentDto.getId(),
//                departmentDto.getDepartmentName(),
//                departmentDto.getDepartmentDescription(),
//                departmentDto.getDepartmentCode()
//        );
        //Model Mapper
//        Department department = modelMapper.map(departmentDto, Department.class);
        // Map Struct
//        Department department = DepartmentMapper.MAPPER.mapToDepartment(departmentDto);

        // Manual Mapper
        Department department = ManDepartmentMapper.mapToDepartment(departmentDto);

        Department savedDepartment = departmentRepository.save(department);

        // Manual
//        DepartmentDto savedDepartmentDto = new DepartmentDto(
//                savedDepartment.getId(),
//                savedDepartment.getDepartmentName(),
//                savedDepartment.getDepartmentDescription(),
//                savedDepartment.getDepartmentCode()
//        );
        // Model Mapper
//        DepartmentDto savedDepartmentDto = modelMapper.map(savedDepartment, DepartmentDto.class);
        // Map Struct
//        DepartmentDto savedDepartmentDto = DepartmentMapper.MAPPER.mapToDepartmentDto(savedDepartment);

        // Manual Mapper
        DepartmentDto savedDepartmentDto = ManDepartmentMapper.mapToDepartmentDto(savedDepartment);

        return savedDepartmentDto;
    }

    @Override
    public DepartmentDto getDepartmentByCode(String departmentCode) {

        Optional<Department> optionalDepartment = departmentRepository.findByDepartmentCode(departmentCode);

        if (optionalDepartment.isEmpty()) {
            throw new ResourceNotFoundException("Department", "code", departmentCode);
        }

        // Manual
//        DepartmentDto departmentDto = new DepartmentDto(
//                department.getId(),
//                department.getDepartmentName(),
//                department.getDepartmentDescription(),
//                department.getDepartmentCode()
//        );
        //Model Mapper
//        DepartmentDto departmentDto = modelMapper.map(department, DepartmentDto.class);
        // Map Struct

//        return DepartmentMapper.MAPPER.mapToDepartmentDto(optionalDepartment.get());

        return ManDepartmentMapper.mapToDepartmentDto(optionalDepartment.get());

    }
}

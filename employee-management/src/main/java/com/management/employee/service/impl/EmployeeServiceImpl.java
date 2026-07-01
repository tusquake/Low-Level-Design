package com.management.employee.service.impl;

import com.management.employee.dto.EmployeeRequestDTO;
import com.management.employee.dto.EmployeeResponseDTO;
import com.management.employee.entity.Employee;
import com.management.employee.exception.DuplicateEmailException;
import com.management.employee.exception.ResourceNotFoundException;
import com.management.employee.repository.EmployeeRepository;
import com.management.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        log.info("Attempting to create a new employee with email: {}", requestDTO.email());
        
        if (employeeRepository.existsByEmail(requestDTO.email())) {
            log.error("Duplicate email collision for: {}", requestDTO.email());
            throw new DuplicateEmailException("Employee with email " + requestDTO.email() + " already exists");
        }

        Employee employee = Employee.builder()
                .name(requestDTO.name())
                .email(requestDTO.email())
                .department(requestDTO.department())
                .salary(requestDTO.salary())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        
        log.info("Successfully created employee with id: {}", savedEmployee.getId());
        return mapToResponseDTO(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        return mapToResponseDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {
        log.info("Fetching employees page with details: {}", pageable);
        return employeeRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
        log.info("Attempting to update employee with id: {}", id);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        // If email has changed, check for duplicates
        if (!employee.getEmail().equalsIgnoreCase(requestDTO.email()) &&
                employeeRepository.existsByEmail(requestDTO.email())) {
            log.error("Duplicate email collision on update for: {}", requestDTO.email());
            throw new DuplicateEmailException("Employee with email " + requestDTO.email() + " already exists");
        }

        employee.setName(requestDTO.name());
        employee.setEmail(requestDTO.email());
        employee.setDepartment(requestDTO.department());
        employee.setSalary(requestDTO.salary());

        Employee updatedEmployee = employeeRepository.save(employee);
        
        log.info("Successfully updated employee with id: {}", updatedEmployee.getId());
        return mapToResponseDTO(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Attempting to delete employee with id: {}", id);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        employeeRepository.delete(employee);
        log.info("Successfully deleted employee with id: {}", id);
    }

    private EmployeeResponseDTO mapToResponseDTO(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary(),
                employee.getCreatedAt(),
                employee.getUpdatedAt(),
                employee.getVersion()
        );
    }
}

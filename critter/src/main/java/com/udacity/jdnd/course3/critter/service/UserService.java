package com.udacity.jdnd.course3.critter.service;


import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.EmployeeSkillType;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {

    private EmployeeRepository employeeRepository;
    private CustomerRepository customerRepository;
    private PetRepository petRepository;
    private ModelMapper modelMapper;



    public UserService( EmployeeRepository employeeRepository, CustomerRepository customerRepository, PetRepository petRepository) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
        this.modelMapper = new ModelMapper();
    }


    public Customer saveCustomer(Customer customer){
        this.customerRepository.save(customer);
        return customer;
    }

    public List<Customer> getAllCustomers(){
        return this.customerRepository.findAll();
    }
    public Customer getCustomerById(Long id){
        return this.customerRepository.getOne(id);
    }


    public Customer findOwnerByhisPetId(Long id){
        return this.customerRepository.findCustomerByPets_Id(id);
    }
    public Employee saveEmployee(Employee emp){
        this.employeeRepository.save(emp);
        return emp;
    }
    public Employee getEmployeeById(Long id){
        return this.employeeRepository.getOne(id);
    }

    @SneakyThrows
    public void updateEmployeeAvailability(Set<DayOfWeek> daysAvailabil , Long id){
        Optional<Employee> employee = this.employeeRepository.findById(id);
        if(employee.isPresent()){
            employee.get().setDaysAvailable(daysAvailabil);
            return;
        }
        employee.orElseThrow(() -> new Exception("Employee does not exist"));
    }

    public List<Employee> findAvailableEmployees(Set<EmployeeSkillType> skills, DayOfWeek dayOfWeek ){
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(dayOfWeek);
        List<Employee> employees = employeeRepository.findAll();

        List<Employee> employeeList = new ArrayList<>();
        employees.forEach(thisEmployee -> {
            if(thisEmployee.getSkills().containsAll(skills) && thisEmployee.getDaysAvailable().containsAll(dayOfWeeks)){
                employeeList.add(thisEmployee);
            }
        });
        return employeeList;
    }

}

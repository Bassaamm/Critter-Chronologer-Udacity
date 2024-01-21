package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dtos.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.h2.engine.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private ScheduleService scheduleService;
    private ModelMapper modelMapper;
    private UserService userService;
    private PetService petService;

    public ScheduleController(ScheduleService scheduleService, UserService userService, PetService petService) {
        this.scheduleService = scheduleService;
        this.modelMapper = new ModelMapper();
        this.userService = userService;
        this.petService = petService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        List<Employee> employees = findEmployeesById(scheduleDTO.getEmployeeIds());
        schedule.setEmployees(employees);

        List<Pet> pets = findPetsById(scheduleDTO.getPetIds());
        schedule.setPets(pets);

        Schedule newSchedule = this.scheduleService.saveSchedule(schedule);

        schedule.setCustomers(
                pets.stream()
                        .map(pet -> this.userService.getCustomerById(pet.getCustomer().getId()))
                        .collect(Collectors.toList())
        );

        employees.forEach(employee -> {
            if(employee.getSchedules() == null) employee.setSchedules(new ArrayList<>());
            employee.getSchedules().add(schedule);
            this.userService.saveEmployee(employee);
        });

        pets.forEach(pet -> {
            if(pet.getSchedules() == null) pet.setSchedules(new ArrayList<>());
            pet.getSchedules().add(schedule);
            petService.savePet(pet);
        });

        schedule.getCustomers().forEach(customer -> {
            if(customer.getSchedules() == null) customer.setSchedules(new ArrayList<>());
            customer.getSchedules().add(schedule);
            this.userService.saveCustomer(customer);
        });

        return convertToDTO(newSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = this.scheduleService.getAllSchedules();
        return schedules.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = this.scheduleService.getScheduleByPet(petId);
        List<ScheduleDTO> scheduleDTOList= new ArrayList<>();
        for(Schedule schedule: schedules){
            scheduleDTOList.add(convertToDTO(schedule));
        }
        return scheduleDTOList;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = this.scheduleService.getScheduleByEmployee(employeeId);
        List<ScheduleDTO> scheduleDTOList= new ArrayList<>();
        for(Schedule schedule: schedules){
            scheduleDTOList.add(convertToDTO(schedule));
        }
        return scheduleDTOList;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules = this.scheduleService.getScheduleByCustomer(customerId);
        List<ScheduleDTO> scheduleDTOList= new ArrayList<>();
        for(Schedule schedule: schedules){
            scheduleDTOList.add(convertToDTO(schedule));
        }
        return scheduleDTOList;
    }

    public ScheduleDTO convertToDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        scheduleDTO.setPetIds(getPetsId(schedule.getPets()));
        scheduleDTO.setEmployeeIds(getUsersId(schedule.getEmployees()));
        return scheduleDTO;

    }

    public List<Long> getUsersId(List<Employee> employees) {
        List<Long> employeesId = new ArrayList<>();
        for (Employee employee : employees) {
            employeesId.add(employee.getId());
        }
        return employeesId;
    }

    public List<Long> getPetsId(List<Pet> pets) {
        List<Long> petsId = new ArrayList<>();
        for (Pet pet : pets) {
            petsId.add(pet.getId());
        }
        return petsId;
    }

    public List<Pet> findPetsById(List<Long> petsId) {
        List<Pet> pets = new ArrayList<>();
        for (Long id : petsId) {
            pets.add(petService.getPetByPetId(id));
        }
        System.out.println("pets.size() = " + pets.size());
        return pets;
    }

    public List<Employee> findEmployeesById(List<Long> employeesId) {
        List<Employee> employees = new ArrayList<>();
        for (Long id : employeesId) {
            employees.add(this.userService.getEmployeeById(id));
        }
        System.out.println("employees.size() = " + employees.size());
        return employees;
    }

}

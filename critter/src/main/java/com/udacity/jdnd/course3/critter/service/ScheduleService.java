package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.repository.SchduleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleService {

    private SchduleRepository schduleRepository;


    public ScheduleService(SchduleRepository schduleRepository) {
        this.schduleRepository = schduleRepository;
    }
    public Schedule saveSchedule(Schedule schedule) {
        return this.schduleRepository.save(schedule);
    }
        public List<Schedule> getAllSchedules(){
        return this.schduleRepository.findAll();
    }
    public List<Schedule> getScheduleByEmployee(Long id){
        return this.schduleRepository.getSchedulesByEmployees_Id(id);
    }
    public List<Schedule> getScheduleByPet(Long petId){
        return this.schduleRepository.getSchedulesByPets_Id(petId);
    }
    public List<Schedule> getScheduleByCustomer(Long ownerId){
        return this.schduleRepository.getSchedulesByCustomers_Id(ownerId);
    }

}

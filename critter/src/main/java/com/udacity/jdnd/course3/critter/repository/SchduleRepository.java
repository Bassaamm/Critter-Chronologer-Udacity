package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface SchduleRepository extends JpaRepository<Schedule,Long> {

    List<Schedule> getSchedulesByPets_Id(Long id);
    List<Schedule> getSchedulesByEmployees_Id(Long id);

    List<Schedule> getSchedulesByCustomers_Id(Long id);
}

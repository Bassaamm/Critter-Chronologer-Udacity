package com.udacity.jdnd.course3.critter.entities;

import com.udacity.jdnd.course3.critter.entities.EmployeeSkillType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionType;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Setter
@Getter
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="employee_id")
    private Long id;

    @Nationalized
    @Column
    private String name;

    @ElementCollection(fetch = FetchType.LAZY )
    @Column
    @Enumerated(EnumType.STRING)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @CollectionTable(name = "employee_skills", joinColumns = @JoinColumn(name = "employee_id"))
    private Set<EmployeeSkillType> skills;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="employee_weekdays",
            joinColumns = @JoinColumn(name="employee_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Column
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysAvailable;

    @ManyToMany(mappedBy = "employees")
    List<Schedule> schedules;


}
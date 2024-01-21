package com.udacity.jdnd.course3.critter.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //他是一個獨立table, 連接employee_id跟schedule_id,作為schedule跟employee之間的關聯表
    @ManyToMany
    @JoinTable(name = "schedule_employee", joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Employee> employees;

    //他也是一個獨立的table，連接pet_id跟schedule_id的關聯表
    @ManyToMany
    @JoinTable(name = "schedule_pet", joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Pet> pets;

    @ManyToMany
    @JoinTable(name = "schedule_customer", joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Customer> customers;

    private LocalDate date;

    //因為他不是獨立的entity所以使用elementCollection就可
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "schedule_activity", joinColumns = @JoinColumn(name = "skill_id"))
    @Column(name = "activity", nullable = false)
    @Enumerated(EnumType.STRING)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<EmployeeSkillType> activities;

}
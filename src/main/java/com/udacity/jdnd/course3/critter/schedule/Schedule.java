package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.Skill;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
public class Schedule {
    @Id
    @GeneratedValue
    private long id;

    private LocalDate date;

    //One schedule can involve many employees
    //and one employees can have many schedules
    @ManyToMany
    private List<Employee> employees;

    //one schedule can have many pets
    //and one pet can have many schedules
    @ManyToMany
    private List<Pet> pets;

    //one schedule needs many skills/activities
    //and one sill/activity is needed in many schedules
    @ManyToMany
    private List<Skill> activity;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<Skill> getActivity() {
        return activity;
    }

    public void setActivity(List<Skill> activity) {
        this.activity = activity;
    }
}

package com.udacity.jdnd.course3.critter.user;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@SequenceGenerator(name="employeeSeq",initialValue = 1)
public class Employee {
    @Id
    //@GeneratedValue
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "employeeSeq")
    private Long id;

    private String name;

    //Many employee can have many skills
    @ManyToMany
    private List<Skill> skill;
    //Many employees can have many days available
    @ManyToMany
    private Set<DayEntity> availableDays;

    public Set<DayEntity> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Set<DayEntity> availableDays) {
        this.availableDays = availableDays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee() {
        skill = new ArrayList<>();
    }

    public List<Skill> getSkill() {
        return skill;
    }

    public void setSkill(List<Skill> skill) {
        this.skill = skill;
    }
}

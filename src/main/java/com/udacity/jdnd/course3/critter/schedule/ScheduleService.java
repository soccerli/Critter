package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserService userService;

    public Schedule saveSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }
    public List<Schedule> getAllSchedules(){
        return (List<Schedule>)scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(Long petId){
        return scheduleRepository.findByPets_Id(petId);
    }
    public List<Schedule> getScheduleForEmployee(Long employeeId){
        return scheduleRepository.findByEmployees_Id(employeeId);
    }

    @Transactional
    public List<Schedule> getScheduleForCustomer(Long customerId){
        List<Pet> pets=userService.getPetsByOwner(customerId);
        if(pets==null) return null;

        List<Schedule> schedules=new ArrayList<>();
        for(Pet p:pets){
            List<Schedule> s=scheduleRepository.findByPets(p);
            if(s!=null) schedules.addAll(s);
        }
        return schedules;
    }

}

package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule,Long> {

    public List<Schedule> findByPets_Id(Long petId);
    public List<Schedule> findByEmployees_Id(Long employeeId);
    public List<Schedule> findByPets(Pet pet);
}

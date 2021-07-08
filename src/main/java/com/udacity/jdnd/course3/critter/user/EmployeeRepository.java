package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee,Long>{

    @Query("Select e from Employee e where e.name= :ename")
    Optional<Employee> queryEmployee(String ename);

    List<Employee> findByAvailableDays_DayValue(Integer dayValue);
}

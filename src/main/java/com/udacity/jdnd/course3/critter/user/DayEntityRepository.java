package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DayEntityRepository extends CrudRepository<DayEntity,Long> {

    @Query("Select d from DayEntity d where d.day=:day")
    Optional<DayEntity> getDay(String day);
}

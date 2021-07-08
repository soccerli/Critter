package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends CrudRepository<Skill,Long> {

    @Query("Select s from Skill s where s.name=:sname")
    Optional<Skill> querySkill(String sname);

}

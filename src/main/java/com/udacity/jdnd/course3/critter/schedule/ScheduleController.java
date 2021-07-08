package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    public ScheduleService scheduleService;
    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule=dtoConvertToSchedule(scheduleDTO);
        if(schedule==null) return null;//invalid scheduleDTO
        schedule=scheduleService.saveSchedule(schedule);
        if(schedule==null) return null;

        return convertToScheduleDTO(schedule);
        // throw new UnsupportedOperationException();
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules= scheduleService.getAllSchedules();
        return (convertToScheduleDTOList(schedules));
        //throw new UnsupportedOperationException();
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules= scheduleService.getScheduleForPet(petId);
        return (convertToScheduleDTOList(schedules));
        //throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules=scheduleService.getScheduleForEmployee(employeeId);
        return(convertToScheduleDTOList(schedules));
        //throw new UnsupportedOperationException();
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules=scheduleService.getScheduleForCustomer(customerId);
        return (convertToScheduleDTOList(schedules));
        //throw new UnsupportedOperationException();
    }

    @Transactional
    private Schedule dtoConvertToSchedule(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        schedule.setActivity(new ArrayList<Skill>());
        LocalDate ld=scheduleDTO.getDate();

        schedule.setDate(ld);
        List<Employee> employees= userService.getEmployees(scheduleDTO.getEmployeeIds());
        schedule.setEmployees(employees);
        List<Pet> pets=petService.getPets(scheduleDTO.getPetIds());
        schedule.setPets(pets);

        //verify requested employee IDs and Pet IDs are all valid
        if(employees.size()!=scheduleDTO.getEmployeeIds().size()
                ||pets.size()!=scheduleDTO.getPetIds().size()) return null;//not every employee or pet available in DB for such request

        //verify required date is available for each one employee
        Set<EmployeeSkill> employeeSkills=scheduleDTO.getActivities();
        List<String> requiredSkillNames = new ArrayList<>();
        if(employeeSkills!=null) {
            for (EmployeeSkill es : employeeSkills) {
                String name=es.name();
                Skill sk = userService.findSkillByName(name);
                if(sk==null) return null; //invalid skill in the request

                schedule.getActivity().add(sk);
                requiredSkillNames.add(es.name());
            }
        }

        //verify required skills are covered by skills of list employees
        List<String>  availableSkillNames = new ArrayList<>();
        List<Boolean> availability = new ArrayList<>();
        for(Employee e:employees){
            List<Skill> skills=e.getSkill();
            if(skills!=null)
                for(Skill s:skills){
                    if(!availableSkillNames.contains(s.getName()))
                              availableSkillNames.add(s.getName());
                }

            Set<DayEntity> des=e.getAvailableDays();
            if(des!=null){
                for(DayEntity d:des){
                    if(ld.getDayOfWeek()== DayOfWeek.valueOf(d.getDay())) availability.add(true);
                }
            }
        }

        //if all requested skills are available and all employee are available for the requested date
        if(availableSkillNames.containsAll(requiredSkillNames)
            &&availability.size()==employees.size()) return schedule;
        else return null;

    }

    private ScheduleDTO convertToScheduleDTO(Schedule schedule){
        if(schedule==null) return null;
        ScheduleDTO scheduleDTO= new ScheduleDTO();
        scheduleDTO.setActivities(new HashSet<EmployeeSkill>());
        scheduleDTO.setEmployeeIds(new ArrayList<Long>());
        scheduleDTO.setPetIds(new ArrayList<Long>());
        scheduleDTO.setId(schedule.getId());

        scheduleDTO.setDate(schedule.getDate());

        List<Employee> employees=schedule.getEmployees();
        if(employees!=null){
            List<Long> eids=scheduleDTO.getEmployeeIds();
            for(Employee e:employees){
                eids.add(e.getId());
            }
        }

        List<Pet> pets=schedule.getPets();
        if(pets!=null){
            List<Long> pids=scheduleDTO.getPetIds();
            for(Pet p:pets){
                pids.add(p.getId());
            }
        }

        Set<EmployeeSkill> activitiesDTO= scheduleDTO.getActivities();
        List<Skill> activities=schedule.getActivity();
        if(activities!=null){
            for(Skill s:activities){
                activitiesDTO.add(EmployeeSkill.valueOf(s.getName()));
            }
        }

        return scheduleDTO;
    }

    public List<ScheduleDTO> convertToScheduleDTOList(List<Schedule> schedules){
        if(schedules==null) return null;
        List<ScheduleDTO> scheduleDTOS=new ArrayList<>();
        for(Schedule s:schedules){
            ScheduleDTO scheduleDTO=convertToScheduleDTO(s);
            if(scheduleDTO!=null) scheduleDTOS.add(scheduleDTO);
        }
        return scheduleDTOS;
    }
}

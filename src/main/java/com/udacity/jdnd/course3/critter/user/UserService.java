package com.udacity.jdnd.course3.critter.user;

import com.google.common.collect.Lists;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private DayEntityRepository dayEntityRepository;

    private final Logger logger= LoggerFactory.getLogger(UserService.class);

    public Skill saveSkill(Skill skill){
        Optional<Skill> sks=skillRepository.querySkill(skill.getName());

        //If Skill already in DB, set the id for the input, and no need to save again
        if(sks.isPresent()){
            Skill s=sks.get();
            skill.setId(s.getId());
            return skill;
        }

        return skillRepository.save(skill);
    }

    public Skill findSkillByName(String name){
        Optional<Skill> sks=skillRepository.querySkill(name);
        if(sks.isPresent()) return sks.get();
        else return null;
    }

    @Transactional
    public DayEntity saveDayEntity(DayEntity dayEntity){
        Optional<DayEntity> op=dayEntityRepository.getDay(dayEntity.getDay());
        //if DayEntity is already in DB, set the id for the input, and no need to save again
        if(op.isPresent()){
            DayEntity de=op.get();
            dayEntity.setId(de.getId());
            return null;
        }
        return dayEntityRepository.save(dayEntity);
    }

    @Transactional
    public Employee saveEmployee(Employee employee){
        //persist  skills if any
        if(employee.getSkill()!=null) {
            for (Skill s : employee.getSkill()) {
                   saveSkill(s);
            }
        }
        //persist available days if any
        if(employee.getAvailableDays()!=null) {
            for (DayEntity d : employee.getAvailableDays()) {
                    saveDayEntity(d);
            }
        }
      return employeeRepository.save(employee);

    }
    public Employee findEmployee(Long id){
        Optional<Employee> op=employeeRepository.findById(id);
        if(op.isPresent()) return op.get();
        return null;
    }

    @Transactional
    public List<Employee> getEmployees(List<Long> employeeIds){
        List<Employee> employees=new ArrayList<>();
        if(employeeIds!=null){
            for(Long id:employeeIds){
                Employee employee=findEmployee(id);
                if(employee!=null) employees.add(employee);
            }
        }
        return employees;
    }

    @Transactional
    public void setAvailability(Set<DayOfWeek> days, Long employeeId){
        Employee employee=findEmployee(employeeId);
        if(employee==null) return; //always protection on null

        Set<DayEntity> availableDays= employee.getAvailableDays();
        if(availableDays==null) availableDays=new HashSet<>();
        else availableDays.clear();

        for(DayOfWeek day:days) {
            DayEntity de=new DayEntity();
            de.setDay(day.name());
            de.setDayValue(day.getValue());
            //if the day is not in DB yet, add it to DayEntity table
            //if the day is already in db, update ID for de
            Optional<DayEntity> op=dayEntityRepository.getDay(day.name());
            if(!op.isPresent()) de=dayEntityRepository.save(de);
            else de.setId(op.get().getId());
            availableDays.add(de);
        }
        employee.setAvailableDays(availableDays);
        //update employee with his  new availability
        employeeRepository.save(employee);
        return;
    }

    @Transactional
    public List<Employee> findEmployeesForService( EmployeeRequestDTO employeeRequestDTO){
        //Convert data in DTO to data of employee Entity
        Integer dayValue=employeeRequestDTO.getDate().getDayOfWeek().getValue();
        Set<EmployeeSkill> employeeSkills=employeeRequestDTO.getSkills();
        List<String> requiredSkillNames = new ArrayList<>();
        if(employeeSkills!=null){
            for(EmployeeSkill es:employeeSkills){
                requiredSkillNames.add(es.name());
            }
        }


        //first find all employees who is available on dayValue
        List<Employee> availableEmployeeList= employeeRepository.findByAvailableDays_DayValue(dayValue);
        //then let's check these employees' skills
        List<Employee> employeeList = new ArrayList<>();
        if(availableEmployeeList!=null){
            for(Employee ep:availableEmployeeList){
                logger.debug("findEmployeesForService: employeeName="+ep.getName());
                List<Skill> availableSkills=ep.getSkill();
                List<String> availableSkillNames = new ArrayList<>();
                for(Skill s: availableSkills) availableSkillNames.add(s.getName());

                if(availableSkillNames.containsAll(requiredSkillNames)){
                    employeeList.add(ep);
                }
            }
        }
        return employeeList;
    }

    public Customer saveCustomer(Customer customer){
          return customerRepository.save(customer);
    }

    public Customer findCustomer(Long id){
        Optional<Customer> cust =customerRepository.findById(id);
        if(cust.isPresent()) return cust.get();
        else return null;
    }
    public List<Customer> getAllCustomers(){
        return (List<Customer>) customerRepository.findAll();
    }

    @Transactional
    public Customer addPetToCustomer(Customer customer,Pet pet){
        logger.debug("UserService-addPetToCustomer:customerId="+customer.getId()+" petId="+pet.getId());
        List<Pet> petList=customer.getPets();
        if(petList==null) petList=new ArrayList<>();
        petList.add(pet);
        customer.setPets(petList);
        return saveCustomer(customer);

    }


    public List<Pet> getPetsByOwner(Long customerId){
        Customer customer=findCustomer(customerId);
        if(customer!=null) return customer.getPets();
        return null;
    }
    public Customer getOwnerByPets(Long petId){
        Optional<Pet> op=petRepository.findById(petId);
        if(op.isPresent()) return op.get().getCustomer();
        return null;
    }

}

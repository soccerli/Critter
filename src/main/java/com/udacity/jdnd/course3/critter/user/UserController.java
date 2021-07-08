package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    private final Logger logger= LoggerFactory.getLogger(UserController.class);

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return convertToCustomerDTO(userService.saveCustomer(dtoConvertToCustomer(customerDTO)));
       // throw new UnsupportedOperationException();
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers=userService.getAllCustomers();
        List<CustomerDTO> customerDTOS=converToCustomerDTOList(customers);
        return customerDTOS;
        //throw new UnsupportedOperationException();
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer= userService.getOwnerByPets(petId);
        return convertToCustomerDTO(customer);
        //throw new UnsupportedOperationException();
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee=dtoConvertToEmployee(employeeDTO);
        Employee rtne=userService.saveEmployee(employee);
        return(convertToEmployeeDTO(rtne));
        //throw new UnsupportedOperationException();
    }

    //@PostMapping("/employee/{employeeId}")
    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee=userService.findEmployee(employeeId);
        return(convertToEmployeeDTO(employee));
       // throw new UnsupportedOperationException();
    }

    //add this just because of the template file has the post request for get,
    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployeeByPost(@PathVariable long employeeId){
        Employee employee=userService.findEmployee(employeeId);
        return(convertToEmployeeDTO(employee));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        userService.setAvailability(daysAvailable,employeeId);
        return;

       // throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTODTO) {
        List<Employee> employeeList=userService.findEmployeesForService(employeeRequestDTODTO);
        List<EmployeeDTO> employeeDTOList=convertToEmployeeDTOList(employeeList);
        return employeeDTOList;
//        throw new UnsupportedOperationException();
    }



    private Employee dtoConvertToEmployee(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());



        Set<EmployeeSkill> employeeSkills=employeeDTO.getSkills();
        if(employeeSkills!=null) {
            employee.setSkill(new ArrayList<Skill>());
            for (EmployeeSkill es : employeeSkills) {
                Skill sk = new Skill();
                sk.setName(es.name());
                employee.getSkill().add(sk);
            }
        }

        Set<DayOfWeek> availableDays=employeeDTO.getDaysAvailable();
        if(availableDays!=null){
            employee.setAvailableDays(new HashSet<DayEntity>());
            for(DayOfWeek d:availableDays){
                DayEntity de=new DayEntity();
                logger.debug("dtoConvertToEmployee:d.name="+d.name()+" d.getValue="+d.getValue());
                de.setDay(d.name());
                de.setDayValue(d.getValue());
                logger.debug("dtoConvertToEmployee:de.Day="+de.getDay()+" de.DayValue="+de.getDayValue());
                employee.getAvailableDays().add(de);
            }
        }

        return employee;

    }

    private List<EmployeeDTO> convertToEmployeeDTOList(List<Employee> employeeList){
        if(employeeList==null) return null;
        List<EmployeeDTO> employeeDTOList=new ArrayList<>();
        for(Employee employee:employeeList){
                EmployeeDTO employeeDTO=convertToEmployeeDTO(employee);
                if(employeeDTO!=null) employeeDTOList.add(employeeDTO);
        }


        return employeeDTOList;
    }
    private EmployeeDTO convertToEmployeeDTO(Employee employee){
        if(employee==null) return null;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        Set<EmployeeSkill> s= null;
        List<Skill> skills=employee.getSkill();
        if(skills!=null) {
            s= new HashSet<EmployeeSkill>();
            for (Skill sk : skills) {
                s.add(EmployeeSkill.valueOf(sk.getName()));
            }
        }
        employeeDTO.setSkills(s);

        Set<DayOfWeek> daysOfAvailable=null;
        Set<DayEntity> dayEntities=employee.getAvailableDays();
        if(dayEntities!=null) {
            daysOfAvailable=new HashSet<DayOfWeek>();
            for (DayEntity de : dayEntities) {
                if(de==null) logger.debug("convertToEmployeeDTO de==null");
                int dv=de.getDayValue();
                daysOfAvailable.add(DayOfWeek.of(dv));
            }
        }
        employeeDTO.setDaysAvailable(daysOfAvailable);
        return employeeDTO;
    }

    private Customer dtoConvertToCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setNotes(customerDTO.getNotes());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        return customer;
    }

    private List<CustomerDTO> converToCustomerDTOList(List<Customer> customers){
        if(customers==null) return null;
        List<CustomerDTO> customerDTOS=new ArrayList<>();
        for(Customer customer:customers){
            CustomerDTO customerDTO=convertToCustomerDTO(customer);
            if(customerDTO!=null) customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }
    private CustomerDTO convertToCustomerDTO(Customer customer){
        if(customer==null) return null;
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setNotes(customer.getNotes());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        List<Long> petIds=null;
        if(customer.getPets()!=null) {
             petIds = new ArrayList<>();
            for (Pet pet : customer.getPets()) petIds.add(pet.getId());
        }
        customerDTO.setPetIds(petIds);


        return customerDTO;
    }

}

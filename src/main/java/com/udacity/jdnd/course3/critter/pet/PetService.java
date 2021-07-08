package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private UserService userService;

    private final Logger logger= LoggerFactory.getLogger(PetService.class);

    public Pet savePet(Pet pet){
        return petRepository.save(pet);
    }

    @Transactional
    public Pet savePet(Pet pet, Long ownerId){
        logger.debug("savePet, ownerId="+ownerId);
        //See if the request comes with ownerId
        Customer customer=userService.findCustomer((Long)ownerId);

        pet.setCustomer(customer);//if customer is null, it is ok to set
        Pet savedPet = savePet(pet);
        //if the there is an owner, update the owner and customer/pet relationship
        if(customer!=null){
            userService.addPetToCustomer(customer,pet);//add pet to customer and update DB
        }
        return savedPet;
    }
    public Pet getPet(Long id){
        Optional<Pet> op=petRepository.findById(id);
        if(op.isPresent()) return op.get();
        return null;
    }

    @Transactional
    public List<Pet> getPets(List<Long> petIds){
        List<Pet> pets=new ArrayList<>();
        if(petIds!=null){
            for(Long id:petIds){
                Pet pet=getPet(id);
                if(pet!=null) pets.add(pet);
            }
        }
        return pets;
    }
    public List<Pet> getAllPet(){
        return (List<Pet>)petRepository.findAll();
    }

}

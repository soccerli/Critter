package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private PetService petService;
    @Autowired
    private UserService userService;

    private Logger logger= LoggerFactory.getLogger(PetController.class);

    @PostMapping
    //Here is only creating new pet, not concerning pet update
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        long ownerId=petDTO.getOwnerId();
        Pet pet=dtoConvertToPet(petDTO);
        pet = petService.savePet(pet,ownerId);

        if(pet==null) return null;
        return convertToPetDTO(pet);
        //throw new UnsupportedOperationException();
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet=petService.getPet(petId);
        if(pet!=null) return convertToPetDTO(pet);
        return null;
        //throw new UnsupportedOperationException();
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> petList = petService.getAllPet();
        List<PetDTO> petDTOS=convertToPetDTOList(petList);
        return petDTOS;
        //throw new UnsupportedOperationException();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets=userService.getPetsByOwner(ownerId);
        if(pets==null) return null; //the customer doesn't exist
        List<PetDTO> petDTOS=convertToPetDTOList(pets);
        return petDTOS;
        //throw new UnsupportedOperationException();
    }

    private List<PetDTO> convertToPetDTOList(List<Pet> pets){
        List<PetDTO> petDTOS=new ArrayList<>();
        for(Pet pet:pets){
            petDTOS.add(convertToPetDTO(pet));
        }
        return petDTOS;
    }

    private Pet dtoConvertToPet(PetDTO petDTO){
        Pet pet = new Pet();
        pet.setId(petDTO.getId());
        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setNotes(petDTO.getNotes());
        pet.setBirthDate(petDTO.getBirthDate());
        return pet;

    }

    private PetDTO convertToPetDTO(Pet pet){
        PetDTO petDTO=new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setName(pet.getName());
        petDTO.setType(pet.getType());
        petDTO.setNotes(pet.getNotes());
        petDTO.setBirthDate(pet.getBirthDate());
        Customer customer=pet.getCustomer();
        Long ownerId=0L;
        if(customer !=null) ownerId=customer.getId();
        petDTO.setOwnerId(ownerId);
        return petDTO;
    }
}

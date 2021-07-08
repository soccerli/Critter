package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@SequenceGenerator(name="petSeq",initialValue = 1)
public class Pet {

    @Id
    //@GeneratedValue
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "petSeq")
    private long id;

    private PetType type;
    private String name;
    private LocalDate birthDate;
    private String notes;

    //Many pets can belong to the same customer,
    //but can't belong to more than one customer
    @ManyToOne
    private  Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

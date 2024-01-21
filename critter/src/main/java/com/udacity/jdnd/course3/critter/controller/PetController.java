package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dtos.PetDTO;
import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private PetService petService;
    private ModelMapper modelMapper;
    private UserService customerService;

    public PetController(PetService petService, UserService customerService) {
        this.petService = petService;
        this.modelMapper =  new ModelMapper();
        this.customerService = customerService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        Customer customer = customerService.getCustomerById(petDTO.getOwnerId());
        pet.setCustomer(customer);
        pet.setName(petDTO.getName());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());
        pet.setType(petDTO.getType());

        Pet newPet = petService.savePet(pet);
        customer.getPets().add(newPet);
        return convertToDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = this.petService.getPetByPetId(petId);
        return convertToDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets() {
        List<Pet> pet = this.petService.getAllPets();
        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet p: pet){
           petDTOs.add(convertToDTO(p));
        }
        return petDTOs;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pet = this.petService.getPetsByCustomerId(ownerId);
        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet p: pet){
           petDTOs.add(convertToDTO(p));
        }
        return petDTOs ;
    }

    private Pet convertToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setId(petDTO.getId());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setName(petDTO.getName());
        pet.setNotes(petDTO.getNotes());
        pet.setType(petDTO.getType());

        if(petDTO.getOwnerId() != 0){
            Customer customer = this.customerService.findOwnerByhisPetId(petDTO.getOwnerId());
            pet.setCustomer(customer);
        }

        return pet;
    }

    public PetDTO convertToDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setName(pet.getName());
        petDTO.setNotes(pet.getNotes());
        petDTO.setOwnerId(pet.getCustomer().getId());
        petDTO.setType(pet.getType());
        return petDTO;
    }
}
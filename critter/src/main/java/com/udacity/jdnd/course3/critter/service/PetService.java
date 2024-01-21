package com.udacity.jdnd.course3.critter.service;


import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    private PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet savePet(Pet pet){
        return this.petRepository.save(pet);
    }
    @SneakyThrows
    public Pet getPetByPetId(Long petId){
        Optional<Pet> optionalPet = this.petRepository.findById(petId);
        if(optionalPet.isPresent()){
            return optionalPet.get();
        }
        return optionalPet.orElseThrow(() -> new Exception("Pet with id: "+ petId + " not found"));
    }

    public List<Pet> getAllPets(){
        return this.petRepository.findAll();
    }

    public List<Pet> getPetsByCustomerId(Long id){
            return this.petRepository.getPetsByCustomer_Id(id);
    }
}

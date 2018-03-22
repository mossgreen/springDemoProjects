package com.moss.demo.petclinic.rest;

import com.moss.demo.petclinic.model.Pet;
import com.moss.demo.petclinic.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.awt.*;
import java.util.Collection;

public class PetRestController {

    @Autowired
    private ClinicService clinicService;

    @RequestMapping(value = "/{petId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Pet> getPet(@PathVariable("petId") int petId) {
        Pet pet = this.clinicService.findPetById(petId);
        if (pet == null) {
            return new ResponseEntity<Pet>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Pet>(pet, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<Pet>> getPets() {
        Collection<Pet> pets = this.clinicService.findAllPets();
        if (pets.isEmpty()) {
            return new ResponseEntity<Collection<Pet>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Pet>>(pets, HttpStatus.OK);
    }


}

package com.gufeifei.demo.petclinic.owner;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for pet forms
 * we're not using Bean validatoinBean validatoin annotations here because it's easier to define such validation rule in Java
 */
public class PetValidator implements Validator {

    private static final String REQUIRED = "required";

    @Override
    public void validate(Object obj, Errors errors) {
        Pet pet = (Pet) obj;
        String name = pet.getName();

        //name validation
        if (!StringUtils.hasLength(name)) {
            errors.rejectValue("name", REQUIRED, REQUIRED);
        }

        //type validation
        if (pet.isNew() && pet.getType() == null) {
            errors.rejectValue("type", REQUIRED, REQUIRED);
        }

        //birth date validation
        if (pet.getBirthDate() == null) {
            errors.rejectValue("birthDate", REQUIRED, REQUIRED);
        }
    }

    /**
     * This validator validates just Pet instances
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return Pet.class.isAssignableFrom(aClass);
    }
}

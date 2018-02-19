package com.gufeifei.demo.petclinic.owner;

import com.gufeifei.demo.petclinic.model.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


/*type of pet, can be dog, cat...*/

@Entity
@Table(name = "types")
public class PetType extends NamedEntity {
}

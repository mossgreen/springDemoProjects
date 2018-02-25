package com.moss.demo.petclinic.repository.jdbc;

import com.moss.demo.petclinic.model.Pet;

public class JdbcPet extends Pet {

    private int typeId;

    private int ownerId;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}

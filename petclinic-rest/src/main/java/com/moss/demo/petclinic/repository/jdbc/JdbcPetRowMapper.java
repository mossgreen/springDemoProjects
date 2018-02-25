package com.moss.demo.petclinic.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class JdbcPetRowMapper implements RowMapper<JdbcPet> {
    @Override
    public JdbcPet mapRow(ResultSet rs, int i) throws SQLException {
        JdbcPet pet = new JdbcPet();
        pet.setId(rs.getInt("pets.id"));
        pet.setName(rs.getString("name"));
        Date birthDate = rs.getDate("birth_date");
        pet.setBirthDate(new Date(birthDate.getTime()));
        pet.setTypeId(rs.getInt("type_id"));
        pet.setOwnerId(rs.getInt("owner_id"));
        return pet;
    }
}

package com.moss.demo.petclinic.repository.jdbc;

import com.moss.demo.petclinic.model.Visit;
import org.springframework.data.jdbc.core.OneToManyResultSetExtractor;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
public class JdbcPetVisitExtractor extends OneToManyResultSetExtractor<JdbcPet, Visit, Integer> {


    public JdbcPetVisitExtractor() {
        super(new JdbcPetRowMapper(), new JdbcVisitRowMapper());
    }


    @Override
    protected Integer mapPrimaryKey(ResultSet rs) throws SQLException {
        return rs.getInt("pets.id");
    }

    @Override
    protected Integer mapForeignKey(ResultSet rs) throws SQLException {
        if (rs.getObject("visits.pet_id") == null) {
            return null;
        } else {
            return rs.getInt("visits.pet_id");
        }
    }

    @Override
    protected void addChild(JdbcPet root, Visit child) {
        root.addVisit(child);
    }
}

package com.moss.demo.petclinic.repository.jdbc;

import com.moss.demo.petclinic.model.Visit;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class JdbcVisitRowMapper implements RowMapper<Visit> {
    @Override
    public Visit mapRow(ResultSet rs, int i) throws SQLException {
        Visit visit = new Visit();
        visit.setId(rs.getInt("visit_id"));
        Date visitDate = rs.getDate("visit_date");
        visit.setDate(new Date(visitDate.getTime()));
        visit.setDescription(rs.getString("description"));
        return visit;
    }
}

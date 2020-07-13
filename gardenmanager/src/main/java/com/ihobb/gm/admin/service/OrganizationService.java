package com.ihobb.gm.admin.service;

import com.ihobb.gm.admin.domain.Organization;

import java.sql.SQLException;
import java.util.List;

public interface OrganizationService {
    Organization fetchOrganizationByCode(String orgCode);

    List<Organization> fetchAll();

    Organization addOrganization(String description);

    String createOrganization(String orgCode) throws SQLException;
}

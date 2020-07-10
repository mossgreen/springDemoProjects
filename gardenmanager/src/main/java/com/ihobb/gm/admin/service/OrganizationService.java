package com.ihobb.gm.admin.service;

import com.ihobb.gm.admin.domain.Organization;

public interface OrganizationService {
    Organization fetchOrganizationByCode(String orgCode);
}
